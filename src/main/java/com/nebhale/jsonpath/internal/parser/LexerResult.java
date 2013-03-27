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

import java.util.List;

final class LexerResult implements ProblemContainer {

    private final TokenStream tokenStream;

    private final List<ExpressionProblem> problems;

    LexerResult(TokenStream tokenStream, List<ExpressionProblem> problems) {
        this.tokenStream = tokenStream;
        this.problems = problems;
    }

    TokenStream getTokenStream() {
        if (!this.problems.isEmpty()) {
            throw new IllegalStateException("Unable to return a TokenStream when there are problems with the lex");
        }

        return this.tokenStream;
    }

    @Override
    public List<ExpressionProblem> getProblems() {
        return this.problems;
    }

    @Override
    public String toString() {
        return "LexerResult [tokenStream=" + this.tokenStream + ", problems=" + this.problems + "]";
    }

}
