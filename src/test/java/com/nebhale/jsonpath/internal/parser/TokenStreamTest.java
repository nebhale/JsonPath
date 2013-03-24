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
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import com.nebhale.jsonpath.internal.parser.Token.TokenType;

public final class TokenStreamTest {

    private final TokenStream tokenStream = new TokenStream();

    private final Token token = new Token(TokenType.ROOT, 0);

    @Before
    public void addToken() {
        this.tokenStream.add(this.token);
    }

    @Test
    public void addRemoveHasToken() {
        assertEquals(this.token, this.tokenStream.remove());
        assertFalse(this.tokenStream.hasToken());
    }

    @Test
    public void testToString() {
        assertEquals("TokenStream [tokens=[Token [type=ROOT, value=null, startPosition=0, endPosition=0]], position=0]", this.tokenStream.toString());
    }

}
