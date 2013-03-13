/*
 * Copyright 2011 the original author or authors.
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

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nebhale.jsonpath.internal.component.PathComponent;
import com.nebhale.jsonpath.internal.parser.ExpressionProblem;
import com.nebhale.jsonpath.internal.parser.ParserResult;
import com.nebhale.jsonpath.internal.parser.RecoveringPathParser;

/**
 * A compiled representation of a <a href="http://goessner.net/articles/JsonPath/">JSONPath expression</a>. JSONPath is
 * a way of using an XPath-like syntax to retrieve content from a JSON payload. The design of this class is intended to
 * resemble the Java {@link java.util.regex.Pattern} class.
 * <p />
 * A JSONPath expression, specified as a string, must first be compiled into an instance of this class. The resulting
 * pattern can then be used to read content from a JSON object.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Thread-safe both during compilation of the expression and the use of the resulting {@link JsonPath}
 * 
 * @see <a href="http://goessner.net/articles/JsonPath/">http://goessner.net/articles/JsonPath/</a>
 */
public final class JsonPath {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final PathComponent pathComponent;

    private JsonPath(PathComponent pathComponent) {
        this.pathComponent = pathComponent;
    }

    /**
     * Compiles the given JSONPath expression into a {@link JsonPath}
     * 
     * @param expression The expression to compile
     * 
     * @return A {@link JsonPath} that can be used to read content from JSON payloads
     * 
     * @throws InvalidJsonPathExpressionException if the {@code expression} argument is not a legal JSONPath expression
     */
    public static JsonPath compile(String expression) {
        ParserResult parserResult = new RecoveringPathParser().parse(expression);

        if (parserResult.getProblems().isEmpty()) {
            return new JsonPath(parserResult.getPathComponent());
        }

        throw new InvalidJsonPathExpressionException(getMessage(parserResult.getProblems()));
    }

    /**
     * A short-cut that encapsulates the {@link #compile(String) compilation} of a JSONPath expression and then the read
     * of data from a JSON payload. <b>Note</b> that this is simply an encapsulation of a call to
     * {@link #compile(String)} followed by a call to {@link #read(String, Class)}. There is no performance benefit to
     * calling this method and it has the downside of not allowing reuse of a compiled {@link JsonPath} expression.
     * 
     * @param expression The expression to use to read content
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * 
     * @return The content read from the JSON payload
     * 
     * @throws InvalidJsonException if the {@code json} argument is not a legal JSON string
     */
    public static <T> T read(String expression, String json, Class<T> expectedReturnType) {
        return read(expression, json, expectedReturnType, OBJECT_MAPPER);
    }

    /**
     * A short-cut that encapsulates the {@link #compile(String) compilation} of a JSONPath expression and then the read
     * of data from a JSON payload. <b>Note</b> that this is simply an encapsulation of a call to
     * {@link #compile(String)} followed by a call to {@link #read(String, TypeReference)}. There is no performance
     * benefit to calling this method and it has the downside of not allowing reuse of a compiled {@link JsonPath}
     * expression.
     * 
     * @param expression The expression to use to read content
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * 
     * @return The content read from the JSON payload
     * 
     * @throws InvalidJsonException if the {@code json} argument is not a legal JSON string
     */
    public static <T> T read(String expression, String json, TypeReference<?> expectedReturnType) {
        return read(expression, json, expectedReturnType, OBJECT_MAPPER);
    }

    /**
     * A short-cut that encapsulates the {@link #compile(String) compilation} of a JSONPath expression and then the read
     * of data from a JSON payload. <b>Note</b> that this is simply an encapsulation of a call to
     * {@link #compile(String)} followed by a call to {@link #read(String, JavaType)}. There is no performance benefit
     * to calling this method and it has the downside of not allowing reuse of a compiled {@link JsonPath} expression.
     * 
     * @param expression The expression to use to read content
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * 
     * @return The content read from the JSON payload
     * 
     * @throws InvalidJsonException if the {@code json} argument is not a legal JSON string
     */
    public static <T> T read(String expression, String json, JavaType expectedReturnType) {
        return read(expression, json, expectedReturnType, OBJECT_MAPPER);
    }

    /**
     * A short-cut that encapsulates the {@link #compile(String) compilation} of a JSONPath expression and then the read
     * of data from a JSON payload. <b>Note</b> that this is simply an encapsulation of a call to
     * {@link #compile(String)} followed by a call to {@link #read(String, Class)}. There is no performance benefit to
     * calling this method and it has the downside of not allowing reuse of a compiled {@link JsonPath} expression.
     * 
     * @param expression The expression to use to read content
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * @param objectMapper The objectMapper to use when converting the input and output values
     * 
     * @return The content read from the JSON payload
     * 
     * @throws InvalidJsonException if the {@code json} argument is not a legal JSON string
     */
    public static <T> T read(String expression, String json, Class<T> expectedReturnType, ObjectMapper objectMapper) {
        return compile(expression).read(json, expectedReturnType, objectMapper);
    }

    /**
     * A short-cut that encapsulates the {@link #compile(String) compilation} of a JSONPath expression and then the read
     * of data from a JSON payload. <b>Note</b> that this is simply an encapsulation of a call to
     * {@link #compile(String)} followed by a call to {@link #read(String, TypeReference)}. There is no performance
     * benefit to calling this method and it has the downside of not allowing reuse of a compiled {@link JsonPath}
     * expression.
     * 
     * @param expression The expression to use to read content
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * @param objectMapper The objectMapper to use when converting the input and output values
     * 
     * @return The content read from the JSON payload
     * 
     * @throws InvalidJsonException if the {@code json} argument is not a legal JSON string
     */
    public static <T> T read(String expression, String json, TypeReference<?> expectedReturnType, ObjectMapper objectMapper) {
        return compile(expression).read(json, expectedReturnType, objectMapper);
    }

    /**
     * A short-cut that encapsulates the {@link #compile(String) compilation} of a JSONPath expression and then the read
     * of data from a JSON payload. <b>Note</b> that this is simply an encapsulation of a call to
     * {@link #compile(String)} followed by a call to {@link #read(String, JavaType)}. There is no performance benefit
     * to calling this method and it has the downside of not allowing reuse of a compiled {@link JsonPath} expression.
     * 
     * @param expression The expression to use to read content
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * @param objectMapper The objectMapper to use when converting the input and output values
     * 
     * @return The content read from the JSON payload
     * 
     * @throws InvalidJsonException if the {@code json} argument is not a legal JSON string
     */
    public static <T> T read(String expression, String json, JavaType expectedReturnType, ObjectMapper objectMapper) {
        return compile(expression).read(json, expectedReturnType, objectMapper);
    }

    /**
     * A short-cut that encapsulates the {@link #compile(String) compilation} of a JSONPath expression and then the read
     * of data from a JSON payload. <b>Note</b> that this is simply an encapsulation of a call to
     * {@link #compile(String)} followed by a call to {@link #read(JsonNode, Class)}. There is no performance benefit to
     * calling this method and it has the downside of not allowing reuse of a compiled {@link JsonPath} expression.
     * 
     * @param expression The expression to use to read content
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * 
     * @return The content read from the JSON payload
     */
    public static <T> T read(String expression, JsonNode json, Class<T> expectedReturnType) {
        return read(expression, json, expectedReturnType, OBJECT_MAPPER);
    }

    /**
     * A short-cut that encapsulates the {@link #compile(String) compilation} of a JSONPath expression and then the read
     * of data from a JSON payload. <b>Note</b> that this is simply an encapsulation of a call to
     * {@link #compile(String)} followed by a call to {@link #read(JsonNode, TypeReference)}. There is no performance
     * benefit to calling this method and it has the downside of not allowing reuse of a compiled {@link JsonPath}
     * expression.
     * 
     * @param expression The expression to use to read content
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * 
     * @return The content read from the JSON payload
     */
    public static <T> T read(String expression, JsonNode json, TypeReference<?> expectedReturnType) {
        return read(expression, json, expectedReturnType, OBJECT_MAPPER);
    }

    /**
     * A short-cut that encapsulates the {@link #compile(String) compilation} of a JSONPath expression and then the read
     * of data from a JSON payload. <b>Note</b> that this is simply an encapsulation of a call to
     * {@link #compile(String)} followed by a call to {@link #read(JsonNode, JavaType)}. There is no performance benefit
     * to calling this method and it has the downside of not allowing reuse of a compiled {@link JsonPath} expression.
     * 
     * @param expression The expression to use to read content
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * 
     * @return The content read from the JSON payload
     */
    public static <T> T read(String expression, JsonNode json, JavaType expectedReturnType) {
        return read(expression, json, expectedReturnType, OBJECT_MAPPER);
    }

    /**
     * A short-cut that encapsulates the {@link #compile(String) compilation} of a JSONPath expression and then the read
     * of data from a JSON payload. <b>Note</b> that this is simply an encapsulation of a call to
     * {@link #compile(String)} followed by a call to {@link #read(JsonNode, Class)}. There is no performance benefit to
     * calling this method and it has the downside of not allowing reuse of a compiled {@link JsonPath} expression.
     * 
     * @param expression The expression to use to read content
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * @param objectMapper The objectMapper to use when converting the output value
     * 
     * @return The content read from the JSON payload
     */
    public static <T> T read(String expression, JsonNode json, Class<T> expectedReturnType, ObjectMapper objectMapper) {
        return compile(expression).read(json, expectedReturnType, objectMapper);
    }

    /**
     * A short-cut that encapsulates the {@link #compile(String) compilation} of a JSONPath expression and then the read
     * of data from a JSON payload. <b>Note</b> that this is simply an encapsulation of a call to
     * {@link #compile(String)} followed by a call to {@link #read(JsonNode, TypeReference)}. There is no performance
     * benefit to calling this method and it has the downside of not allowing reuse of a compiled {@link JsonPath}
     * expression.
     * 
     * @param expression The expression to use to read content
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * @param objectMapper The objectMapper to use when converting the output value
     * 
     * @return The content read from the JSON payload
     */
    public static <T> T read(String expression, JsonNode json, TypeReference<?> expectedReturnType, ObjectMapper objectMapper) {
        return compile(expression).read(json, expectedReturnType, objectMapper);
    }

    /**
     * A short-cut that encapsulates the {@link #compile(String) compilation} of a JSONPath expression and then the read
     * of data from a JSON payload. <b>Note</b> that this is simply an encapsulation of a call to
     * {@link #compile(String)} followed by a call to {@link #read(JsonNode, JavaType)}. There is no performance benefit
     * to calling this method and it has the downside of not allowing reuse of a compiled {@link JsonPath} expression.
     * 
     * @param expression The expression to use to read content
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * @param objectMapper The objectMapper to use when converting the output value
     * 
     * @return The content read from the JSON payload
     */
    public static <T> T read(String expression, JsonNode json, JavaType expectedReturnType, ObjectMapper objectMapper) {
        return compile(expression).read(json, expectedReturnType, objectMapper);
    }

    /**
     * Reads content from a JSON payload based on the expression compiled into this instance
     * 
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * 
     * @return The content read from the JSON payload
     * 
     * @throws InvalidJsonException if the {@code json} argument is not a legal JSON string
     */
    public <T> T read(String json, Class<T> expectedReturnType) {
        return read(json, expectedReturnType, OBJECT_MAPPER);
    }

    /**
     * Reads content from a JSON payload based on the expression compiled into this instance
     * 
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * 
     * @return The content read from the JSON payload
     * 
     * @throws InvalidJsonException if the {@code json} argument is not a legal JSON string
     */
    public <T> T read(String json, TypeReference<?> expectedReturnType) {
        return read(json, expectedReturnType, OBJECT_MAPPER);
    }

    /**
     * Reads content from a JSON payload based on the expression compiled into this instance
     * 
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * 
     * @return The content read from the JSON payload
     * 
     * @throws InvalidJsonException if the {@code json} argument is not a legal JSON string
     */
    public <T> T read(String json, JavaType expectedReturnType) {
        return read(json, expectedReturnType, OBJECT_MAPPER);
    }

    /**
     * Reads content from a JSON payload based on the expression compiled into this instance
     * 
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * @param objectMapper The objectMapper to use when converting the input and output values
     * 
     * @return The content read from the JSON payload
     * 
     * @throws InvalidJsonException if the {@code json} argument is not a legal JSON string
     */
    public <T> T read(String json, Class<T> expectedReturnType, ObjectMapper objectMapper) {
        try {
            JsonNode tree = objectMapper.readTree(json);
            return read(tree, expectedReturnType, objectMapper);
        } catch (IOException e) {
            throw new InvalidJsonException(e);
        }
    }

    /**
     * Reads content from a JSON payload based on the expression compiled into this instance
     * 
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * @param objectMapper The objectMapper to use when converting the input and output values
     * 
     * @return The content read from the JSON payload
     * 
     * @throws InvalidJsonException if the {@code json} argument is not a legal JSON string
     */
    public <T> T read(String json, TypeReference<?> expectedReturnType, ObjectMapper objectMapper) {
        try {
            JsonNode tree = objectMapper.readTree(json);
            return read(tree, expectedReturnType, objectMapper);
        } catch (IOException e) {
            throw new InvalidJsonException(e);
        }
    }

    /**
     * Reads content from a JSON payload based on the expression compiled into this instance
     * 
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * @param objectMapper The objectMapper to use when converting the input and output values
     * 
     * @return The content read from the JSON payload
     * 
     * @throws InvalidJsonException if the {@code json} argument is not a legal JSON string
     */
    public <T> T read(String json, JavaType expectedReturnType, ObjectMapper objectMapper) {
        try {
            JsonNode tree = objectMapper.readTree(json);
            return read(tree, expectedReturnType, objectMapper);
        } catch (IOException e) {
            throw new InvalidJsonException(e);
        }
    }

    /**
     * Reads content from a JSON payload based on the expression compiled into this instance
     * 
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * 
     * @return The content read from the JSON payload
     */
    public <T> T read(JsonNode json, Class<T> expectedReturnType) {
        return read(json, expectedReturnType, OBJECT_MAPPER);
    }

    /**
     * Reads content from a JSON payload based on the expression compiled into this instance
     * 
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * 
     * @return The content read from the JSON payload
     */
    public <T> T read(JsonNode json, TypeReference<?> expectedReturnType) {
        return read(json, expectedReturnType, OBJECT_MAPPER);
    }

    /**
     * Reads content from a JSON payload based on the expression compiled into this instance
     * 
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * 
     * @return The content read from the JSON payload
     */
    public <T> T read(JsonNode json, JavaType expectedReturnType) {
        return read(json, expectedReturnType, OBJECT_MAPPER);
    }

    /**
     * Reads content from a JSON payload based on the expression compiled into this instance
     * 
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * @param objectMapper The objectMapper to use when converting output value
     * 
     * @return The content read from the JSON payload
     */
    public <T> T read(JsonNode json, Class<T> expectedReturnType, ObjectMapper objectMapper) {
        JsonNode result = this.pathComponent.get(json);
        return objectMapper.convertValue(result, expectedReturnType);
    }

    /**
     * Reads content from a JSON payload based on the expression compiled into this instance
     * 
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * @param objectMapper The objectMapper to use when converting output value
     * 
     * @return The content read from the JSON payload
     */
    public <T> T read(JsonNode json, TypeReference<?> expectedReturnType, ObjectMapper objectMapper) {
        JsonNode result = this.pathComponent.get(json);
        return objectMapper.convertValue(result, expectedReturnType);
    }

    /**
     * Reads content from a JSON payload based on the expression compiled into this instance
     * 
     * @param json The JSON payload to retrieve data from
     * @param expectedReturnType The type that the return value is expected to be
     * @param objectMapper The objectMapper to use when converting output value
     * 
     * @return The content read from the JSON payload
     */
    public <T> T read(JsonNode json, JavaType expectedReturnType, ObjectMapper objectMapper) {
        JsonNode result = this.pathComponent.get(json);
        return objectMapper.convertValue(result, expectedReturnType);
    }

    private static String getMessage(List<ExpressionProblem> problems) {
        StringBuilder sb = new StringBuilder();
        for (ExpressionProblem expressionProblem : problems) {
            sb.append(expressionProblem.toString());
        }
        return sb.toString();
    }

}
