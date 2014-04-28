package com.wandoulabs.onecache.core

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest.{FlatSpecLike, BeforeAndAfterAll, Matchers}

class OneCacheMapStoreSpec(_system: ActorSystem) extends TestKit(_system) with FlatSpecLike with Matchers with CacheBehaviors with ImplicitSender with BeforeAndAfterAll {

  def this() = this(ActorSystem("OneCacheSpec",
    ConfigFactory.parseString("")))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  val store = system.actorOf(MapStore.props(testActor))
  val cache: Cache = new OneCache(store)

  "OneCache" should behave like crud(cache)

}
