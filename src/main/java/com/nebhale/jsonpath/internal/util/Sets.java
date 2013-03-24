/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nebhale.jsonpath.internal.util;

import java.util.HashSet;
import java.util.Set;

/**
 * Utilities for dealing with {@link Set}s
 * <p />
 *
 * <strong>Concurrent Semantics</strong><br />
 *
 * Thread-safe
 */
public final class Sets {

    private Sets() {
    }

    /**
     * Creates a {@link Set} from a collection of items
     *
     * @param items The items to create the set from
     * @return The set created from the collection of items
     */
    public static <T> Set<T> asSet(T... items) {
        Set<T> set = new HashSet<T>();
        for (T item : items) {
            set.add(item);
        }
        return set;
    }
}
