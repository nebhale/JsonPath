/*
 * Copyright 2011 the original author or authors.
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

package com.nebhale.jsonpath;

import static com.nebhale.jsonpath.testutils.JsonUtils.STRING_INVALID;
import static com.nebhale.jsonpath.testutils.JsonUtils.STRING_VALID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.nebhale.jsonpath.internal.util.Sets;

public final class JsonPathTests {

    @Test
    public void compileValid() {
        assertNotNull(JsonPath.compile("$"));
    }

    @Test(expected = InvalidJsonPathExpressionException.class)
    public void compileInvalid() {
        JsonPath.compile(".");
    }

    @Test
    public void readStatic() {
        assertNotNull(JsonPath.read("$", STRING_VALID, Map.class));
    }

    @Test
    public void readValid() {
        assertNotNull(JsonPath.compile("$").read(STRING_VALID, Map.class));
    }

    @Test(expected = InvalidJsonException.class)
    public void readInvalid() {
        assertNotNull(JsonPath.compile("$").read(STRING_INVALID, Map.class));
    }

    @Test
    public void examples() {
        assertEquals("Sayings of the Century", JsonPath.read("$.store.book[0].title", STRING_VALID, String.class));
        assertEquals(Sets.asSet("Sayings of the Century", "Sword of Honour"), JsonPath.read("$.store.book[0,1].title", STRING_VALID, Set.class));
        assertEquals("Sayings of the Century", JsonPath.read("$['store']['book'][0]['title']", STRING_VALID, String.class));
        assertEquals(Arrays.asList("red", "blue"), JsonPath.read("$.store.bicycle.color", STRING_VALID, List.class));
        assertEquals(Arrays.asList("city", "hybrid", "downhill", "freeride"), JsonPath.read("$.store.bicycle.style", STRING_VALID, List.class));
    }
}
