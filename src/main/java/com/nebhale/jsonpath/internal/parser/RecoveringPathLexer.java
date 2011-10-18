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

import java.util.ArrayList;
import java.util.List;

import com.nebhale.jsonpath.internal.parser.PathCharacter.CharacterType;
import com.nebhale.jsonpath.internal.parser.Token.TokenType;

/**
 * An implementation of {@link PathLexer} that tokenizes into the following types:
 * <p />
 * 
 * <pre>
 * ROOT:        ROOT
 * CHILD:       DOT_CHILD | ARRAY_CHILD
 * INDEX:       ARRAY_OPEN INDEX_CHARACTER* ARRAY_CLOSE
 * 
 * DOT_CHILD:   DOT SIMPLE_NAME_CHARACTER*
 * ARRAY_CHILD: ARRAY_OPEN ( QUOTE SIMPLE_NAME_CHARACTER* QUOTE | DOUBLE_QUOTE SIMPLE_NAME_CHARACTER* DOUBLE_QUOTE ) ARRAY_CLOSE
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

            if (context.parsingState == ParsingState.DEFAULT) {
                handleParsingStateDefault(context, c);
            } else if (context.parsingState == ParsingState.ARRAY) {
                handleParsingStateArray(context, c);
            } else if (context.parsingState == ParsingState.ARRAY_CHILD_DOUBLE_QUOTE) {
                handleArrayChildDoubleQuote(context, c);
            } else if (context.parsingState == ParsingState.ARRAY_CHILD_QUOTE) {
                handleArrayChildQuote(context, c);
            } else if (context.parsingState == ParsingState.DOT_CHILD) {
                handleParsingStateDotChild(context, c);
            } else if (context.parsingState == ParsingState.INDEX) {
                handleParsingStateIndex(context, c);
            }
        }

        return new LexerResult(context.tokenStream, context.problems);
    }

    private void emitArrayChild(LexerContext context) {
        context.tokenStream.add(createToken(TokenType.CHILD, context));
        context.parsingState = ParsingState.ARRAY;
    }

    private void emitDotChild(LexerContext context) {
        context.tokenStream.add(createToken(TokenType.CHILD, context));
        context.parsingState = ParsingState.DEFAULT;
    }

    private void emitIndex(LexerContext context) {
        context.tokenStream.add(createToken(TokenType.INDEX, context));
        context.parsingState = ParsingState.ARRAY;
    }

    private void emitIllegalCharacter(LexerContext context, PathCharacter c) {
        context.problems.add(new ExpressionProblem(context.expression, c.getPosition(), "Illegal character '%s'", c));
    }

    private void emitRoot(LexerContext context, PathCharacter c) {
        Token token = new Token(TokenType.ROOT, c.getPosition());
        context.tokenStream.add(token);
    }

    private void handleArrayChildDoubleQuote(LexerContext context, PathCharacter c) {
        if (c.isType(CharacterType.COMPLEX_NAME_CHARACTER)) {
            handleComplexNameCharacter(context, c);
        } else if (c.isType(CharacterType.DOUBLE_QUOTE)) {
            emitArrayChild(context);
            context.scanner.consume();
        } else {
            emitIllegalCharacter(context, c);
            context.scanner.consume();
        }
    }

    private void handleArrayChildQuote(LexerContext context, PathCharacter c) {
        if (c.isType(CharacterType.COMPLEX_NAME_CHARACTER)) {
            handleComplexNameCharacter(context, c);
        } else if (c.isType(CharacterType.QUOTE)) {
            emitArrayChild(context);
            context.scanner.consume();
        } else {
            emitIllegalCharacter(context, c);
            context.scanner.consume();
        }
    }

    private void handleArrayDigit(LexerContext context, PathCharacter c) {
        context.value.add(c);
        context.scanner.consume();
        context.parsingState = ParsingState.INDEX;
    }

    private void handleArrayDoubleQuote(LexerContext context) {
        context.scanner.consume();
        context.parsingState = ParsingState.ARRAY_CHILD_DOUBLE_QUOTE;
    }

    private void handleArrayClose(LexerContext context) {
        context.scanner.consume();
        context.parsingState = ParsingState.DEFAULT;
    }

    private void handleArrayOpen(LexerContext context) {
        context.scanner.consume();
        context.parsingState = ParsingState.ARRAY;
    }

    private void handleArrayQuote(LexerContext context) {
        context.scanner.consume();
        context.parsingState = ParsingState.ARRAY_CHILD_QUOTE;
    }

    private void handleComplexNameCharacter(LexerContext context, PathCharacter c) {
        context.value.add(c);
        context.scanner.consume();
    }

    private void handleDot(LexerContext context) {
        context.scanner.consume();
        context.parsingState = ParsingState.DOT_CHILD;
    }

    private void handleIndexCharacter(LexerContext context, PathCharacter c) {
        context.value.add(c);
        context.scanner.consume();
    }

    private void handleParsingStateArray(LexerContext context, PathCharacter c) {
        if (c.isType(CharacterType.DOUBLE_QUOTE)) {
            handleArrayDoubleQuote(context);
        } else if (c.isType(CharacterType.QUOTE)) {
            handleArrayQuote(context);
        } else if (c.isType(CharacterType.ARRAY_CLOSE)) {
            handleArrayClose(context);
        } else if (c.isType(CharacterType.DIGIT)) {
            handleArrayDigit(context, c);
        } else {
            emitIllegalCharacter(context, c);
            context.scanner.consume();
        }
    }

    private void handleParsingStateDefault(LexerContext context, PathCharacter c) {
        if (c.isType(CharacterType.ARRAY_OPEN)) {
            handleArrayOpen(context);
        } else if (c.isType(CharacterType.DOT)) {
            handleDot(context);
        } else if (c.isType(CharacterType.END)) {
            context.scanner.consume();
        } else if (c.isType(CharacterType.ROOT)) {
            emitRoot(context, c);
            context.scanner.consume();
        } else {
            emitIllegalCharacter(context, c);
            context.scanner.consume();
        }
    }

    private void handleParsingStateDotChild(LexerContext context, PathCharacter c) {
        if (c.isType(CharacterType.SIMPLE_NAME_CHARACTER)) {
            handleSimpleNameCharacter(context, c);
        } else {
            emitDotChild(context);
        }
    }

    private void handleParsingStateIndex(LexerContext context, PathCharacter c) {
        if (c.isType(CharacterType.INDEX_CHARACTER)) {
            handleIndexCharacter(context, c);
        } else if (c.isType(CharacterType.ARRAY_CLOSE)) {
            emitIndex(context);
        } else {
            emitIllegalCharacter(context, c);
            context.scanner.consume();
        }
    }

    private void handleSimpleNameCharacter(LexerContext context, PathCharacter c) {
        context.value.add(c);
        context.scanner.consume();
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
        ARRAY, //
        ARRAY_CHILD_DOUBLE_QUOTE, //
        ARRAY_CHILD_QUOTE, //
        DEFAULT, //
        DOT_CHILD, //
        INDEX
    }

    private static class LexerContext {

        private final String expression;

        private volatile ParsingState parsingState = ParsingState.DEFAULT;

        private final List<ExpressionProblem> problems = new ArrayList<ExpressionProblem>();

        private final PathScanner scanner;

        private final TokenStream tokenStream = new TokenStream();

        private final List<PathCharacter> value = new ArrayList<PathCharacter>();

        private LexerContext(String expression) {
            this.expression = expression;
            this.scanner = new StandardPathScanner(expression);
        }

    }

}
