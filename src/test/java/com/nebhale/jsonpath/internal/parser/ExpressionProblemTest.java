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

import org.junit.Test;

public class ExpressionProblemTest {

    @Test
    public void problemPosition() {
        String output = new ExpressionProblem("/token1/token2/", 7, "test-message").toString();
        assertEquals("test-message\n" + //
            "/token1/token2/\n" + //
            "-------^\n", output);
    }

    @Test
    public void startAndStopPosition() {
        String output = new ExpressionProblem("/token1/token2/", 8, 13, "test-message").toString();
        assertEquals("test-message\n" + //
            "/token1/token2/\n" + //
            "--------^____^\n", output);
    }

}
