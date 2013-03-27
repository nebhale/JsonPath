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

import com.nebhale.jsonpath.internal.component.PathComponent;

/**
 * An object that contains the results of a parser run
 * <p />
 *
 * <strong>Concurrent Semantics</strong><br />
 *
 * Thread-safe
 */
public final class ParserResult implements ProblemContainer {

    private final PathComponent pathComponent;

    private final List<ExpressionProblem> problems;

    /**
     * Create a new instance of {@link ParserResult} with a specified {@link PathComponent} and collection of problems
     * experienced while parsing
     *
     * @param pathComponent The path component created by parsing an expression
     * @param problems The collection of problems encountered while parsing an expression
     */
    public ParserResult(PathComponent pathComponent, List<ExpressionProblem> problems) {
        this.pathComponent = pathComponent;
        this.problems = problems;
    }

    /**
     * The path component created by parsing an expression
     *
     * @return The path component created by parsing an expression
     *
     * @throws IllegalStateException when {@link #getProblems()} returns a {@link List} that is not empty
     */
    public PathComponent getPathComponent() {
        if (!getProblems().isEmpty()) {
            throw new IllegalStateException("Unable to return a PathComponent when there are problems with the parse");
        }

        return this.pathComponent;
    }

    @Override
    public List<ExpressionProblem> getProblems() {
        return this.problems;
    }

    @Override
    public String toString() {
        return "ParserResult [pathComponent=" + this.pathComponent + ", problems=" + this.problems + "]";
    }

}
