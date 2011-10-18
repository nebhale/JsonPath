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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.nebhale.jsonpath.internal.parser.PathCharacter.CharacterType;
import com.nebhale.jsonpath.internal.util.Sets;

public final class PathScannerTests {

    @Test
    public void test() {
        List<PathCharacter> expected = new ArrayList<PathCharacter>();
        expected.add(new PathCharacter(Sets.asSet(CharacterType.ARRAY_CLOSE), ']', 0));
        expected.add(new PathCharacter(Sets.asSet(CharacterType.ARRAY_OPEN), '[', 1));
        expected.add(new PathCharacter(Sets.asSet(CharacterType.DIGIT, CharacterType.SIMPLE_NAME_CHARACTER, CharacterType.COMPLEX_NAME_CHARACTER,
            CharacterType.INDEX_CHARACTER), '0', 2));
        expected.add(new PathCharacter(Sets.asSet(CharacterType.DOT), '.', 3));
        expected.add(new PathCharacter(Sets.asSet(CharacterType.DOUBLE_QUOTE), '"', 4));
        expected.add(new PathCharacter(Sets.asSet(CharacterType.HYPHEN, CharacterType.COMPLEX_NAME_CHARACTER), '-', 5));
        expected.add(new PathCharacter(Sets.asSet(CharacterType.LETTER, CharacterType.SIMPLE_NAME_CHARACTER, CharacterType.COMPLEX_NAME_CHARACTER),
            'A', 6));
        expected.add(new PathCharacter(Sets.asSet(CharacterType.QUOTE), '\'', 7));
        expected.add(new PathCharacter(Sets.asSet(CharacterType.ROOT), '$', 8));
        expected.add(new PathCharacter(
            Sets.asSet(CharacterType.UNDERSCORE, CharacterType.SIMPLE_NAME_CHARACTER, CharacterType.COMPLEX_NAME_CHARACTER), '_', 9));
        expected.add(new PathCharacter(Sets.asSet(CharacterType.COMMA, CharacterType.COMPLEX_NAME_CHARACTER, CharacterType.INDEX_CHARACTER), ',', 10));
        expected.add(new PathCharacter(Sets.asSet(CharacterType.SPACE, CharacterType.COMPLEX_NAME_CHARACTER, CharacterType.INDEX_CHARACTER), ' ', 11));
        expected.add(new PathCharacter(Sets.asSet(CharacterType.END), '\0', 12));

        StandardPathScanner p = new StandardPathScanner("][0.\"-A'$_, ");

        Iterator<PathCharacter> i = expected.iterator();
        while (p.ready()) {
            assertEquals(i.next(), p.get());
            p.consume();
        }

    }
}
