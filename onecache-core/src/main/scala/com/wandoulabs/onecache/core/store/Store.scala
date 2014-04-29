package com.wandoulabs.onecache.core.store

import akka.actor.{ ActorRef, Actor }
import com.wandoulabs.onecache.core.cache.Cache
import Cache.{ Remove, Put, Get }
import com.wandoulabs.onecache.core.Statistics

object Store {

  final case class Element(val value: Any, val ttlSecs: Int = 0, var lastAccessedSecs: Int) extends Serializable

}

trait Store {
  self: Actor =>

  import Statistics._
  import Store._

  val stat: ActorRef

  def contains(key: Any): Boolean

  def put(key: Any, value: Element): Unit

  def putIfAbsent(key: Any, value: Element): Boolean = {
    if (contains(key)) {
      put(key, value)
      true
    } else {
      false
    }
  }

  def get(key: Any): Option[Element]

  def remove(key: Any): Unit

  def lastAccessedSecs = (System.currentTimeMillis() / 1000).toInt

  def receive: Receive = {
    case x @ Get(key) =>
      get(key) match {
        case Some(v) =>
          val currSecs = lastAccessedSecs
          val deltaSecs = currSecs - v.lastAccessedSecs
          if (v.ttlSecs <= 0 || v.ttlSecs > deltaSecs) {
            sender() ! Some(v.value)
            v.lastAccessedSecs = currSecs
            stat ! Hit(key)
          } else {
            sender() ! None
            stat ! Miss(key)
            remove(key)
          }
        case _ =>
          sender() ! None
          stat ! Miss(key)
      }
    case x @ Put(key, value, false, ttlSecs) =>
      put(key, Element(value, ttlSecs, lastAccessedSecs))
      stat ! Write(key)
    case x @ Put(key, value, true, ttlSecs) =>
      putIfAbsent(key, Element(value, ttlSecs, lastAccessedSecs)) match {
        case true =>
          sender() ! true
          stat ! Write(key)
        case false =>
          sender() ! false
      }
    case x @ Remove(key) =>
      remove(key)
      stat ! Delete(key)
  }
}

