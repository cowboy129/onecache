package com.wandoulabs.onecache.core.store

import akka.actor.{ Actor, Props, ActorRef }

object MapStore {
  def props(stat: ActorRef): Props = Props(classOf[MapStore], stat)
}

class MapStore(val stat: ActorRef) extends Actor with Store {

  import Store._

  var storage = Map.empty[Any, Element]

  override def contains(key: Any): Boolean = storage.contains(key)

  override def get(key: Any): Option[Element] = storage.get(key)

  override def put(key: Any, value: Element): Unit = {
    storage += key -> value
  }

  override def remove(key: Any): Unit = {
    storage -= key
  }
}
