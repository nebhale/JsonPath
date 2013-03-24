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
import java.util.List;

final class TokenStream {

    private final List<Token> tokens = new ArrayList<Token>();

    private volatile int position = 0;

    void add(Token token) {
        this.tokens.add(token);
    }

    Token remove() {
        return this.tokens.get(this.position++);
    }

    boolean hasToken() {
        return this.position < this.tokens.size();
    }

    @Override
    public String toString() {
        return "TokenStream [tokens=" + this.tokens + ", position=" + this.position + "]";
    }

}
