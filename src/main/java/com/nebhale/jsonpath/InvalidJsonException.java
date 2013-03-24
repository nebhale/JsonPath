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
 * A {@link RuntimeException} that indicates that a payload is not legal JSON
 * <p />
 *
 * <strong>Concurrent Semantics</strong><br />
 *
 * Thread-safe
 */
public final class InvalidJsonException extends RuntimeException {

    private static final long serialVersionUID = 1507166234096259451L;

    /**
     * Constructs a new exception with the specified cause and a detail message of ({@code cause==null ? null :
     * cause.toString()}) (which typically contains the class and detail message of {@code cause}). This constructor is
     * useful for exceptions that are little more than wrappers for other throwables (for example,
     * {@link java.security.PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). (A
     *        {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public InvalidJsonException(Throwable cause) {
        super(cause);
    }

}
