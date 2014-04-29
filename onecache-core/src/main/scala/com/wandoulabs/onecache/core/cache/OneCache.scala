package com.wandoulabs.onecache.core.cache

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._

class OneCache(val store: ActorRef, val ttlSecs: Int = 0) extends Cache {

  import Cache._

  override def put(key: Any, value: Any): Unit = {
    store ! Put(key, value, false, ttlSecs)
  }

  override def putIfAbsent(key: Any, value: Any): Boolean = {
    implicit val timeout: Timeout = 5.seconds
    Await.result((store ? Put(key, value, true, ttlSecs)).mapTo[Boolean], Duration.Inf)
  }

  override def get(key: Any): Option[Any] = {
    implicit val timeout: Timeout = 5.seconds
    Await.result((store ? Get(key)).mapTo[Option[Any]], Duration.Inf)
  }

  override def remove(key: Any): Unit = {
    store ! Remove(key)
  }

}
