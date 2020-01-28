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

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JsonConverter {

  private JsonConverter() {
    // utility class
  }

  @SuppressWarnings("unchecked")
  public static Map<String, Object> plainMapFrom(JsonObject jsonObject) {
    Map<String, Object> plainMap = new HashMap<>(jsonObject.getMap());
    plainMap.replaceAll(JsonConverter::simplifyValue);
    return plainMap;
  }

  @SuppressWarnings("unchecked")
  public static List<Object> plainListFrom(JsonArray jsonArray) {
    List<Object> plainList = new ArrayList<Object>(jsonArray.getList());
    plainList.replaceAll(JsonConverter::simplifyValue);
    return plainList;
  }

  private static Object simplifyValue(String key, Object value) {
    return simplifyValue(value);
  }

  private static Object simplifyValue(Object value) {
    if (value instanceof JsonObject) {
      return plainMapFrom((JsonObject) value);
    } else if (value instanceof JsonArray) {
      return plainListFrom((JsonArray) value);
    } else {
      return value;
    }
  }

}
