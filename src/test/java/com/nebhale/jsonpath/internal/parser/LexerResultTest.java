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
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public final class LexerResultTest {

    private final TokenStream tokenStream = new TokenStream();

    private final List<ExpressionProblem> problems = new ArrayList<ExpressionProblem>();

    private final ExpressionProblem problem = new ExpressionProblem("test-source", 0, "test-message");

    private final LexerResult lexerResult = new LexerResult(this.tokenStream, this.problems);

    @Test
    public void getTokenStream() {
        assertSame(this.tokenStream, this.lexerResult.getTokenStream());
    }

    @Test(expected = IllegalStateException.class)
    public void getTokenStreamWithProblems() {
        this.problems.add(this.problem);
        this.lexerResult.getTokenStream();
    }

    @Test
    public void getProblems() {
        assertSame(this.problems, this.lexerResult.getProblems());
    }

    @Test
    public void testToString() {
        assertEquals("LexerResult [tokenStream=TokenStream [tokens=[], position=0], problems=[]]", this.lexerResult.toString());
    }

}
