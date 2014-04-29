package com.wandoulabs.onecache.core.cache

object Cache {

  final case class Put(val key: Any, val value: Any, val putIfAbsent: Boolean = false, val ttlSecs: Int = 0) extends Serializable

  final case class Get(val key: Any) extends Serializable

  final case class Remove(val key: Any) extends Serializable

}

trait Cache {

  def put(key: Any, value: Any): Unit

  def putIfAbsent(key: Any, value: Any): Boolean

  def get(key: Any): Option[Any]

  def remove(key: Any): Unit

}
