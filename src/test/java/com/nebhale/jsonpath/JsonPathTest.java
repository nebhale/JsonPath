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

import static com.nebhale.jsonpath.testutils.JsonUtils.NODE;
import static com.nebhale.jsonpath.testutils.JsonUtils.STRING_INVALID;
import static com.nebhale.jsonpath.testutils.JsonUtils.STRING_VALID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.nebhale.jsonpath.internal.util.Sets;

public final class JsonPathTest {

    @Test
    public void compileValid() {
        assertNotNull(JsonPath.compile("$"));
    }

    @Test(expected = InvalidJsonPathExpressionException.class)
    public void compileInvalid() {
        JsonPath.compile(".");
    }

    @Test
    public void stringInputClassOutputStatic() {
        assertNotNull(JsonPath.read("$", STRING_VALID, Map.class));
    }

    @Test
    public void stringInputTypeReferenceOutputStatic() {
        assertNotNull(JsonPath.read("$", STRING_VALID, new TypeReference<JsonNode>() {
        }));
    }

    @Test
    public void stringInputJavaTypeOutputStatic() {
        assertNotNull(JsonPath.read("$", STRING_VALID, SimpleType.construct(Object.class)));
    }

    @Test
    public void jsonNodeInputClassOutputStatic() {
        assertNotNull(JsonPath.read("$", NODE, Map.class));
    }

    @Test
    public void jsonNodeInputTypeReferenceOutputStatic() {
        assertNotNull(JsonPath.read("$", NODE, new TypeReference<JsonNode>() {
        }));
    }

    @Test
    public void jsonNodeInputJavaTypeOutputStatic() {
        assertNotNull(JsonPath.read("$", NODE, SimpleType.construct(Object.class)));
    }

    @Test
    public void readValid() {
        assertNotNull(JsonPath.compile("$").read(STRING_VALID, Map.class));
    }

    @Test(expected = InvalidJsonException.class)
    public void readStringInputClassOutputInvalid() {
        JsonPath.compile("$").read(STRING_INVALID, Map.class);
    }

    @Test(expected = InvalidJsonException.class)
    public void readStringInputTypeReferenceOutputInvalid() {
        JsonPath.compile("$").read(STRING_INVALID, new TypeReference<JsonNode>() {
        });
    }

    @Test(expected = InvalidJsonException.class)
    public void readStringInputJavaTypeOutputInvalid() {
        JsonPath.compile("$").read(STRING_INVALID, SimpleType.construct(Object.class));
    }

    @Test
    public void stringInputClassOutput() {
        assertEquals("Sayings of the Century", JsonPath.read("$.store.book[0].title", STRING_VALID, String.class));
        assertEquals(Sets.asSet("Sayings of the Century", "Sword of Honour"), JsonPath.read("$.store.book[0,1].title", STRING_VALID, Set.class));
        assertEquals("Sayings of the Century", JsonPath.read("$['store']['book'][0]['title']", STRING_VALID, String.class));
        assertEquals(Arrays.asList("red", "blue"), JsonPath.read("$.store.bicycle.color", STRING_VALID, List.class));
        assertEquals(Arrays.asList("city", "hybrid", "downhill", "freeride"), JsonPath.read("$.store.bicycle.style", STRING_VALID, List.class));
        assertEquals(Arrays.asList("city", "hybrid", "downhill", "freeride"), JsonPath.read("$.store..style", STRING_VALID, List.class));
    }

    @Test
    public void stringInputTypeReferenceOutput() {
        assertEquals(new TextNode("Sayings of the Century"), JsonPath.read("$.store.book[0].title", STRING_VALID, new TypeReference<JsonNode>() {
        }));
        assertEquals(Sets.asSet(new TextNode("Sayings of the Century"), new TextNode("Sword of Honour")),
            JsonPath.read("$.store.book[0,1].title", STRING_VALID, new TypeReference<Set<JsonNode>>() {
            }));
        assertEquals(new TextNode("Sayings of the Century"),
            JsonPath.read("$['store']['book'][0]['title']", STRING_VALID, new TypeReference<JsonNode>() {
            }));
        assertEquals(Arrays.asList(new TextNode("red"), new TextNode("blue")),
            JsonPath.read("$.store.bicycle.color", STRING_VALID, new TypeReference<List<JsonNode>>() {
            }));
        assertEquals(Arrays.asList(new TextNode("city"), new TextNode("hybrid"), new TextNode("downhill"), new TextNode("freeride")),
            JsonPath.read("$.store.bicycle.style", STRING_VALID, new TypeReference<List<JsonNode>>() {
            }));
        assertEquals(Arrays.asList(new TextNode("city"), new TextNode("hybrid"), new TextNode("downhill"), new TextNode("freeride")),
            JsonPath.read("$.store..style", STRING_VALID, new TypeReference<List<JsonNode>>() {
            }));
    }

    @Test
    public void stringInputJavaTypeOutput() {
        assertEquals("Sayings of the Century", JsonPath.read("$.store.book[0].title", STRING_VALID, SimpleType.construct(String.class)));
        assertEquals(Sets.asSet("Sayings of the Century", "Sword of Honour"),
            JsonPath.read("$.store.book[0,1].title", STRING_VALID, CollectionType.construct(Set.class, SimpleType.construct(String.class))));
        assertEquals("Sayings of the Century", JsonPath.read("$['store']['book'][0]['title']", STRING_VALID, SimpleType.construct(String.class)));
        assertEquals(Arrays.asList("red", "blue"),
            JsonPath.read("$.store.bicycle.color", STRING_VALID, CollectionType.construct(List.class, SimpleType.construct(String.class))));
        assertEquals(Arrays.asList("city", "hybrid", "downhill", "freeride"),
            JsonPath.read("$.store.bicycle.style", STRING_VALID, CollectionType.construct(List.class, SimpleType.construct(String.class))));
        assertEquals(Arrays.asList("city", "hybrid", "downhill", "freeride"),
            JsonPath.read("$.store..style", STRING_VALID, CollectionType.construct(List.class, SimpleType.construct(String.class))));
    }

    @Test
    public void jsonNodeInputClassOutput() {
        assertEquals("Sayings of the Century", JsonPath.read("$.store.book[0].title", NODE, String.class));
        assertEquals(Sets.asSet("Sayings of the Century", "Sword of Honour"), JsonPath.read("$.store.book[0,1].title", NODE, Set.class));
        assertEquals("Sayings of the Century", JsonPath.read("$['store']['book'][0]['title']", NODE, String.class));
        assertEquals(Arrays.asList("red", "blue"), JsonPath.read("$.store.bicycle.color", NODE, List.class));
        assertEquals(Arrays.asList("city", "hybrid", "downhill", "freeride"), JsonPath.read("$.store.bicycle.style", NODE, List.class));
        assertEquals(Arrays.asList("city", "hybrid", "downhill", "freeride"), JsonPath.read("$.store..style", NODE, List.class));
    }

    @Test
    public void jsonNodeInputTypeReferenceOutput() {
        assertEquals(new TextNode("Sayings of the Century"), JsonPath.read("$.store.book[0].title", NODE, new TypeReference<JsonNode>() {
        }));
        assertEquals(Sets.asSet(new TextNode("Sayings of the Century"), new TextNode("Sword of Honour")),
            JsonPath.read("$.store.book[0,1].title", NODE, new TypeReference<Set<JsonNode>>() {
            }));
        assertEquals(new TextNode("Sayings of the Century"), JsonPath.read("$['store']['book'][0]['title']", NODE, new TypeReference<JsonNode>() {
        }));
        assertEquals(Arrays.asList(new TextNode("red"), new TextNode("blue")),
            JsonPath.read("$.store.bicycle.color", NODE, new TypeReference<List<JsonNode>>() {
            }));
        assertEquals(Arrays.asList(new TextNode("city"), new TextNode("hybrid"), new TextNode("downhill"), new TextNode("freeride")),
            JsonPath.read("$.store.bicycle.style", NODE, new TypeReference<List<JsonNode>>() {
            }));
        assertEquals(Arrays.asList(new TextNode("city"), new TextNode("hybrid"), new TextNode("downhill"), new TextNode("freeride")),
            JsonPath.read("$.store..style", NODE, new TypeReference<List<JsonNode>>() {
            }));
    }

    @Test
    public void jsonNodeInputJavaTypeOutput() {
        assertEquals("Sayings of the Century", JsonPath.read("$.store.book[0].title", NODE, SimpleType.construct(String.class)));
        assertEquals(Sets.asSet("Sayings of the Century", "Sword of Honour"),
            JsonPath.read("$.store.book[0,1].title", NODE, CollectionType.construct(Set.class, SimpleType.construct(String.class))));
        assertEquals("Sayings of the Century", JsonPath.read("$['store']['book'][0]['title']", NODE, SimpleType.construct(String.class)));
        assertEquals(Arrays.asList("red", "blue"),
            JsonPath.read("$.store.bicycle.color", NODE, CollectionType.construct(List.class, SimpleType.construct(String.class))));
        assertEquals(Arrays.asList("city", "hybrid", "downhill", "freeride"),
            JsonPath.read("$.store.bicycle.style", NODE, CollectionType.construct(List.class, SimpleType.construct(String.class))));
        assertEquals(Arrays.asList("city", "hybrid", "downhill", "freeride"),
            JsonPath.read("$.store..style", NODE, CollectionType.construct(List.class, SimpleType.construct(String.class))));
    }

    @Test
    public void serializable() throws IOException, ClassNotFoundException {
        assertTrue(Serializable.class.isAssignableFrom(JsonPath.class));

        JsonPath jsonPath = JsonPath.compile("$.store.book[0].title");
        assertEquals("Sayings of the Century", jsonPath.read(STRING_VALID, String.class));

        byte[] serialized;
        ObjectOutputStream out = null;
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bytes);

            out.writeObject(jsonPath);
            out.flush();

            serialized = bytes.toByteArray();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

        JsonPath newJsonPath;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new ByteArrayInputStream(serialized));

            newJsonPath = (JsonPath) in.readObject();
        } finally {
            if (in != null) {
                in.close();
            }
        }

        assertEquals("Sayings of the Century", newJsonPath.read(STRING_VALID, String.class));
    }

    @Test
    public void testToString() {
        assertEquals("JsonPath [expression=$.store.book[0].title]", JsonPath.compile("$.store.book[0].title").toString());
    }
}
