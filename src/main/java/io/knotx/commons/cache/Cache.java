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
package io.knotx.commons.cache;

import io.reactivex.Maybe;

public interface Cache {

  /**
   * Lookup cache for value under the specified key. Returns an empty Maybe if no value was cached
   * under that key. Please note that null values (if permitted by cache) should be evaluated to an
   * empty Maybe.
   * <p>
   * This method must not throw exceptions.
   *
   * @param key lookup key
   * @return Maybe representing cached value, lack of it or an error
   */
  Maybe<Object> get(String key);

  /**
   * Put value into the cache under the specified key.
   * <p>
   * This method may throw an exception to signalize that storage was not successful.
   *
   * @param key   key under which the provided value will be stored
   * @param value value to be stored
   */
  void put(String key, Object value);

}
