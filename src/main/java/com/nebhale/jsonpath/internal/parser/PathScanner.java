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

interface PathScanner {

    /**
     * Gets the current {@link PathCharacter}. This method simply returns the character, but does not remove it from the
     * stream.
     *
     * @return The current {@link PathCharacter}
     */
    PathCharacter get();

    /**
     * Whether the scanner is ready to produce another {@link PathCharacter}
     *
     * @return Whether the scanner is ready to produce another {@link PathCharacter}
     */
    boolean ready();

    /**
     * Consumes the current {@link PathCharacter} from the stream. This method allows the stream to emit the next
     * {@link PathCharacter} in the sequence.
     */
    void consume();
}
