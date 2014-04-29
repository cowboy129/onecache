package com.wandoulabs.onecache.core

import com.wandoulabs.onecache.core.cache.Cache

trait CacheManager {
  def getCache(name: Any): Cache
}
