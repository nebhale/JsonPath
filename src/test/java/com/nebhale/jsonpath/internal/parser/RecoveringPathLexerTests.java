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

package com.nebhale.jsonpath.internal.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.nebhale.jsonpath.internal.parser.Token.TokenType;

public class RecoveringPathLexerTests {

    private final RecoveringPathLexer lexer = new RecoveringPathLexer();

    @Test(expected = IllegalStateException.class)
    public void lexProblems() {
        LexerResult result = this.lexer.lex("Q");
        assertEquals(result.getProblems().toString(), 1, result.getProblems().size());
        result.getTokenStream();
    }

    @Test
    public void illegalStartCharacter() {
        LexerResult result = this.lexer.lex("Q");
        assertEquals(result.getProblems().toString(), 1, result.getProblems().size());
    }

    @Test
    public void illegalArrayChildDoubleQuoteCharacter() {
        LexerResult result = this.lexer.lex("[\"&\"]");
        assertEquals(result.getProblems().toString(), 1, result.getProblems().size());
    }

    @Test
    public void illegalArrayChildQuoteCharacter() {
        LexerResult result = this.lexer.lex("['&']");
        assertEquals(result.getProblems().toString(), 1, result.getProblems().size());
    }

    @Test
    public void illegalArrayCharacter() {
        LexerResult result = this.lexer.lex("[&]");
        assertEquals(result.getProblems().toString(), 1, result.getProblems().size());
    }

    @Test
    public void illegalIndexCharacter() {
        LexerResult result = this.lexer.lex("[0&]");
        assertEquals(result.getProblems().toString(), 1, result.getProblems().size());
    }

    @Test
    public void root() {
        LexerResult result = this.lexer.lex("$");
        assertEquals(result.getProblems().toString(), 0, result.getProblems().size());
        assertEquals(new Token(TokenType.ROOT, 0), result.getTokenStream().remove());
    }

    @Test
    public void dotChild() {
        LexerResult result = this.lexer.lex(".dot_child");
        assertEquals(result.getProblems().toString(), 0, result.getProblems().size());
        assertEquals(new Token(TokenType.CHILD, "dot_child", 1, 9), result.getTokenStream().remove());
    }

    @Test
    public void arrayChildQuote() {
        LexerResult result = this.lexer.lex("['array_child']");
        assertEquals(result.getProblems().toString(), 0, result.getProblems().size());
        assertEquals(new Token(TokenType.CHILD, "array_child", 2, 12), result.getTokenStream().remove());
    }

    @Test
    public void arrayChildDoubleQuote() {
        LexerResult result = this.lexer.lex("[\"array_child\"]");
        assertEquals(result.getProblems().toString(), 0, result.getProblems().size());
        assertEquals(new Token(TokenType.CHILD, "array_child", 2, 12), result.getTokenStream().remove());
    }

    @Test
    public void index() {
        LexerResult result = this.lexer.lex("[0123456789]");
        assertEquals(result.getProblems().toString(), 0, result.getProblems().size());
        assertEquals(new Token(TokenType.INDEX, "0123456789", 1, 10), result.getTokenStream().remove());
    }

    @Test
    public void indexes() {
        LexerResult result = this.lexer.lex("[01234, 56789]");
        assertEquals(result.getProblems().toString(), 0, result.getProblems().size());
        assertEquals(new Token(TokenType.INDEX, "01234, 56789", 1, 12), result.getTokenStream().remove());
    }

}
