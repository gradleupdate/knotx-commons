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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AllowedHeadersFilter implements Predicate<String> {

  private final List<Pattern> patterns;

  private AllowedHeadersFilter(List<Pattern> patterns) {
    this.patterns = patterns;
  }

  public static AllowedHeadersFilter create(List<Pattern> regexes) {
    return new AllowedHeadersFilter(regexes);
  }

  private static AllowedHeadersFilter create(Set<String> expressions) {
    expressions = Objects.isNull(expressions) ? Collections.emptySet() : expressions;
    List<Pattern> patterns = expressions.stream()
        .map(expression -> Pattern.compile(expression, Pattern.CASE_INSENSITIVE))
        .collect(Collectors.toList());

    return AllowedHeadersFilter.create(patterns);
  }

  public static class CaseInsensitive {
    public static AllowedHeadersFilter create(Set<String> expressions) {
      return AllowedHeadersFilter.create(expressions);
    }
  }

  @Override
  public boolean test(String header) {
    return patterns.stream().anyMatch(pattern -> pattern.matcher(header).matches());
  }
}
