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
 * A {@link PathComponent} that handles the {@code $} character
 * <p />
 *
 * <strong>Concurrent Semantics</strong><br />
 *
 * Thread-safe
 */
public final class RootPathComponent extends AbstractChainedPathComponent {

    public RootPathComponent(PathComponent delegate) {
        super(delegate);
    }

    @Override
    protected JsonNode select(JsonNode input) {
        return input;
    }

    @Override
    public String toString() {
        return "RootPathComponent []";
    }

}
