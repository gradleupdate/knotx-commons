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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AllowedHeadersFilterTest {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_LOWERCASE = "content-type";
    private static final String ACCEPT = "Accept";
    private static final String ACCEPT_LOWERCASE = "accept";
    private static final String[] HEADERS = {
            ACCEPT,
            "Accept-Encoding",
            "Accept-Language",
            "Connection",
            "Host",
            "Upgrade-Insecure-Requests",
            "User-Agent",
            "Keep-Alive"
    };

    private static List<Pattern> preparePatters() {
        return Arrays.asList(HEADERS).stream()
            .map(Pattern::compile)
            .collect(Collectors.toList());
    }

    public static AllowedHeadersFilter getPatternFilter() {
        return AllowedHeadersFilter.create(preparePatters());
    }

    @Test
    @DisplayName("Check header with allowed headers list")
    void checkHeaderWithAllowedHeadersList() {
        AllowedHeadersFilter allowedHeadersFilter = getPatternFilter();
        assertFalse(allowedHeadersFilter.test(CONTENT_TYPE));
        assertTrue(allowedHeadersFilter.test(ACCEPT));
    }

    @Test
    @DisplayName("Check header with allowed headers list as case sensitive")
    void checkHeaderWithAllowedHeadersListCaseSensitive() {
        AllowedHeadersFilter allowedHeadersFilter = getPatternFilter();
        assertFalse(allowedHeadersFilter.test(ACCEPT_LOWERCASE));
        assertTrue(allowedHeadersFilter.test(ACCEPT));
    }

    @Test
    @DisplayName("Check header with allowed headers set case insensitive")
    void checkHeaderWithAllowedHeadersSetCaseInsensitive() {
        Set<String> strings = new HashSet<>(Arrays.asList(HEADERS));
        AllowedHeadersFilter allowedHeadersFilter = AllowedHeadersFilter.CaseInsensitive.create(strings);
        assertFalse(allowedHeadersFilter.test(CONTENT_TYPE_LOWERCASE));
        assertTrue(allowedHeadersFilter.test(ACCEPT_LOWERCASE));
    }

    @Test
    @DisplayName("Check header with allowed headers set")
    void checkHeaderWithAllowedHeadersSet() {
        Set<String> strings = new HashSet<>(Arrays.asList(HEADERS));
        AllowedHeadersFilter allowedHeadersFilter = AllowedHeadersFilter.CaseInsensitive.create(strings);
        assertFalse(allowedHeadersFilter.test(CONTENT_TYPE));
        assertTrue(allowedHeadersFilter.test(ACCEPT));
    }

    @Test
    @DisplayName("Check header with null allowed headers")
    void checkHeaderWithNullAllowedHeaders() {
        Set<String> strings = null;
        AllowedHeadersFilter allowedHeadersFilter = AllowedHeadersFilter.CaseInsensitive.create(strings);
        assertFalse(allowedHeadersFilter.test(CONTENT_TYPE));
    }

    @Test
    @DisplayName("Check header with empty set allowed headers")
    void checkHeaderWithEmptySetAllowedHeaders() {
        Set<String> strings = new HashSet<>();
        AllowedHeadersFilter allowedHeadersFilter = AllowedHeadersFilter.CaseInsensitive.create(strings);
        assertFalse(allowedHeadersFilter.test(CONTENT_TYPE));
    }
}