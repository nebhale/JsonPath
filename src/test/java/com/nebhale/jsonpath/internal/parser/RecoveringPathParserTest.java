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

package com.nebhale.jsonpath.internal.parser;

import static com.nebhale.jsonpath.testutils.AssertUtils.assertNoProblems;
import static com.nebhale.jsonpath.testutils.AssertUtils.assertProblemCount;
import static com.nebhale.jsonpath.testutils.JsonUtils.NODE;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public final class RecoveringPathParserTest {

    private final RecoveringPathParser parser = new RecoveringPathParser();

    @Test
    public void illegalToken() {
        ParserResult result = this.parser.parse(".dot_child");
        assertProblemCount(result, 1);
    }

    @Test
    public void root() {
        ParserResult result = this.parser.parse("$");

        assertNoProblems(result);
        assertEquals(NODE, result.getPathComponent().get(NODE));
    }

    @Test
    public void dotChild() {
        ParserResult result = this.parser.parse("$.store");

        assertNoProblems(result);
        assertEquals(NODE.get("store"), result.getPathComponent().get(NODE));
    }

    @Test
    public void arrayChild() {
        ParserResult result = this.parser.parse("$['store']");

        assertNoProblems(result);
        assertEquals(NODE.get("store"), result.getPathComponent().get(NODE));
    }

    @Test
    public void index() {
        ParserResult result = this.parser.parse("$.store.book[0]");

        assertNoProblems(result);
        assertEquals(NODE.get("store").get("book").get(0), result.getPathComponent().get(NODE));
    }

    @Test
    public void indexes() {
        JsonNode nodeBook = NODE.get("store").get("book");
        ArrayNode expected = JsonNodeFactory.instance.arrayNode();
        expected.add(nodeBook.get(0));
        expected.add(nodeBook.get(1));

        ParserResult result = this.parser.parse("$.store.book[0, 1]");

        assertNoProblems(result);
        assertEquals(expected, result.getPathComponent().get(NODE));
    }

    @Test
    public void deepWildcard() {
        ParserResult result = this.parser.parse("$..*");

        assertNoProblems(result);
        assertEquals(38, result.getPathComponent().get(NODE).size());
    }

    @Test
    public void wildcardDotChild() {
        JsonNode nodeStore = NODE.get("store");
        ArrayNode expected = JsonNodeFactory.instance.arrayNode();
        expected.add(nodeStore.get("book"));
        expected.add(nodeStore.get("bicycle"));

        ParserResult result = this.parser.parse("$.store.*");

        assertNoProblems(result);
        assertEquals(expected, result.getPathComponent().get(NODE));
    }

    @Test
    public void wildcardIndex() {
        JsonNode nodeBook = NODE.get("store").get("book");
        ArrayNode expected = JsonNodeFactory.instance.arrayNode();
        expected.add(nodeBook.get(0));
        expected.add(nodeBook.get(1));
        expected.add(nodeBook.get(2));
        expected.add(nodeBook.get(3));

        ParserResult result = this.parser.parse("$.store.book[*]");

        assertNoProblems(result);
        assertEquals(expected, result.getPathComponent().get(NODE));
    }

    @Test
    public void wildcardArrayChild() {
        JsonNode nodeStore = NODE.get("store");
        ArrayNode expected = JsonNodeFactory.instance.arrayNode();
        expected.add(nodeStore.get("book"));
        expected.add(nodeStore.get("bicycle"));

        ParserResult result = this.parser.parse("$.store[*]");

        assertNoProblems(result);
        assertEquals(expected, result.getPathComponent().get(NODE));
    }

    @Test
    public void chain() {
        assertEquals(NODE.get("store").get("book").get(0).get("title"), this.parser.parse("$.store.book[0].title").getPathComponent().get(NODE));
        assertEquals(NODE.get("store").get("book").get(0).get("title"),
            this.parser.parse("$['store']['book'][0]['title']").getPathComponent().get(NODE));
    }

    @Test(expected = IllegalStateException.class)
    public void lexProblems() {
        ParserResult result = this.parser.parse("Q");
        assertProblemCount(result, 1);
        result.getPathComponent();
    }

    @Test(expected = IllegalStateException.class)
    public void parserProblems() {
        ParserResult result = this.parser.parse("$$");
        assertProblemCount(result, 1);
        result.getPathComponent();
    }

    @Test
    public void testToString() {
        assertEquals("RecoveringPathParser []", this.parser.toString());
    }
}
