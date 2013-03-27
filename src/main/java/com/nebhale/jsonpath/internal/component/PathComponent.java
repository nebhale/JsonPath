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

package com.nebhale.jsonpath.internal.component;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Represents a segment of a JSONPath expression
 * <p />
 *
 * <strong>Concurrent Semantics</strong><br />
 *
 * Implementations must be thread-safe
 */
public interface PathComponent {

    /**
     * Returns a {@link JsonNode} that represents the current state of a JSONPath selection after this
     * {@link PathComponent} is finished with it
     *
     * @param input A {@link JsonNode} that represents the context that this JSONPath selection should work against
     *
     * @return the current state of a JSONPath selection after this {@link PathComponent} is finished with it
     */
    JsonNode get(JsonNode input);
}
