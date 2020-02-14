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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private static final String UPGRADE_INSECURE_REQUESTS = "Upgrade-Insecure-Requests";
    private static final String WILDCARD = ".*";
    private static final String[] HEADERS = {
            ACCEPT,
            "Accept-Encoding",
            "Accept-Language",
            "Connection",
            "Host",
            UPGRADE_INSECURE_REQUESTS,
            "User-Agent",
            "Keep-Alive"
    };
    private static final String[] WILDCARD_ALLOWED_HEADERS = {
        ACCEPT,
        "Accept-.*",
        "Connection",
        "Host",
        "Upgrade-Insecure-.*",
        "User-.*",
        "Keep-.*"
    };

    private static List<Pattern> preparePatters(String [] allowedHeaders) {
        return Arrays.stream(allowedHeaders)
            .map(Pattern::compile)
            .collect(Collectors.toList());
    }

    public static AllowedHeadersFilter getPatternFilter(String [] allowedHeaders) {
        return AllowedHeadersFilter.create(preparePatters(allowedHeaders));
    }

    @Test
    @DisplayName("Check header with allowed headers list")
    void checkHeaderWithAllowedHeadersList() {
        AllowedHeadersFilter allowedHeadersFilter = getPatternFilter(HEADERS);
        assertFalse(allowedHeadersFilter.test(CONTENT_TYPE));
        assertTrue(allowedHeadersFilter.test(ACCEPT));
    }

    @Test
    @DisplayName("Check header with allowed headers list as case sensitive")
    void checkHeaderWithAllowedHeadersListCaseSensitive() {
        AllowedHeadersFilter allowedHeadersFilter = getPatternFilter(HEADERS);
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

    @Test
    @DisplayName("Check header with wildcards variants allowed headers list")
    void checkHeaderWithWildcardsVariantsAllowedHeadersList() {
        Set<String> allowedHeaders = new HashSet<>(Arrays.asList(WILDCARD_ALLOWED_HEADERS));
        List<String> headers = Arrays.asList(HEADERS);

        List<String> filteredHeaders = headers.stream()
            .filter(AllowedHeadersFilter.CaseInsensitive.create(allowedHeaders))
            .collect(Collectors.toList());
        assertEquals(filteredHeaders, headers);
    }

    @Test
    @DisplayName("Check header with single wildcard sign")
    void checkHeaderWithSingleWildcardSignAllowedHeadersList() {
        Set<String> allowedHeaders = new HashSet<>(Collections.singletonList(WILDCARD));
        List<String> headers = Arrays.asList(HEADERS);

        List<String> filteredHeaders = headers.stream()
            .filter(AllowedHeadersFilter.CaseInsensitive.create(allowedHeaders))
            .collect(Collectors.toList());
        assertEquals(filteredHeaders, headers);
    }
}