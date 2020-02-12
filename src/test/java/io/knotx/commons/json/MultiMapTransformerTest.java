package io.knotx.commons.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.MultiMap;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MultiMapTransformerTest {

  @Test
  @DisplayName("Expect empty map to be transformed into empty JsonObject")
  void transformEmptyMap() {
    JsonObject output = MultiMapTransformer.toJson(MultiMap.caseInsensitiveMultiMap());

    assertEquals(new JsonObject(), output);
  }

  @Test
  @DisplayName("Expect entry with single value to be wrapped into JsonArray")
  void transformSingleValuedMap() {
    MultiMap input = MultiMap.caseInsensitiveMultiMap().add("key", "value");
    JsonObject output = MultiMapTransformer.toJson(input);

    assertEquals(1, output.getJsonArray("key").size());
    assertEquals("value", output.getJsonArray("key").getList().get(0));
  }

  @Test
  @DisplayName("Expect entry with many values to be wrapped into JsonArray")
  void transformMultiValuedMap() {
    MultiMap input = MultiMap.caseInsensitiveMultiMap()
        .add("key", "value1")
        .add("key", "value2")
        .add("key", "value3");
    JsonObject output = MultiMapTransformer.toJson(input);

    assertEquals(3, output.getJsonArray("key").size());
    assertEquals(Arrays.asList("value1", "value2", "value3"), output.getJsonArray("key").getList());
  }

}
