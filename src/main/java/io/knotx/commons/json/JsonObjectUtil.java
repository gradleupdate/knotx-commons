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

import static java.util.stream.Collectors.toList;

import io.vertx.core.json.JsonObject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;

public final class JsonObjectUtil {

  private JsonObjectUtil() {
    // utility class
  }

  public static Object getObject(String key, JsonObject parent) {
    return getValue(key, parent,
        (lastSelector, directParent) -> directParent.getValue(lastSelector));
  }

  public static JsonObject getJsonObject(String key, JsonObject parent) {
    return getValue(key, parent, JsonObjectUtil::toJsonObject);
  }

  public static String getString(String key, JsonObject parent) {
    return getValue(key, parent, JsonObjectUtil::toString);
  }

  public static void putValue(String key, JsonObject node, Object value) {
    getOrPutJsonObject(node, allButLastSelector(key))
        .put(lastSelector(key), value);
  }

  private static <T> T getValue(String key, JsonObject parent,
      BiFunction<String, JsonObject, T> func) {
    JsonObject directParent = getInnerJsonObject(parent, allButLastSelector(key));

    return func.apply(lastSelector(key), directParent);
  }

  private static List<String> allButLastSelector(String keyChain) {
    String[] keys = StringUtils.split(keyChain, ".");
    return Arrays.stream(keys)
        .limit(keys.length - 1)
        .collect(toList());
  }

  private static String lastSelector(String keyChain) {
    return StringUtils.contains(keyChain, '.')
        ? StringUtils.substringAfterLast(keyChain, ".")
        : keyChain;
  }

  private static JsonObject getInnerJsonObject(JsonObject node, List<String> keys) {
    for (String key : keys) {
      node = node.getJsonObject(key);
      if (node == null) {
        return new JsonObject();
      }
    }
    return node;
  }

  private static JsonObject getOrPutJsonObject(JsonObject node, List<String> keys) {
    for (String key : keys) {
      node = getOrPutJsonObject(node, key);
    }
    return node;
  }

  private static JsonObject getOrPutJsonObject(JsonObject node, String key) {
    JsonObject next = node.getJsonObject(key);
    if (next == null) {
      next = new JsonObject();
      node.put(key, next);
    }
    return next;
  }

  private static JsonObject toJsonObject(String key, JsonObject parent) {
    return parent.getJsonObject(key);
  }

  private static String toString(String key, JsonObject parent) {
    return Optional.ofNullable(parent.getValue(key))
        .map(Object::toString)
        .orElse("");
  }
}
