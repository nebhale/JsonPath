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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nebhale.jsonpath.internal.parser.PathCharacter.CharacterType;
import com.nebhale.jsonpath.internal.util.Sets;

/**
 * A {@link PathScanner} that scans into the following types:
 * <p />
 *
 * <pre>
 * COMPLEX_NAME_CHARACTER: LETTER | DIGIT | COMMA | HYPHEN | SPACE | UNDERSCORE
 * SIMPLE_NAME_CHARACTER:  LETTER | DIGIT | UNDERSCORE
 * INDEX_CHARACTER:        DIGIT | COMMA | SPACE
 * LETTER:                 [A-Za-z]
 * DIGIT:                  [0-9]
 *
 * ARRAY_CLOSE:     ]
 * ARRAY_OPEN:      [
 * COMMA:           ,
 * DOT:             .
 * DOUBLE_QUOTE:    "
 * HYPHEN:          -
 * QUOTE:           '
 * ROOT:            $
 * SPACE:          ' '
 * UNDERSCORE:      _
 * WILDCARD:        *
 * </pre>
 *
 * <strong>Concurrent Semantics</strong><br />
 *
 * Not thread-safe
 */
final class StandardPathScanner implements PathScanner {

    private static final char ARRAY_CLOSE = ']';

    private static final char ARRAY_OPEN = '[';

    private static final char COMMA = ',';

    private static final char DOT = '.';

    private static final char DOUBLE_QUOTE = '"';

    private static final char HYPHEN = '-';

    private static final char QUOTE = '\'';

    private static final char ROOT = '$';

    private static final char SPACE = ' ';

    private static final char UNDERSCORE = '_';

    private static final char WILDCARD = '*';

    private final List<PathCharacter> pathCharacters = new ArrayList<PathCharacter>();

    private volatile int position = 0;

    StandardPathScanner(String expression) {
        int counter = 0;
        for (char c : expression.toCharArray()) {
            Set<CharacterType> characterTypes = new HashSet<CharacterType>();

            if (c == ARRAY_CLOSE) {
                characterTypes.add(CharacterType.ARRAY_CLOSE);
            } else if (c == ARRAY_OPEN) {
                characterTypes.add(CharacterType.ARRAY_OPEN);
            } else if (c == COMMA) {
                characterTypes.add(CharacterType.COMMA);
            } else if (Character.isDigit(c)) {
                characterTypes.add(CharacterType.DIGIT);
            } else if (c == DOT) {
                characterTypes.add(CharacterType.DOT);
            } else if (c == DOUBLE_QUOTE) {
                characterTypes.add(CharacterType.DOUBLE_QUOTE);
            } else if (c == HYPHEN) {
                characterTypes.add(CharacterType.HYPHEN);
            } else if (Character.isLetter(c)) {
                characterTypes.add(CharacterType.LETTER);
            } else if (c == QUOTE) {
                characterTypes.add(CharacterType.QUOTE);
            } else if (c == ROOT) {
                characterTypes.add(CharacterType.ROOT);
            } else if (c == SPACE) {
                characterTypes.add(CharacterType.SPACE);
            } else if (c == UNDERSCORE) {
                characterTypes.add(CharacterType.UNDERSCORE);
            } else if (c == WILDCARD) {
                characterTypes.add(CharacterType.WILDCARD);
            }

            if (isComplexNameCharacter(c)) {
                characterTypes.add(CharacterType.COMPLEX_NAME_CHARACTER);
            }

            if (isSimpleNameCharacter(c)) {
                characterTypes.add(CharacterType.SIMPLE_NAME_CHARACTER);
            }

            if (isIndexCharacter(c)) {
                characterTypes.add(CharacterType.INDEX_CHARACTER);
            }

            this.pathCharacters.add(new PathCharacter(characterTypes, c, counter++));
        }

        this.pathCharacters.add(new PathCharacter(Sets.asSet(CharacterType.END), '\0', counter));
    }

    @Override
    public void consume() {
        this.position++;
    }

    @Override
    public PathCharacter get() {
        return this.pathCharacters.get(this.position);
    }

    @Override
    public boolean ready() {
        return this.position < this.pathCharacters.size();
    }

    private boolean isComplexNameCharacter(char c) {
        return Character.isLetter(c) || Character.isDigit(c) || (c == COMMA) || (c == HYPHEN) || (c == SPACE) || (c == UNDERSCORE);
    }

    private boolean isSimpleNameCharacter(char c) {
        return Character.isLetter(c) || Character.isDigit(c) || (c == UNDERSCORE);
    }

    private boolean isIndexCharacter(char c) {
        return Character.isDigit(c) || (c == COMMA) || (c == SPACE);
    }

    @Override
    public String toString() {
        return "StandardPathScanner [pathCharacters=" + this.pathCharacters + ", position=" + this.position + "]";
    }

}
