package com.wandoulabs.onecache.core

import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestKit }
import com.typesafe.config.ConfigFactory
import org.scalatest.{ FlatSpecLike, BeforeAndAfterAll, Matchers }
import com.wandoulabs.onecache.core.store.MapStore
import com.wandoulabs.onecache.core.cache.{ OneCache, Cache }

abstract class OneCacheSpec(_system: ActorSystem) extends TestKit(_system) with FlatSpecLike with Matchers with CacheBehaviors with ImplicitSender with BeforeAndAfterAll {

  def name: String

  def cache: Cache

  val ttlSecs = 1

  def this() = this(ActorSystem("OneCacheSpec",
    ConfigFactory.parseString("")))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  def basicTest {
    name should behave like {
      crud(cache)
      expired(cache)
    }
  }

}

class MapStoreSpec extends OneCacheSpec {
  val store = system.actorOf(MapStore.props(testActor))
  val cache: Cache = new OneCache(store, ttlSecs)
  val name = "MapStore"

  basicTest

}
