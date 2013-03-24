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

package com.nebhale.jsonpath;

/**
 * A {@link RuntimeException} that indicates that an expression is not legal JSONPath
 * <p />
 *
 * <strong>Concurrent Semantics</strong><br />
 *
 * Thread-safe
 */
public final class InvalidJsonPathExpressionException extends RuntimeException {

    private static final long serialVersionUID = 8552755743942956994L;

    /**
     * Constructs a new runtime exception with the specified detail message. The cause is not initialized, and may
     * subsequently be initialized by a call to {@link Throwable#initCause(java.lang.Throwable)}.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the
     *        {@link Throwable#getMessage()} method.
     */
    public InvalidJsonPathExpressionException(String message) {
        super(message);
    }

}
