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
package io.knotx.commons.json;

import java.util.Optional;
import java.util.function.Function;

import io.vertx.core.json.JsonObject;

public class JsonObjectUtil {

  private static Function<JsonObject, JsonObject> toJsonObject(String key) {
    return json -> json.getJsonObject(key);
  }

  private static Function<JsonObject, String> toString(String key) {
    return json -> Optional.ofNullable(json.getValue(key))
        .map(Object::toString)
        .orElse("");
  }

  public static String getString(String key, JsonObject parent) {
    return getValue(key, parent, JsonObjectUtil::toString);
  }

  public static JsonObject getJsonObject(String key, JsonObject parent) {
    return getValue(key, parent, JsonObjectUtil::toJsonObject);
  }

  public static <T> T getValue(String key, JsonObject parent,
      Function<String, Function<JsonObject, T>> func) {
    int dotIndex = key.indexOf('.');

    if (dotIndex == -1) {
      return func.apply(key)
          .apply(parent);
    }

    String newKey = key.substring(dotIndex + 1);
    JsonObject child = getChild(key, parent, dotIndex);

    return child == null ? null : getValue(newKey, child, func);
  }

  private static JsonObject getChild(String key, JsonObject parent, int dotIndex) {
    String childKey = key.substring(0, dotIndex);
    return parent.getJsonObject(childKey);
  }
}
