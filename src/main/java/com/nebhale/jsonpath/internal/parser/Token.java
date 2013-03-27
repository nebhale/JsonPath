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

final class Token {

    private final TokenType type;

    private final String value;

    private final int startPosition;

    private final int endPosition;

    Token(TokenType type, int position) {
        this(type, null, position, position);
    }

    Token(TokenType type, String value, int startPosition, int endPosition) {
        this.type = type;
        this.value = value;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    TokenType getType() {
        return this.type;
    }

    String getValue() {
        return this.value;
    }

    int getStartPosition() {
        return this.startPosition;
    }

    int getEndPosition() {
        return this.endPosition;
    }

    enum TokenType {

        CHILD, //
        DEEP_WILDCARD, //
        INDEX, //
        ROOT, //
        WILDCARD

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + this.endPosition;
        result = (prime * result) + this.startPosition;
        result = (prime * result) + this.type.hashCode();
        result = (prime * result) + ((this.value == null) ? 0 : this.value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Token other = (Token) obj;
        if (this.endPosition != other.endPosition) {
            return false;
        }
        if (this.startPosition != other.startPosition) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Token [type=" + this.type + ", value=" + this.value + ", startPosition=" + this.startPosition + ", endPosition=" + this.endPosition
            + "]";
    }

}
