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

import com.nebhale.jsonpath.internal.component.ChildPathComponent;
import com.nebhale.jsonpath.internal.component.DeepWildcardPathComponent;
import com.nebhale.jsonpath.internal.component.IndexPathComponent;
import com.nebhale.jsonpath.internal.component.PathComponent;
import com.nebhale.jsonpath.internal.component.RootPathComponent;
import com.nebhale.jsonpath.internal.component.WildcardPathComponent;
import com.nebhale.jsonpath.internal.parser.Token.TokenType;

/**
 * An implementation of {@link PathParser} that parses into the following structure:
 * <p />
 *
 * <pre>
 * JSON_PATH:   ROOT ( CHILD | INDEX )*
 * </pre>
 *
 * <strong>Concurrent Semantics</strong><br />
 *
 * Thread-safe
 *
 * @see RecoveringPathLexer
 */
public final class RecoveringPathParser implements PathParser {

    @Override
    public ParserResult parse(String expression) {
        LexerResult lexerResult = new RecoveringPathLexer().lex(expression);
        if (lexerResult.getProblems().isEmpty()) {
            return parse(expression, lexerResult);
        }

        return new ParserResult(null, lexerResult.getProblems());
    }

    private ParserResult parse(String expression, LexerResult lexerResult) {
        TokenStream tokenStream = lexerResult.getTokenStream();
        List<ExpressionProblem> problems = new ArrayList<ExpressionProblem>();

        return new ParserResult(createFirstPathComponent(expression, tokenStream, problems), problems);
    }

    private PathComponent createFirstPathComponent(String expression, TokenStream tokenStream, List<ExpressionProblem> problems) {
        PathComponent pathComponent = null;

        if (tokenStream.hasToken()) {
            Token token = tokenStream.remove();

            if (token.getType() == TokenType.ROOT) {
                pathComponent = new RootPathComponent(createPathComponent(expression, tokenStream, problems));
            } else {
                problems.add(new ExpressionProblem(expression, token.getStartPosition(), token.getEndPosition(), "First token must be '$'"));
            }
        }

        return pathComponent;
    }

    private PathComponent createPathComponent(String expression, TokenStream tokenStream, List<ExpressionProblem> problems) {
        PathComponent pathComponent = null;

        if (tokenStream.hasToken()) {
            Token token = tokenStream.remove();

            if (token.getType() == TokenType.CHILD) {
                pathComponent = new ChildPathComponent(createPathComponent(expression, tokenStream, problems), token.getValue());
            } else if (token.getType() == TokenType.DEEP_WILDCARD) {
                pathComponent = new DeepWildcardPathComponent(createPathComponent(expression, tokenStream, problems));
            } else if (token.getType() == TokenType.INDEX) {
                pathComponent = new IndexPathComponent(createPathComponent(expression, tokenStream, problems), token.getValue());
            } else if (token.getType() == TokenType.WILDCARD) {
                pathComponent = new WildcardPathComponent(createPathComponent(expression, tokenStream, problems));
            } else {
                problems.add(new ExpressionProblem(expression, token.getStartPosition(), token.getEndPosition(), "Illegal token '%s'", token));
            }
        }

        return pathComponent;
    }

    @Override
    public String toString() {
        return "RecoveringPathParser []";
    }

}
