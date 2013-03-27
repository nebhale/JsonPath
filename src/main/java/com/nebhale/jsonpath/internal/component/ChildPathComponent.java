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
 * A {@link PathComponent} that handles child names
 * <p />
 *
 * <strong>Concurrent Semantics</strong><br />
 *
 * Thread-safe
 */
public final class ChildPathComponent extends AbstractChainedPathComponent {

    private final String[] names;

    public ChildPathComponent(PathComponent delegate, String names) {
        super(delegate);
        this.names = ArrayUtils.parseAsStringArray(names);
    }

    @Override
    protected JsonNode select(JsonNode input) {
        if (input.isArray()) {
            ArrayNode result = JsonNodeFactory.instance.arrayNode();
            for (JsonNode node : input) {
                JsonNode nodeResult = selectNode(node);
                if (nodeResult != null) {
                    if (nodeResult.isArray()) {
                        result.addAll((ArrayNode) nodeResult);
                    } else {
                        result.add(nodeResult);
                    }
                }
            }
            return result;
        } else {
            return selectNode(input);
        }
    }

    private JsonNode selectNode(JsonNode input) {
        if (this.names.length == 1) {
            return input.get(this.names[0]);
        } else {
            ArrayNode result = JsonNodeFactory.instance.arrayNode();
            for (String name : this.names) {
                result.add(input.get(name));
            }
            return result;
        }
    }

    @Override
    public String toString() {
        return "ChildPathComponent [names=" + Arrays.toString(this.names) + "]";
    }

}
