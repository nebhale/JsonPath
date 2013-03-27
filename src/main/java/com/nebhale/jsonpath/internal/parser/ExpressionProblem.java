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

/**
 * Represents a problem found during the parsing of a JSONPath expression
 * <p />
 *
 * <strong>Concurrent Semantics</strong><br />
 *
 * Thread-safe
 */
public final class ExpressionProblem {

    private final String message;

    private final String source;

    private final int startPosition;

    private final int endPosition;

    public ExpressionProblem(String source, int problemPosition, String message, Object... args) {
        this(source, problemPosition, problemPosition, message, args);
    }

    public ExpressionProblem(String source, int startPosition, int endPosition, String messageFormat, Object... args) {
        this.message = String.format(messageFormat, args);
        this.source = source;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.message).append("\n");
        sb.append(this.source).append("\n");

        for (int i = 0; i < this.startPosition; i++) {
            sb.append('-');
        }

        sb.append('^');

        int problemWidth = this.endPosition - this.startPosition;
        if (problemWidth > 0) {
            for (int i = 0; i < (problemWidth - 1); i++) {
                sb.append('_');
            }

            sb.append('^');
        }

        sb.append('\n');

        return sb.toString();
    }
}
