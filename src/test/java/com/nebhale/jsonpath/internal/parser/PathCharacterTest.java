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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.nebhale.jsonpath.internal.parser.PathCharacter.CharacterType;
import com.nebhale.jsonpath.internal.util.Sets;
import com.nebhale.jsonpath.testutils.EqualsAndHashCodeTestUtils;

public final class PathCharacterTest {

    @Test
    public void test() {
        PathCharacter pathCharacter = new PathCharacter(Sets.asSet(CharacterType.ROOT), '$', 0);

        assertTrue(pathCharacter.isType(CharacterType.ROOT));
        assertEquals('$', pathCharacter.getValue());
        assertEquals(0, pathCharacter.getPosition());
    }

    @Test
    public void equalsHashCode() {
        new EqualsAndHashCodeTestUtils<PathCharacter>(new PathCharacter(Sets.asSet(CharacterType.ROOT), '$', 0)) //
        .assertEqual(new PathCharacter(Sets.asSet(CharacterType.ROOT), '$', 0)) //
        .assertNotEqual( //
            new PathCharacter(Sets.asSet(CharacterType.DIGIT), '$', 0), //
            new PathCharacter(Sets.asSet(CharacterType.ROOT), '0', 0), //
            new PathCharacter(Sets.asSet(CharacterType.ROOT), '$', 1));
    }

    @Test
    public void testToString() {
        assertEquals("PathCharacter [types=[ROOT], value=$, position=0]", new PathCharacter(Sets.asSet(CharacterType.ROOT), '$', 0).toString());
    }
}
