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

package com.nebhale.jsonpath.testutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * A utility class for testing that the {@link Object#equals(Object)} and {@link Object#hashCode()} methods on an
 * instance work properly. This utility class is designed such that chained invocation is possible. It is typically used
 * in the following fashion:
 * 
 * <pre>
 * new EqualsAndHashCodeUtils&lt;String&gt;(&quot;a&quot;) //
 * .assertEqual(&quot;a&quot;) //
 * .assertNotEqual(&quot;b&quot;, &quot;c&quot;);
 * </pre>
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Thread-safe
 */
public final class EqualsAndHashCodeTestUtils<T> {

    private final T control;

    /**
     * Creates an instance of this utility with an {@link Object} to use as a control instance. Ensures the following
     * 
     * <ul>
     * <li>{@code control}'s type is not {@link Object}</li>
     * <li>{@code control} is not equal to a different type</li>
     * <li>{@code control} is not equal to null</li>
     * <li>{@code control} is equal to itself</li>
     * </ul>
     * 
     * @param control The {@link Object} to use as a control instance
     */
    public EqualsAndHashCodeTestUtils(T control) {
        this.control = control;

        assertFalse("control type cannot be Object", Object.class.getClass().equals(this.control.getClass()));
        assertFalse("control is equal to a different type", this.control.equals(new Object()));
        assertFalse("control is equal to null", this.control.equals(null));
        assertTrue("control is not equal to itself", this.control.equals(this.control));
    }

    /**
     * Asserts that an instance is equal to the control instance. This means the following
     * 
     * <ul>
     * <li>{@code control} is equal to {@code equal}</li>
     * <li>{@code equal} is equal to {@code}</li>
     * <li>{@code control}'s hash code is equal to {@code equal}'s hash code</li>
     * </ul>
     * 
     * @param equals The instances that should be equal to the control instance
     * 
     * @return The called instance to allow chained invocation
     */
    public EqualsAndHashCodeTestUtils<T> assertEqual(T... equals) {
        for (T equal : equals) {
            assertTrue(String.format("'%s' was not equal to '%s'", this.control, equal), this.control.equals(equal));
            assertTrue(String.format("'%s' was not equal to '%s'", equal, this.control), equal.equals(this.control));
            assertTrue(String.format("The hash code of '%s' was not equal to the hash code of '%s'", this.control, equal),
                this.control.hashCode() == equal.hashCode());
        }
        return this;
    }

    /**
     * Asserts that an instance is not equal to the control instance. This means the following
     * 
     * <ul>
     * <li>{@code control} is not equal to {@code notEqual}</li>
     * <li>{@code notEqual} is not equal to {@code}</li>
     * <li>{@code control}'s hash code is not equal to {@code notEqual}'s hash code</li>
     * </ul>
     * 
     * @param notEquals The instances that should not be equal to the control instance
     * 
     * @return The called instance to allow chained invocation
     */
    public EqualsAndHashCodeTestUtils<T> assertNotEqual(T... notEquals) {
        for (Object notEqual : notEquals) {
            assertFalse(String.format("'%s' was equal to '%s'", this.control, notEqual), this.control.equals(notEqual));
            assertFalse(String.format("'%s' was equal to '%s'", notEqual, this.control), notEqual.equals(this.control));
            assertFalse(String.format("The hash code of '%s' was equal to the hash code of '%s'", this.control, notEqual),
                this.control.hashCode() == notEqual.hashCode());
        }
        return this;
    }

}