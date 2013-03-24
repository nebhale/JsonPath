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
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.nebhale.jsonpath.internal.parser.Token.TokenType;

public class RecoveringPathLexerTest {

    private final RecoveringPathLexer lexer = new RecoveringPathLexer();

    @Test(expected = IllegalStateException.class)
    public void lexProblems() {
        LexerResult result = this.lexer.lex("Q");
        assertProblemCount(result, 1);
        result.getTokenStream();
    }

    @Test
    public void illegalStartCharacter() {
        LexerResult result = this.lexer.lex("Q");
        assertProblemCount(result, 1);
    }

    @Test
    public void illegalArrayChildDoubleQuoteCharacter() {
        LexerResult result = this.lexer.lex("[\"&\"]");
        assertProblemCount(result, 1);
    }

    @Test
    public void illegalArrayChildQuoteCharacter() {
        LexerResult result = this.lexer.lex("['&']");
        assertProblemCount(result, 1);
    }

    @Test
    public void illegalArrayCharacter() {
        LexerResult result = this.lexer.lex("[&]");
        assertProblemCount(result, 1);
    }

    @Test
    public void illegalIndexCharacter() {
        LexerResult result = this.lexer.lex("[0&]");
        assertProblemCount(result, 1);
    }

    @Test
    public void illegalIndexWildcard() {
        LexerResult result = this.lexer.lex("[0*]");
        assertProblemCount(result, 1);
    }

    @Test
    public void illegalIndexWildcardLate() {
        LexerResult result = this.lexer.lex("[*0]");
        assertProblemCount(result, 1);
    }

    @Test
    public void illegalDotChildWildcard() {
        LexerResult result = this.lexer.lex(".dot_child*");
        assertProblemCount(result, 1);
    }

    @Test
    public void illegalDoubleQuoteWildcard() {
        LexerResult result = this.lexer.lex("[\"array_child*\"]");
        assertProblemCount(result, 1);
    }

    @Test
    public void illegalQuoteWildcard() {
        LexerResult result = this.lexer.lex("['array_child*']");
        assertProblemCount(result, 1);
    }

    @Test
    public void illegalQuoteWildcardLate() {
        LexerResult result = this.lexer.lex("['*a']");
        assertProblemCount(result, 1);
    }

    @Test
    public void root() {
        LexerResult result = this.lexer.lex("$");
        assertNoProblems(result);
        assertEquals(new Token(TokenType.ROOT, 0), result.getTokenStream().remove());
    }

    @Test
    public void dotChild() {
        LexerResult result = this.lexer.lex(".dot_child");
        assertNoProblems(result);
        assertEquals(new Token(TokenType.CHILD, "dot_child", 1, 9), result.getTokenStream().remove());
    }

    @Test
    public void deepWildcard() {
        LexerResult result = this.lexer.lex("..a");
        assertNoProblems(result);
        assertEquals(new Token(TokenType.DEEP_WILDCARD, 1), result.getTokenStream().remove());
        assertEquals(new Token(TokenType.CHILD, "a", 2, 2), result.getTokenStream().remove());
    }

    @Test
    public void arrayChildQuote() {
        LexerResult result = this.lexer.lex("['array_child']");
        assertNoProblems(result);
        assertEquals(new Token(TokenType.CHILD, "array_child", 2, 12), result.getTokenStream().remove());
    }

    @Test
    public void arrayChildDoubleQuote() {
        LexerResult result = this.lexer.lex("[\"array_child\"]");
        assertNoProblems(result);
        assertEquals(new Token(TokenType.CHILD, "array_child", 2, 12), result.getTokenStream().remove());
    }

    @Test
    public void arrayChildrenQuote() {
        LexerResult result = this.lexer.lex("['first_child, second_child']");
        assertNoProblems(result);
        assertEquals(new Token(TokenType.CHILD, "first_child, second_child", 2, 26), result.getTokenStream().remove());
    }

    @Test
    public void arrayChildrenDoubleQuote() {
        LexerResult result = this.lexer.lex("[\"first_child, second_child\"]");
        assertNoProblems(result);
        assertEquals(new Token(TokenType.CHILD, "first_child, second_child", 2, 26), result.getTokenStream().remove());
    }

    @Test
    public void index() {
        LexerResult result = this.lexer.lex("[0123456789]");
        assertNoProblems(result);
        assertEquals(new Token(TokenType.INDEX, "0123456789", 1, 10), result.getTokenStream().remove());
    }

    @Test
    public void indexes() {
        LexerResult result = this.lexer.lex("[01234, 56789]");
        assertNoProblems(result);
        assertEquals(new Token(TokenType.INDEX, "01234, 56789", 1, 12), result.getTokenStream().remove());
    }

    @Test
    public void wildcardIndex() {
        LexerResult result = this.lexer.lex("[*]");
        assertNoProblems(result);
        assertEquals(new Token(TokenType.WILDCARD, 1), result.getTokenStream().remove());
    }

    @Test
    public void wildcardName() {
        LexerResult result = this.lexer.lex(".*");
        assertNoProblems(result);
        assertEquals(new Token(TokenType.WILDCARD, 1), result.getTokenStream().remove());
    }

    @Test
    public void wildcardDoubleQuote() {
        LexerResult result = this.lexer.lex("[\"*\"]");
        assertNoProblems(result);
        assertEquals(new Token(TokenType.WILDCARD, 2), result.getTokenStream().remove());
    }

    @Test
    public void wildcardQuote() {
        LexerResult result = this.lexer.lex("['*']");
        assertNoProblems(result);
        assertEquals(new Token(TokenType.WILDCARD, 2), result.getTokenStream().remove());
    }

    @Test
    public void testToString() {
        assertEquals("RecoveringPathLexer []", this.lexer.toString());
    }

}
