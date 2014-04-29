package com.wandoulabs.onecache.core

import org.scalatest.FlatSpecLike
import akka.testkit.TestKit
import scala.concurrent.duration._
import com.wandoulabs.onecache.core.cache.Cache
import com.wandoulabs.onecache.core.Statistics.{ Delete, Write, Hit, Miss }

trait CacheBehaviors extends TestKit {
  this: FlatSpecLike =>

  def ttlSecs: Int

  def crud(cache: Cache) {
    it should "handle empty" in {
      assertResult(None)(cache.get("key"))
      awaitAssert {
        expectMsg(Miss("key"))
      }
    }

    it should "handle put" in {
      cache.put("key", "val")
      assertResult(Some("val"))(cache.get("key"))
      awaitAssert {
        expectMsg(Write("key"))
        expectMsg(Hit("key"))
      }
    }

    it should "handle putIfAbsent" in {
      cache.putIfAbsent("key2", "val2")
      awaitAssert {
        expectNoMsg(1.seconds)
      }
      assertResult(None)(cache.get("key2"))
      awaitAssert {
        expectMsg(Miss("key2"))
      }
      cache.putIfAbsent("key", "val2")
      awaitAssert {
        expectMsg(Write("key"))
      }
      assertResult(Some("val2"))(cache.get("key"))
      awaitAssert {
        expectMsg(Hit("key"))
      }
    }

    it should "handle remove" in {
      cache.remove("key")
      awaitAssert {
        expectMsg(Delete("key"))
      }
      assertResult(None)(cache.get("key"))
      awaitAssert {
        expectMsg(Miss("key"))
      }
    }
  }

  def expired(cache: Cache) {
    it should "expire in time" in {
      cache.put("expired", "val")
      assertResult(Some("val"))(cache.get("expired"))
      awaitAssert {
        expectMsg(Write("expired"))
        expectMsg(Hit("expired"))
      }

      // after sleep, the cache will expired
      Thread.sleep((ttlSecs + 1) * 1000)
      assertResult(None)(cache.get("expired"))
      awaitAssert {
        expectMsg(Miss("expired"))
      }
    }
  }
}
