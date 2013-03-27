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

import static com.nebhale.jsonpath.testutils.JsonUtils.NODE;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

public final class AbstractChainedPathComponentTest {

    private final PathComponent delegate = mock(PathComponent.class);

    @Test
    public void getWithDelegate() {
        StubChainedPathComponent chainedPathComponent = new StubChainedPathComponent(this.delegate);
        chainedPathComponent.get(NODE);
        assertTrue(chainedPathComponent.selectCalled);
        verify(this.delegate).get(NODE);
    }

    @Test
    public void getWithoutDelegate() {
        StubChainedPathComponent chainedPathComponent = new StubChainedPathComponent(null);
        chainedPathComponent.get(NODE);
        assertTrue(chainedPathComponent.selectCalled);
    }

    private static final class StubChainedPathComponent extends AbstractChainedPathComponent {

        private volatile boolean selectCalled = false;

        public StubChainedPathComponent(PathComponent delegate) {
            super(delegate);
        }

        @Override
        protected JsonNode select(JsonNode input) {
            this.selectCalled = true;
            return input;
        }

    }

}
