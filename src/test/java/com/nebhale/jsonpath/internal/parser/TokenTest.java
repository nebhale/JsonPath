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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.nebhale.jsonpath.internal.parser.Token.TokenType;
import com.nebhale.jsonpath.testutils.EqualsAndHashCodeTestUtils;

public final class TokenTest {

    @Test
    public void test() {
        Token token = new Token(TokenType.ROOT, 0);

        assertEquals(TokenType.ROOT, token.getType());
        assertNull(token.getValue());
        assertEquals(0, token.getStartPosition());
        assertEquals(0, token.getEndPosition());
    }

    @Test
    public void equalsHashCode() {
        new EqualsAndHashCodeTestUtils<Token>(new Token(TokenType.ROOT, "test-value", 0, 1)) //
        .assertEqual(new Token(TokenType.ROOT, "test-value", 0, 1)) //
        .assertNotEqual( //
            new Token(TokenType.CHILD, "test-value", 0, 1), //
            new Token(TokenType.ROOT, "different-test-value", 0, 1), //
            new Token(TokenType.ROOT, "test-value", 2, 1), //
            new Token(TokenType.ROOT, "test-value", 0, 2));

        new EqualsAndHashCodeTestUtils<Token>(new Token(TokenType.ROOT, 0)) //
        .assertEqual(new Token(TokenType.ROOT, 0)) //
        .assertNotEqual(new Token(TokenType.ROOT, "test-value", 0, 0));

    }

    @Test
    public void testToString() {
        assertEquals("Token [type=ROOT, value=test-value, startPosition=0, endPosition=1]", new Token(TokenType.ROOT, "test-value", 0, 1).toString());
    }

}
