package com.wandoulabs.onecache.core

import org.scalatest.FlatSpecLike
import akka.testkit.TestKit

trait CacheBehaviors extends TestKit {
  this: FlatSpecLike =>

  def crud(cache: Cache) {
    it should "handle empty" in {
      assertResult(None)(cache.get("key"))
    }

    it should "handle put" in {
      cache.put("key", "val")
      assertResult(Some("val"))(cache.get("key"))
    }

    it should "handle putIfAbsent" in {
      cache.putIfAbsent("key2", "val2")
      assertResult(None)(cache.get("key2"))
      cache.putIfAbsent("key", "val2")
      assertResult(Some("val2"))(cache.get("key"))
    }

    it should "handle remove" in {
      cache.remove("key")
      assertResult(None)(cache.get("key"))
    }
  }

}
