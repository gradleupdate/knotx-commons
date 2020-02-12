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
import io.vertx.reactivex.core.MultiMap;

public final class MultiMapTransformer {

  private MultiMapTransformer() {
    // utility class
  }

  /**
   * Transforms a given MultiMap into a JsonObject.
   * Map keys are translated into JsonObject's keys.
   * Map values are stored in the associated JsonArray.
   * @param map collection to transform
   * @return JsonObject with new representation
   */
  public static JsonObject toJson(MultiMap map) {
    JsonObject output = new JsonObject();
    map.entries().forEach(entry -> {
      final JsonArray values;
      if (output.containsKey(entry.getKey())) {
        values = output.getJsonArray(entry.getKey());
      } else {
        values = new JsonArray();
      }
      output.put(entry.getKey(), values.add(entry.getValue())
      );
    });
    return output;
  }

}
