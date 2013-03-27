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

import java.util.Arrays;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.nebhale.jsonpath.internal.util.ArrayUtils;

/**
 * A {@link PathComponent} that handles numeric indexed children
 * <p />
 *
 * <strong>Concurrent Semantics</strong><br />
 *
 * Thread-safe
 */
public final class IndexPathComponent extends AbstractChainedPathComponent {

    private final int[] indexes;

    public IndexPathComponent(PathComponent delegate, String indexes) {
        super(delegate);
        this.indexes = ArrayUtils.parseAsIntArray(indexes);
    }

    @Override
    protected JsonNode select(JsonNode input) {
        if (this.indexes.length == 1) {
            return input.get(this.indexes[0]);
        } else {
            ArrayNode result = JsonNodeFactory.instance.arrayNode();
            for (int index : this.indexes) {
                result.add(input.get(index));
            }
            return result;
        }
    }

    @Override
    public String toString() {
        return "IndexPathComponent [indexes=" + Arrays.toString(this.indexes) + "]";
    }

}
