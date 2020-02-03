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
package io.knotx.commons.http.request;

import io.vertx.reactivex.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataObjectsUtilTest {
    private static final String EMPTY = "";
    private static final String MULTI_VALUE =
            "Content-Type:text/html;charset=UTF-8;multipart/form-data|Keep-Alive:timeout=5|";
    public static final String[] CONTENT_TYPE_HTML = {
            "text/html",
            "charset=UTF-8"
    };
    private static final String[] CONTENT_TYPE_HTML_CLONE = CONTENT_TYPE_HTML.clone();
    private static final String[] CONTENT_TYPE_REVERSE = {
            "charset=UTF-8",
            "text/html"
    };

    private static final String JSON_BODY_BASE64 =
            "ewogICJvYmplY3QiOiB7CiAgICAiYSI6ICJiIiwKICAgICJjIjogImQiLAogICAgImUiOiAiZiIKICB9LAog" +
            "ICJhcnJheSI6IFsKICAgIDEsCiAgICAyCiAgXSwKICAic3RyaW5nIjogIkhlbGxvIFdvcmxkIgp9";
    private static final String OTHER_JSON_BODY_BASE64 =
            "ewogICJvYmplY3QiOiB7CiAgICAiYSI6ICJiIiwKICAgICJjIjogImQiLAogICAgImUiOiAiZiIKICB9Cn0";

    @Test
    @DisplayName("Expect two empty elements are the same")
    void equalsEmptyElementsAreTheSame() {
        MultiMap multi1 = MultiMap.caseInsensitiveMultiMap();
        MultiMap multi2 = MultiMap.caseInsensitiveMultiMap();
        assertTrue(DataObjectsUtil.equalsMultiMap(multi1, multi2));

        multi2.add("Content-Type", "text/html");
        assertFalse(DataObjectsUtil.equalsMultiMap(multi1, multi2));
    }

    @Test
    @DisplayName("Expect two case insensitive elements are the same")
    void expectCaseInsensitiveElementsAreTheSame() {
        MultiMap multi1 = MultiMap.caseInsensitiveMultiMap();
        MultiMap multi2 = MultiMap.caseInsensitiveMultiMap();

        multi1.add("content-type", "text/html");
        multi2.add("Content-Type", "text/html");

        assertTrue(DataObjectsUtil.equalsMultiMap(multi1, multi2));

        multi2.add("Content-Type", "multipart/form-data");
        assertFalse(DataObjectsUtil.equalsMultiMap(multi1, multi2));
    }

    @Test
    @DisplayName("Expect multi value elements are the same if they keep order")
    void expectMultiValueElementsAreTheSameIfKeepOrder() {
        MultiMap multi1 = MultiMap.caseInsensitiveMultiMap();
        MultiMap multi2 = MultiMap.caseInsensitiveMultiMap();

        List<String> contentType1 = Arrays.asList(CONTENT_TYPE_HTML);
        List<String> contentType2 = Arrays.asList(CONTENT_TYPE_HTML_CLONE);

        multi1.add("content-type", contentType1);
        multi2.add("Content-Type", contentType2);
        assertTrue(DataObjectsUtil.equalsMultiMap(multi1, multi2));
    }

    @Test
    @DisplayName("Expect multi value elements aren't the same if they don't keep order")
    void expectMultiValueElementsNotSameIfDontKeepOrder() {
        MultiMap multi1 = MultiMap.caseInsensitiveMultiMap();
        MultiMap multi2 = MultiMap.caseInsensitiveMultiMap();

        List<String> contentType1 = Arrays.asList(CONTENT_TYPE_HTML);
        List<String> contentTypeReverse = Arrays.asList(CONTENT_TYPE_REVERSE);

        multi1.add("Content-Type", contentType1);
        multi2.add("Content-Type", contentTypeReverse);
        assertFalse(DataObjectsUtil.equalsMultiMap(multi1, multi2));
    }

    @Test
    @DisplayName("Expect empty MultiMap is same as the empty string")
    void expectEmptyMultiMapIssameAsTheEmptyString() {
        MultiMap multi = MultiMap.caseInsensitiveMultiMap();
        assertEquals(DataObjectsUtil.toString(multi), EMPTY);
    }

    @Test
    @DisplayName("Expect correct format for not empty MultiMap to string")
    void expectCorrectFormatForNotEmptyMultiMapToString() {
        MultiMap multi = MultiMap.caseInsensitiveMultiMap();

        List<String> contentType = Arrays.asList(CONTENT_TYPE_HTML);
        multi.add("Content-Type", contentType);
        multi.add("Content-Type", "multipart/form-data");
        multi.add("Keep-Alive", "timeout=5");
        assertEquals(DataObjectsUtil.toString(multi), MULTI_VALUE);
        assertNotEquals(DataObjectsUtil.toString(multi), EMPTY);
    }

    @Test
    @DisplayName("Expect equality for hashCode")
    void expectEqualityForHashCode() {
        MultiMap multi1 = MultiMap.caseInsensitiveMultiMap();
        MultiMap multi2 = MultiMap.caseInsensitiveMultiMap();

        List<String> contentType1 = Arrays.asList(CONTENT_TYPE_HTML);
        List<String> contentType2 = Arrays.asList(CONTENT_TYPE_HTML_CLONE);

        multi1.add("Content-Type", contentType1);
        multi2.add("Content-Type", contentType2);
        assertEquals(DataObjectsUtil.multiMapHash(multi1), DataObjectsUtil.multiMapHash(multi2));

        multi2.add("Content-Type", "multipart/form-data");
        assertNotEquals(DataObjectsUtil.multiMapHash(multi1), DataObjectsUtil.multiMapHash(multi2));
    }

    @Test
    @DisplayName("Expect equality for request body")
    void expectEqualityForRequestBody() {
        Buffer buffer1 = Buffer.buffer(java.util.Base64.getDecoder().decode(JSON_BODY_BASE64));
        Buffer buffer2 = Buffer.buffer(java.util.Base64.getDecoder().decode(JSON_BODY_BASE64));
        assertTrue(DataObjectsUtil.equalsBody(buffer1, buffer2));

        Buffer buffer3 = Buffer.buffer(java.util.Base64.getDecoder().decode(OTHER_JSON_BODY_BASE64));
        assertFalse(DataObjectsUtil.equalsBody(buffer1, buffer3));
    }
}