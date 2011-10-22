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

package com.nebhale.jsonpath.internal.component;

import static com.nebhale.jsonpath.testutils.JsonUtils.NODE;
import static org.junit.Assert.assertEquals;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.junit.Test;

public final class IndexPathComponentTests {

    private static final JsonNode NODE_BOOK = NODE.get("store").get("book");

    @Test
    public void selectSingle() {
        JsonNode expected = NODE_BOOK.get(0);

        JsonNode result = new IndexPathComponent(null, "0").select(NODE_BOOK);

        assertEquals(expected, result);
    }

    @Test
    public void selectMultiple() {
        ArrayNode expected = JsonNodeFactory.instance.arrayNode();
        expected.add(NODE_BOOK.get(0));
        expected.add(NODE_BOOK.get(1));

        JsonNode result = new IndexPathComponent(null, "0, 1").select(NODE_BOOK);

        assertEquals(expected, result);
    }

}