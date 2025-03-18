# Test Standards and Guidelines

## Package Organization

- Test packages should mirror the main source structure
    - Example: `main/java/com.asim.books.author.controller` → `test/java/com.asim.books.author.controller`
- If needed, sub-packages can be created.
    - Example: `...controller.integration` `...controller.unit` `...controller.util`
- Any common test java utilities should be placed in the `util` package within `test/java`

## Naming

- Unit tests classes: `{Name}Test.java`
- Integration tests classes: `{Name}IntegrationTest.java`
  **If a class has multiple test classes a prefix should indicate what part is tested.**
- Test methods: `when{Case}_then{Result}`
- Display names-method level: `should {ExpectedBehavior} when {Scenario}`
- Display names-class level: `{DescriptiveShortName} (Integration) Tests`

## General Guidelines

- Avoid extensive integration tests when unit tests are possible
    - Example: if a controller method uses `@Validated`, one validation case should be sufficient to test the
      integration, all other validation cases should be tested in the model itself.
- Avoid, where possible, asserting the non-functional and easily changeable parts, target the crucial and case-relevant
  parts of the object.
    - Example: checking an error message to verify the error is thrown, while assertThrows is sufficient.
- Avoid repeating the exact assertions in multiple tests.
    - Example: if a method asserts all fields of an object, do not repeat this in a test for specific field.
- Modularize test setups where possible for reusability.
    - Examples: `@BeforeAll`, `@BeforeEach`, `@ParameterizedTest`, utilities.
- Avoid test interdependencies.
- If test class is too long and tests are possible to categorize , split it into multiple classes.
- Follow the AAA pattern (Arrange, Act, Assert) with comments indicating each section where possible
- Use appropriate assertions from JUnit or Hamcrest based on the readability within the context.