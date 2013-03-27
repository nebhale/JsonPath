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

import java.util.StringTokenizer;

/**
 * A utility class for dealing with arrays
 * <p />
 *
 * <strong>Concurrent Semantics</strong><br />
 *
 * Thread-safe
 */
public final class ArrayUtils {

    private static final String DELIMITERS = ", ";

    private ArrayUtils() {
    }

    public static int[] parseAsIntArray(String value) {
        StringTokenizer tokenizer = new StringTokenizer(value, DELIMITERS);
        int[] array = new int[tokenizer.countTokens()];
        for (int i = 0; i < array.length; i++) {
            array[i] = Integer.parseInt(tokenizer.nextToken());
        }
        return array;
    }

    public static String[] parseAsStringArray(String value) {
        StringTokenizer tokenizer = new StringTokenizer(value, DELIMITERS);
        String[] array = new String[tokenizer.countTokens()];
        for (int i = 0; i < array.length; i++) {
            array[i] = tokenizer.nextToken();
        }
        return array;
    }
}
