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
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public final class ChildPathComponentTest {

    @Test
    public void selectSingle() {
        JsonNode expected = NODE.get("store");

        JsonNode result = new ChildPathComponent(null, "store").select(NODE);

        assertEquals(expected, result);
    }

    @Test
    public void selectMultiple() {
        JsonNode nodeStore = NODE.get("store");

        ArrayNode expected = JsonNodeFactory.instance.arrayNode();
        expected.add(nodeStore.get("book"));
        expected.add(nodeStore.get("bicycle"));

        JsonNode result = new ChildPathComponent(null, "book, bicycle").select(nodeStore);

        assertEquals(expected, result);
    }

    @Test
    public void selectDoesNotExist() {
        assertNull(new ChildPathComponent(null, "foo").select(NODE));
    }

    @Test
    public void selectArraySingle() {
        JsonNode nodeBook = NODE.get("store").get("book");

        ArrayNode expected = JsonNodeFactory.instance.arrayNode();
        expected.add(nodeBook.get(0).get("title"));
        expected.add(nodeBook.get(1).get("title"));
        expected.add(nodeBook.get(2).get("title"));
        expected.add(nodeBook.get(3).get("title"));

        JsonNode result = new ChildPathComponent(null, "title").select(nodeBook);

        assertEquals(expected, result);
    }

    @Test
    public void selectArrayMultiple() {
        JsonNode nodeBike = NODE.get("store").get("bicycle");

        ArrayNode expected = JsonNodeFactory.instance.arrayNode();
        expected.add(nodeBike.get(0).get("style").get(0));
        expected.add(nodeBike.get(0).get("style").get(1));
        expected.add(nodeBike.get(1).get("style").get(0));
        expected.add(nodeBike.get(1).get("style").get(1));

        JsonNode result = new ChildPathComponent(null, "style").select(nodeBike);

        assertEquals(expected, result);
    }

    @Test
    public void testToString() {
        assertEquals("ChildPathComponent [names=[name-1, name-2]]", new ChildPathComponent(null, "name-1,name-2").toString());
    }

}
