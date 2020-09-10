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

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import java.util.Objects;

@DataObject(generateConverter = true)
public class InMemoryCacheOptions {

  private boolean enableMaximumSize = true;
  private boolean enableTtlAfterWrite = true;
  private boolean enableTtlAfterAccess = false;
  private long ttl = 5000L;
  private long ttlAfterAccessMs = 5000L;
  private Long ttlAfterWriteMs;
  private long maximumSize = 1000L;

  public InMemoryCacheOptions(JsonObject json) {
    InMemoryCacheOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject output = new JsonObject();
    InMemoryCacheOptionsConverter.toJson(this, output);
    return output;
  }

  public boolean isEnableMaximumSize() {
    return enableMaximumSize;
  }

  /**
   * Sets flag controlling whether cache size should be limited by number of entries. When enabled,
   * maximumSize option is passed to Cache builder.
   * <p>
   * Defaults to true.
   *
   * @param enableMaximumSize should size of cache be limited by number of entries
   * @return a reference to this so that API can be used fluently
   */
  public InMemoryCacheOptions setEnableMaximumSize(boolean enableMaximumSize) {
    this.enableMaximumSize = enableMaximumSize;
    return this;
  }

  public boolean isEnableTtlAfterWrite() {
    return enableTtlAfterWrite;
  }

  /**
   * Sets flag controlling whether TTL after write should be configured. When enabled, ttlAfterWrite
   * or ttl option is passed to Cache builder.
   * <p>
   * Defaults to true.
   *
   * @param enableTtlAfterWrite should TTL after write be configured
   * @return a reference to this so that API can be used fluently
   */
  public InMemoryCacheOptions setEnableTtlAfterWrite(boolean enableTtlAfterWrite) {
    this.enableTtlAfterWrite = enableTtlAfterWrite;
    return this;
  }

  public boolean isEnableTtlAfterAccess() {
    return enableTtlAfterAccess;
  }

  /**
   * Sets flag controlling whether TTL after access should be configured. When enabled, ttlAfterAccess
   * option is passed to Cache builder.
   * <p>
   * Defaults to false.
   *
   * @param enableTtlAfterAccess should TTL after access be configured
   * @return a reference to this so that API can be used fluently
   */
  public InMemoryCacheOptions setEnableTtlAfterAccess(boolean enableTtlAfterAccess) {
    this.enableTtlAfterAccess = enableTtlAfterAccess;
    return this;
  }

  public long getTtl() {
    return ttl;
  }

  /**
   * Legacy option for setting TTL after write in ms. Applies only when ttlAfterWrite is not set and
   * enableTtlAfterWrite is set to true.
   * <p>
   * Defaults to 5000ms.
   *
   * @param ttl TTL after write in millis
   * @return a reference to this so that API can be used fluently
   */
  @Deprecated
  public InMemoryCacheOptions setTtl(long ttl) {
    this.ttl = ttl;
    return this;
  }

  public long getTtlAfterAccessMs() {
    return ttlAfterAccessMs;
  }

  /**
   * TTL after access in millis. Applies only when enableTtlAfterAccess is set to true.
   * <p>
   * Defaults to 5000ms.
   *
   * @param ttlAfterAccessMs TTL after access in millis
   * @return a reference to this so that API can be used fluently
   */
  public InMemoryCacheOptions setTtlAfterAccessMs(long ttlAfterAccessMs) {
    this.ttlAfterAccessMs = ttlAfterAccessMs;
    return this;
  }

  public Long getTtlAfterWriteMs() {
    return ttlAfterWriteMs;
  }

  /**
   * TTL after write in millis. Applies only when enableTtlAfterWrite is set to true.
   * <p>
   * Not set by default.
   *
   * @param ttlAfterWriteMs TTL after write in millis
   * @return a reference to this so that API can be used fluently
   */
  public InMemoryCacheOptions setTtlAfterWriteMs(Long ttlAfterWriteMs) {
    this.ttlAfterWriteMs = ttlAfterWriteMs;
    return this;
  }

  public long getMaximumSize() {
    return maximumSize;
  }

  /**
   * Sets maximum cache size (number of entries).
   * <p>
   * Defaults to 1000.
   *
   * @param maximumSize maximum size in entries
   * @return a reference to this so that API can be used fluently
   */
  public InMemoryCacheOptions setMaximumSize(long maximumSize) {
    this.maximumSize = maximumSize;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InMemoryCacheOptions that = (InMemoryCacheOptions) o;
    return enableMaximumSize == that.enableMaximumSize &&
        enableTtlAfterWrite == that.enableTtlAfterWrite &&
        enableTtlAfterAccess == that.enableTtlAfterAccess &&
        ttl == that.ttl &&
        ttlAfterAccessMs == that.ttlAfterAccessMs &&
        maximumSize == that.maximumSize &&
        Objects.equals(ttlAfterWriteMs, that.ttlAfterWriteMs);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(enableMaximumSize, enableTtlAfterWrite, enableTtlAfterAccess, ttl, ttlAfterAccessMs,
            ttlAfterWriteMs, maximumSize);
  }

  @Override
  public String toString() {
    return "InMemoryCacheOptions{" +
        "enableMaximumSize=" + enableMaximumSize +
        ", enableTtlAfterWrite=" + enableTtlAfterWrite +
        ", enableTtlAfterAccess=" + enableTtlAfterAccess +
        ", ttl=" + ttl +
        ", ttlAfterAccessMs=" + ttlAfterAccessMs +
        ", ttlAfterWriteMs=" + ttlAfterWriteMs +
        ", maximumSize=" + maximumSize +
        '}';
  }
}
