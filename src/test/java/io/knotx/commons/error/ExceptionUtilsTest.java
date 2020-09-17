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
package io.knotx.commons.error;

import static org.junit.jupiter.api.Assertions.*;

import io.reactivex.exceptions.CompositeException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExceptionUtilsTest {

  @Test
  @DisplayName("Expect single exception when not composite")
  void singleException() {
    Throwable single = new RuntimeException();

    List<Throwable> flattened = ExceptionUtils.flatIfComposite(single);

    assertEquals(Collections.singletonList(single), flattened);
  }

  @Test
  @DisplayName("Expect flattened composite exception")
  void compositeException() {
    Throwable first = new RuntimeException();
    Throwable second = new IllegalStateException();
    Throwable composite = new CompositeException(first, second);

    List<Throwable> flattened = ExceptionUtils.flatIfComposite(composite);

    assertEquals(Arrays.asList(first, second), flattened);
  }

}
