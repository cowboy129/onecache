package com.wandoulabs.onecache.core

import akka.actor.Actor

object Statistics {

  final case class Hit(key: Any) extends Serializable

  final case class Miss(key: Any) extends Serializable

  final case class Write(key: Any) extends Serializable

  final case class Delete(key: Any) extends Serializable

  final case object GetStats

  final case class Stats(puts: Long, gets: Long, hits: Long, misses: Long, removals: Long)

}

class Statistics extends Actor {

  import Statistics._

  private[this] var cachePuts = 0L
  private[this] var cacheGets = 0L
  private[this] var cacheHits = 0L
  private[this] var cacheMisses = 0L
  private[this] var cacheRemovals = 0L

  override def receive: Receive = {
    case Hit(key) =>
      cacheGets += 1
      cacheHits += 1
    case Miss(key) =>
      cacheGets += 1
      cacheMisses += 1
    case Write(key) =>
      cachePuts += 1
    case Delete(key) =>
      cacheRemovals += 1
    case GetStats =>
      sender() ! Stats(cachePuts, cacheGets, cacheHits, cacheMisses, cacheRemovals)

  }

}
