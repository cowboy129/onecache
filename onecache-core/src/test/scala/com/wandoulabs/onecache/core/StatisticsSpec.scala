package com.wandoulabs.onecache.core

import akka.actor.{ Props, ActorSystem }
import akka.testkit.{ ImplicitSender, TestKit }
import org.scalatest.{ BeforeAndAfterAll, Matchers, FlatSpecLike }
import com.typesafe.config.ConfigFactory
import com.wandoulabs.onecache.core.Statistics._
import com.wandoulabs.onecache.core.Statistics.Write
import com.wandoulabs.onecache.core.Statistics.Delete
import com.wandoulabs.onecache.core.Statistics.Miss
import com.wandoulabs.onecache.core.Statistics.Hit

class StatisticsSpec(_system: ActorSystem) extends TestKit(_system) with FlatSpecLike with Matchers with ImplicitSender with BeforeAndAfterAll {

  def this() = this(ActorSystem("StatisticsSpec",
    ConfigFactory.parseString("")))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  val stat = system.actorOf(Props(classOf[Statistics]))

  it should "process stats" in {
    stat ! Hit("")
    stat ! Miss("")
    stat ! Delete("")
    stat ! Write("")
    stat ! GetStats
    awaitAssert {
      expectMsg(Stats(1, 2, 1, 1, 1))
    }
  }
}
