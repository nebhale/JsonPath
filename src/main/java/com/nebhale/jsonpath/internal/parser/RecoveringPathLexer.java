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

import java.util.ArrayList;
import java.util.List;

import com.nebhale.jsonpath.internal.parser.PathCharacter.CharacterType;
import com.nebhale.jsonpath.internal.parser.Token.TokenType;

/**
 * An implementation of {@link PathLexer} that tokenizes into the following types:
 * <p />
 *
 * <pre>
 * ROOT:            ROOT
 * CHILD:           DOT_CHILD | ARRAY_CHILD
 * INDEX:           ARRAY_OPEN ( INDEX_CHARACTER* | WILDCARD ) ARRAY_CLOSE
 * DOT_CHILD:       DOT SIMPLE_NAME
 * ARRAY_CHILD:     ARRAY_OPEN ( QUOTE COMPLEX_NAME QUOTE | DOUBLE_QUOTE COMPLEX_NAME DOUBLE_QUOTE ) ARRAY_CLOSE
 * SIMPLE_NAME:     SIMPLE_NAME_CHARACTER* | WILDCARD
 * COMPLEX_NAME:    COMPLEX_NAME_CHARACTER* | WILDCARD
 * </pre>
 *
 * <strong>Concurrent Semantics</strong><br />
 *
 * Thread-safe
 *
 * @see StandardPathScanner
 */
final class RecoveringPathLexer implements PathLexer {

    @Override
    public LexerResult lex(String expression) {
        LexerContext context = new LexerContext(expression);
        PathScanner scanner = context.scanner;

        while (scanner.ready()) {
            PathCharacter c = scanner.get();

            if (context.parsingState == ParsingState.BASE) {
                base(context, c);
            } else if (context.parsingState == ParsingState.ARRAY_OPEN) {
                arrayOpen(context, c);
            } else if (context.parsingState == ParsingState.ARRAY_CLOSE) {
                arrayClose(context, c);
            } else if (context.parsingState == ParsingState.DOUBLE_QUOTE_CHILD) {
                arrayChild(context, c, CharacterType.DOUBLE_QUOTE, ParsingState.DOUBLE_QUOTE_CLOSE);
            } else if (context.parsingState == ParsingState.QUOTE_CHILD) {
                arrayChild(context, c, CharacterType.QUOTE, ParsingState.QUOTE_CLOSE);
            } else if (context.parsingState == ParsingState.DOT_CHILD) {
                dotChild(context, c);
            } else if (context.parsingState == ParsingState.DOUBLE_QUOTE_CLOSE) {
                arrayChildClose(context, c, CharacterType.DOUBLE_QUOTE);
            } else if (context.parsingState == ParsingState.INDEX) {
                index(context, c);
            } else if (context.parsingState == ParsingState.QUOTE_CLOSE) {
                arrayChildClose(context, c, CharacterType.QUOTE);
            }
        }

        return new LexerResult(context.tokenStream, context.problems);
    }

    private void arrayChild(LexerContext context, PathCharacter c, CharacterType quoteType, ParsingState closeState) {
        if (c.isType(CharacterType.COMPLEX_NAME_CHARACTER)) {
            context.value.add(c);
            context.scanner.consume();
        } else if (c.isType(quoteType)) {
            context.tokenStream.add(createToken(TokenType.CHILD, context));
            context.parsingState = closeState;
        } else if (c.isType(CharacterType.WILDCARD)) {
            emitWildcard(context, c);
            context.scanner.consume();
            context.parsingState = closeState;
        } else {
            emitIllegalCharacter(context, c);
            context.scanner.consume();
            context.parsingState = closeState;
        }
    }

    private void arrayChildClose(LexerContext context, PathCharacter c, CharacterType quoteType) {
        if (c.isType(quoteType)) {
            context.parsingState = ParsingState.ARRAY_CLOSE;
        } else {
            emitIllegalCharacter(context, c);
        }
        context.scanner.consume();
    }

    private void arrayClose(LexerContext context, PathCharacter c) {
        if (c.isType(CharacterType.ARRAY_CLOSE)) {
            context.parsingState = ParsingState.BASE;
        } else {
            emitIllegalCharacter(context, c);
        }
        context.scanner.consume();
    }

    private void arrayOpen(LexerContext context, PathCharacter c) {
        if (c.isType(CharacterType.DOUBLE_QUOTE)) {
            context.scanner.consume();
            context.parsingState = ParsingState.DOUBLE_QUOTE_CHILD;
        } else if (c.isType(CharacterType.QUOTE)) {
            context.scanner.consume();
            context.parsingState = ParsingState.QUOTE_CHILD;
        } else if (c.isType(CharacterType.ARRAY_CLOSE)) {
            context.parsingState = ParsingState.ARRAY_CLOSE;
        } else if (c.isType(CharacterType.DIGIT)) {
            context.parsingState = ParsingState.INDEX;
        } else if (c.isType(CharacterType.WILDCARD)) {
            context.parsingState = ParsingState.INDEX;
        } else {
            emitIllegalCharacter(context, c);
            context.scanner.consume();
        }
    }

    private void base(LexerContext context, PathCharacter c) {
        if (c.isType(CharacterType.ARRAY_OPEN)) {
            context.parsingState = ParsingState.ARRAY_OPEN;
        } else if (c.isType(CharacterType.DOT)) {
            context.parsingState = ParsingState.DOT_CHILD;
        } else if (c.isType(CharacterType.ROOT)) {
            Token token = new Token(TokenType.ROOT, c.getPosition());
            context.tokenStream.add(token);
        } else if (!c.isType(CharacterType.END)) {
            emitIllegalCharacter(context, c);
        }
        context.scanner.consume();
    }

    private void dotChild(LexerContext context, PathCharacter c) {
        if (c.isType(CharacterType.SIMPLE_NAME_CHARACTER)) {
            context.value.add(c);
            context.scanner.consume();
        } else if (c.isType(CharacterType.WILDCARD)) {
            emitWildcard(context, c);
            context.scanner.consume();
            context.parsingState = ParsingState.BASE;
        } else if (context.value.isEmpty() && c.isType(CharacterType.DOT)) {
            Token token = new Token(TokenType.DEEP_WILDCARD, c.getPosition());
            context.tokenStream.add(token);
            context.scanner.consume();
        } else {
            context.tokenStream.add(createToken(TokenType.CHILD, context));
            context.parsingState = ParsingState.BASE;
        }
    }

    private void index(LexerContext context, PathCharacter c) {
        if (c.isType(CharacterType.INDEX_CHARACTER)) {
            context.value.add(c);
            context.scanner.consume();
        } else if (c.isType(CharacterType.WILDCARD)) {
            emitWildcard(context, c);
            context.scanner.consume();
            context.parsingState = ParsingState.ARRAY_CLOSE;
        } else if (c.isType(CharacterType.ARRAY_CLOSE)) {
            context.tokenStream.add(createToken(TokenType.INDEX, context));
            context.parsingState = ParsingState.ARRAY_CLOSE;
        } else {
            emitIllegalCharacter(context, c);
            context.scanner.consume();
        }
    }

    private void emitWildcard(LexerContext context, PathCharacter c) {
        if (context.value.isEmpty()) {
            Token token = new Token(TokenType.WILDCARD, c.getPosition());
            context.tokenStream.add(token);
        } else {
            emitIllegalCharacter(context, c);
        }
    }

    private void emitIllegalCharacter(LexerContext context, PathCharacter c) {
        context.problems.add(new ExpressionProblem(context.expression, c.getPosition(), "Illegal character '%s'", c));
    }

    private Token createToken(TokenType type, LexerContext context) {
        List<PathCharacter> value = context.value;

        StringBuilder sb = new StringBuilder();
        for (PathCharacter c : value) {
            sb.append(c.getValue());
        }

        int startPosition;
        int endPosition;
        if (value.isEmpty()) {
            startPosition = 0;
            endPosition = 0;
        } else {
            startPosition = value.get(0).getPosition();
            endPosition = value.get(value.size() - 1).getPosition();
        }

        context.value.clear();
        return new Token(type, sb.toString(), startPosition, endPosition);
    }

    private static enum ParsingState {
        ARRAY_OPEN, //
        ARRAY_CLOSE, //
        BASE, //
        DOT_CHILD, //
        DOUBLE_QUOTE_CHILD, //
        DOUBLE_QUOTE_CLOSE, //
        INDEX, //
        QUOTE_CHILD, //
        QUOTE_CLOSE
    }

    @Override
    public String toString() {
        return "RecoveringPathLexer []";
    }

    private static class LexerContext {

        private final String expression;

        private volatile ParsingState parsingState = ParsingState.BASE;

        private final List<ExpressionProblem> problems = new ArrayList<ExpressionProblem>();

        private final PathScanner scanner;

        private final TokenStream tokenStream = new TokenStream();

        private final List<PathCharacter> value = new ArrayList<PathCharacter>();

        private LexerContext(String expression) {
            this.expression = expression;
            this.scanner = new StandardPathScanner(expression);
        }

        @Override
        public String toString() {
            return "LexerContext [expression=" + this.expression + ", parsingState=" + this.parsingState + ", problems=" + this.problems
                + ", scanner=" + this.scanner + ", tokenStream=" + this.tokenStream + ", value=" + this.value + "]";
        }

    }

}
