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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonConverterTest {

  private static final String EMPTY_JSON_ARRAY = "[]";
  private static final String EMPTY_JSON_OBJECT = "{}";

  private static final String SAMPLE_JSON_OBJECT =
      "{\"outerArray\":[{\"innerArray\":[{\"property\":\"item1\"},{\"property\":\"item2\"}]}]}";
  private static final String SAMPLE_JSON_ARRAY = "[" + SAMPLE_JSON_OBJECT + "]";

  @Test
  @DisplayName("Expect Map created from JSONObject not to contain any JsonObject instances nested")
  void nestedJsonObjectsShouldBeAbsentInMap() {
    JsonObject original = new JsonObject(SAMPLE_JSON_OBJECT);

    Map<String, Object> map = JsonConverter.plainMapFrom(original);
    assertFalse(containsNestedInstance(map.values(), JsonObject.class));
  }

  @Test
  @DisplayName("Expect Map created from JSONObject not to contain any JsonArray instances nested")
  void nestedJsonArraysShouldBeAbsentInMap() {
    JsonObject original = new JsonObject(SAMPLE_JSON_OBJECT);

    Map<String, Object> map = JsonConverter.plainMapFrom(original);
    assertFalse(containsNestedInstance(map.values(), JsonArray.class));
  }

  @Test
  @DisplayName("Expect List created from JsonArray not to contain any JsonObject instances nested")
  void nestedJsonObjectsShouldBeAbsentInList() {
    JsonArray original = new JsonArray(SAMPLE_JSON_ARRAY);

    List<Object> list = JsonConverter.plainListFrom(original);
    assertFalse(containsNestedInstance(list, JsonObject.class));
  }

  @Test
  @DisplayName("Expect List created from JSONObject not to contain any JsonArray instances nested")
  void nestedJsonArraysShouldBeAbsentInList() {
    JsonArray original = new JsonArray(SAMPLE_JSON_ARRAY);

    List<Object> list = JsonConverter.plainListFrom(original);
    assertFalse(containsNestedInstance(list, JsonArray.class));
  }

  @Test
  @DisplayName("Expect Map created from copied JsonObject to be identical to the original's")
  void copiedMapShouldBeConvertedToOriginal() {
    JsonObject original = new JsonObject(SAMPLE_JSON_OBJECT);
    JsonObject copy = original.copy();

    Map transformedMap = JsonConverter.plainMapFrom(copy);
    assertNotEquals(original.getMap(), copy.getMap());
    assertEquals(original.getMap(), transformedMap);
  }

  @Test
  @DisplayName("Expect List created from copied JsonArray to be identical to the original's")
  void copiedListShouldBeConvertedToOriginal() {
    JsonArray original = new JsonArray(SAMPLE_JSON_ARRAY);
    JsonArray copy = original.copy();

    List transformedList = JsonConverter.plainListFrom(copy);
    assertNotEquals(original.getList(), copy.getList());
    assertEquals(original.getList(), transformedList);
  }

  @Test
  @DisplayName("Expect Map created from empty JsonObject to be empty")
  void emptyJsonObjectShouldConvertToEmptyMap() {
    JsonObject array = new JsonObject(EMPTY_JSON_OBJECT);

    Map map = JsonConverter.plainMapFrom(array);
    assertTrue(map.isEmpty());
  }

  @Test
  @DisplayName("Expect List created from empty JsonArray to be empty")
  void emptyJsonArrayShouldConvertToEmptyArray() {
    JsonArray array = new JsonArray(EMPTY_JSON_ARRAY);

    List list = JsonConverter.plainListFrom(array);
    assertTrue(list.isEmpty());
  }

  @SuppressWarnings("unchecked")
  private boolean containsNestedInstance(Collection<Object> input, Class clazz) {
    return input.stream()
        .anyMatch(clazz::isInstance)
        || input.stream()
        .map(this::toCollection)
        .anyMatch(collection -> containsNestedInstance(collection, clazz));
  }

  private Collection toCollection(Object object) {
    if (object instanceof List) {
      return (List) object;
    } else if (object instanceof Map) {
      return ((Map) object).values();
    } else {
      return Collections.emptyList();
    }
  }

}
