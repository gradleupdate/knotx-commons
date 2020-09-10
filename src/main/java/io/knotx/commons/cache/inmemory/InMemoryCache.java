/*
 * Copyright (C) 2019 Knot.x Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.knotx.commons.cache.inmemory;

import com.google.common.cache.CacheBuilder;
import io.knotx.commons.cache.Cache;
import io.reactivex.Maybe;
import java.util.concurrent.TimeUnit;

public class InMemoryCache implements Cache {

  private final com.google.common.cache.Cache<String, Object> cache;

  public InMemoryCache(InMemoryCacheOptions options) {
    CacheBuilder builder = CacheBuilder.newBuilder();
    if (options.isEnableMaximumSize()) {
      builder.maximumSize(options.getMaximumSize());
    }
    if (options.isEnableTtlAfterWrite()) {
      if (options.getTtlAfterWriteMs() != null) {
        builder.expireAfterWrite(options.getTtlAfterWriteMs(), TimeUnit.MILLISECONDS);
      } else {
        builder.expireAfterWrite(options.getTtl(), TimeUnit.MILLISECONDS);
      }
    }
    if (options.isEnableTtlAfterAccess()) {
      builder.expireAfterAccess(options.getTtlAfterAccessMs(), TimeUnit.MILLISECONDS);
    }
    cache = builder.build();
  }

  @Override
  public Maybe<Object> get(String key) {
    return Maybe.fromCallable(() -> cache.getIfPresent(key));
  }

  @Override
  public void put(String key, Object value) {
    cache.put(key, value);
  }

}
