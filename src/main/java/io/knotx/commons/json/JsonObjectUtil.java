package io.knotx.commons.json;

import java.util.function.Function;

import io.vertx.core.json.JsonObject;

public class JsonObjectUtil {

  private static Function<JsonObject, JsonObject> toJsonObject(String key) {
    return json -> json.getJsonObject(key);
  }

  private static Function<JsonObject, String> toString(String key) {
    return json -> json.getString(key);
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
