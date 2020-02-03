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
import java.util.HashSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MultiMapCollectorTest {
    public static final String[] CONTENT_TYPE_HTML = {
        "text/html",
        "charset=UTF-8"
    };
    private static final String[] HEADERS = {
        "Accept",
        "Accept-Encoding",
        "Accept-Language",
        "Connection",
        "Host",
        "Upgrade-Insecure-Requests",
        "User-Agent",
        "Keep-Alive"
    };

    @Test
    @DisplayName("Expect collector correct result")
    void expectCollectorCorrectResult() {
        MultiMap multi = MultiMap.caseInsensitiveMultiMap();

        List<String> contentType = Arrays.asList(CONTENT_TYPE_HTML);
        multi.add("Content-Type", contentType);
        multi.add("Content-Type", "multipart/form-data");
        multi.add("Keep-Alive", "timeout=5");


        MultiMap collectorResult = multi.names().stream()
            .filter(
                AllowedHeadersFilter.CaseInsensitive.create(new HashSet<>(Arrays.asList(HEADERS)))
            )
            .collect(MultiMapCollector.toMultiMap(o -> o, multi::getAll));

        MultiMap multiContentType = MultiMap.caseInsensitiveMultiMap();
        multiContentType.add("Keep-Alive", "timeout=5");

        assertTrue(DataObjectsUtil.equalsMultiMap(collectorResult, multiContentType));
        assertFalse(DataObjectsUtil.equalsMultiMap(collectorResult, multi));
    }
}