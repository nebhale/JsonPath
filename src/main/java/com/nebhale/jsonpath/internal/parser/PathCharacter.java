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

import java.util.Set;

final class PathCharacter {

    private final Set<CharacterType> types;

    private final char value;

    private final int position;

    PathCharacter(Set<CharacterType> types, char value, int position) {
        this.types = types;
        this.value = value;
        this.position = position;
    }

    boolean isType(CharacterType type) {
        return this.types.contains(type);
    }

    char getValue() {
        return this.value;
    }

    int getPosition() {
        return this.position;
    }

    enum CharacterType {
        ARRAY_CLOSE, //
        ARRAY_OPEN, //
        COMMA, //
        COMPLEX_NAME_CHARACTER, //
        DIGIT, //
        DOT, //
        DOUBLE_QUOTE, //
        END, //
        HYPHEN, //
        INDEX_CHARACTER, //
        LETTER, //
        QUOTE, //
        ROOT, //
        SIMPLE_NAME_CHARACTER, //
        SPACE, //
        UNDERSCORE, //
        WILDCARD;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + this.position;
        result = (prime * result) + this.types.hashCode();
        result = (prime * result) + this.value;
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
        PathCharacter other = (PathCharacter) obj;
        if (this.position != other.position) {
            return false;
        }
        if (!this.types.equals(other.types)) {
            return false;
        }
        if (this.value != other.value) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PathCharacter [types=" + this.types + ", value=" + this.value + ", position=" + this.position + "]";
    }

}
