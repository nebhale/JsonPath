JsonPath is a Java implementation of the JSONPath specification.  Details about the specification can be found at <http://goessner.net/articles/JsonPath/>. This implementation is designed to mimic the [Java `Pattern` class][pattern] and it's lifecycle.

[pattern]: http://download.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html

A JsonPath expression, specified as a string, must first be compiled into an instance of `JsonPath`. The resulting instance can then be used to read content from a JSON string. All of the smarts involved in the content reading reside in the `JsonPath` instance, so content can be read from multiple JSON strings using the same `JsonPath` instance. Since a `JsonPath` instance is thread-safe once compiled, multiple threads can use the same shared instance.

A typical invocation is thus:

```java
JsonPath namePath = JsonPath.compile("$.name");
List<String> names = namePath.read("[ { \"name\" : \"foo\" }, { \"name\" : \"bar\" } ]", List.class);
```

A `JsonPath.read(String expression, String json, Class<T> expectedReturnType)` method is defined by this class as a convenience for when the JSONPath is used just once. This method compiles an expressions and reads content from a JSON string in a single invocation. The statement:

```java
List<String> names = JsonPath.read("$.name", "[ { \"name\" : \"foo\" }, { \"name\" : \"bar\" } ]", List.class)
```

is equivalent to the two statements above, though for repeated reads it is less efficient since it does not allow the compiled JSONPath to be reused.

Instances of the `JsonPath` class are immutable and are safe for use by multiple concurrent threads.

# JSONPath Expressions #

_The following content is adapted from <http://goessner.net/articles/JsonPath/>_

JSONPath expressions always refer to a JSON structure in the same way as XPath expression are used in combination with an XML document. Since a JSON structure is usually anonymous and doesn't necessarily have a "root member object" JSONPath assumes the abstract name `$` assigned to the outer level object.

JSONPath expressions can use the dot-notation:

```javascript
$.store.book[0].title
```

or the bracket–notation:

```javascript
$['store']['book'][0]['title']
```

for input paths. Internal or output paths will always be converted to the more general bracket–notation.

Expressions based on the underlying [Jackson `JsonNode`][jsonNode] can be used as an alternative to explicit names or indicies as in:

[jsonNode]: http://jackson.codehaus.org/1.9.0/javadoc/org/codehaus/jackson/JsonNode.html

```javascript
$.store.book[(@.size() - 1)].title
```

using the symbol `@` for the current object. Filter expressions are supported via the syntax `?(<expression>)` as in

```javascript
$.store.book[?(@.price < 10)].title
```

Here is a complete overview of the JSONPath syntax:

| Operator                  | Description                                                        |
| :------------------------ | :----------------------------------------------------------------- |
| `$`                       | Root node. Required as first character of path.                    |
| `@`                       | Current node. Available in filters and expressions.                |
| `*`                       | Wildcard. Available anywhere a name or numeric are required.       |
| `..`                      | Deep wildcard. Available anywhere a name is required.              |
| `.<name>`                 | Dot-notated child                                                  |
| `['<name> (, <name>)']`   | Bracket-notated child or children                                  |
| `[<number> (, <number>)]` | Indexed child or children                                          |
| `[?(<expression>)]`       | Filter expression. Expression must evaluate to a boolean value.    |
| `[(<expression>)]`        | Expression. Expression must evaluate to a string or numeric value. |

# JSONPath Examples #

```javascript
{
	"store": {
		"book": [ {
			"category": "reference",
			"author": "Nigel Rees",
			"title": "Sayings of the Century",
			"price": 8.95
		}, {
			"category": "fiction",
			"author": "Evelyn Waugh",
			"title": "Sword of Honour",
			"price": 12.99
		}, {
			"category": "fiction",
			"author": "Herman Melville",
			"title": "Moby Dick",
			"isbn": "0-553-21311-3",
			"price": 8.99
		}, {
			"category": "fiction",
			"author": "J. R. R. Tolkien",
			"title": "The Lord of the Rings",
			"isbn": "0-395-19395-8",
			"price": 22.99
		} ],
		"bicycle": [ {
			"color": "red",
			"price": 19.95,
			"style": [ "city", "hybrid" ]
		}, {
			"color": "blue",
			"price": 59.91,
			"style": [ "downhill", "freeride" ]
		} ]
	}
}
```

| JSONPath | Result |
| :------- | :----- |
| `$.store.book[*].author` | The authors of all books            |
| `$..author`              | All authors                         |
| `$.store.*`              | All things, both books and bicycles |
| `$.store..price`         | The price of everything             |
| `$..book[2]`             | The third book                      |
| `$..book[(@.length-1)]`  | The last book                       |
| `$..book[0,1]`           | The first two books                 |
| `$..book[?(@.isbn)]`     | All books with an ISBN number       |
| `$..book[?(@.price<10)]` | All books cheaper than 10           |
| `$..*`                   | All JSON nodes                      |