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

package com.nebhale.jsonpath.internal.component;

import static com.nebhale.jsonpath.testutils.JsonUtils.NODE;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public final class WildcardPathComponentTest {

    @Test
    public void selectWildcardArray() {
        JsonNode nodeBook = NODE.get("store").get("book");

        ArrayNode expected = JsonNodeFactory.instance.arrayNode();
        expected.add(nodeBook.get(0));
        expected.add(nodeBook.get(1));
        expected.add(nodeBook.get(2));
        expected.add(nodeBook.get(3));

        JsonNode result = new WildcardPathComponent(null).select(nodeBook);

        assertEquals(expected, result);
    }

    @Test
    public void selectWildcardObject() {
        JsonNode nodeStore = NODE.get("store");

        ArrayNode expected = JsonNodeFactory.instance.arrayNode();
        expected.add(nodeStore.get("book"));
        expected.add(nodeStore.get("bicycle"));

        JsonNode result = new WildcardPathComponent(null).select(nodeStore);

        assertEquals(expected, result);
    }

    @Test
    public void testToString() {
        assertEquals("WildcardPathComponent []", new WildcardPathComponent(null).toString());
    }

}
