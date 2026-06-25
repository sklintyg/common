# JUnit 4 -> JUnit 5 Migration Plan

**Goal:** Eliminate the JUnit Vintage engine deprecation warning by migrating all
remaining JUnit 4 test files to JUnit 5 and removing the `junit:junit` and
`junit-vintage-engine` dependencies from `build.gradle`.

---

## Remaining Work

| Step | Description | Files | Status |
|---|---|---:|---|
| Step 0 | Trim over-applied @MockitoSettings(LENIENT) in Batch 2 | 58 | To do |
| Batch 1 | Plain JUnit 4, no @RunWith | ~79 | To do |
| Batch 4 | @RunWith(Parameterized) | 11 | To do |
| Batch 5 | Remove JUnit 4 build deps | - | To do |

---

## Lessons Learned / Approach Rules

### Do NOT use scripts for assertion argument order

JUnit 4 uses message-first: `assertEquals(String message, T expected, T actual)`
JUnit 5 uses message-last: `assertEquals(T expected, T actual, String message)`

A parser-based script to flip argument order worked correctly for a single run but
is dangerous: for 3-arg assertEquals each run *rotates* arguments (not toggles),
so even numbers of runs = wrong order but compiling code. This caused multiple
full rollbacks.

**Correct approach:**
1. Apply import replacements only (these are idempotent and safe to script).
2. Run `compileTestJava`. The compiler reports exactly which lines have
   type-mismatched message-first calls (e.g. "no suitable method found for
   assertEquals(String, int, int)").
3. Fix only the specific lines reported by the compiler - targeted manual edits.
4. For `assertEquals(String, String, String)` (compiles but wrong semantics):
   grep for the pattern after step 2 and fix manually.

### @MockitoSettings(LENIENT) - add only where needed

`MockitoExtension` uses `STRICT_STUBS` by default. `LENIENT` should only be added
to a test class when Mockito reports `UnnecessaryStubbingException` for that class
at runtime - meaning a stub was set up but not called by any test in the class.

**Do not** add LENIENT as a blanket measure. Run tests first; add only where
`UnnecessaryStubbingException` actually fires.

### Remove `public` from class and method declarations (convention)

JUnit 5 does not require `public`. By convention, migrate to package-private:

- `public class FooTest {` -> `class FooTest {`
- `public void testFoo()` -> `void testFoo()`
- `@BeforeEach public void setUp()` -> `@BeforeEach void setUp()`

This is a style change only and has no functional impact.

---

## Step 0: Trim @MockitoSettings(LENIENT) in Batch 2

**Problem:** LENIENT was added to 58 of 59 Batch 2 files without first verifying
which classes actually need it. The correct trigger is a runtime
`UnnecessaryStubbingException` - a stub was set up but not called by any test
in the class.

**Procedure:**
1. Remove `@MockitoSettings(strictness = Strictness.LENIENT)` and its import from
   all Batch 2 files that currently have it.
2. Run `.\gradlew.bat test --no-daemon -x spotlessCheck --continue`
3. For each `UnnecessaryStubbingException` failure: add LENIENT back to that class.
4. Repeat until BUILD SUCCESSFUL.
5. Commit.

---

## Batch 1: Plain JUnit 4, no @RunWith (~79 files)

These files use JUnit 4 directly with no runner annotation. Migration is mechanical.

### Import replacements (safe to script - idempotent)

| From | To |
|---|---|
| `import org.junit.Test;` | `import org.junit.jupiter.api.Test;` |
| `import org.junit.Before;` | `import org.junit.jupiter.api.BeforeEach;` |
| `import org.junit.After;` | `import org.junit.jupiter.api.AfterEach;` |
| `import org.junit.BeforeClass;` | `import org.junit.jupiter.api.BeforeAll;` |
| `import org.junit.AfterClass;` | `import org.junit.jupiter.api.AfterAll;` |
| `import static org.junit.Assert.*;` | `import static org.junit.jupiter.api.Assertions.*;` |
| `import org.junit.Assert;` (non-static) | `import static org.junit.jupiter.api.Assertions.*;` |
| `Assert.assertEquals(` etc. | `assertEquals(` (remove qualifier) |

### Annotation replacements

| From | To |
|---|---|
| `@Before` | `@BeforeEach` |
| `@After` | `@AfterEach` |
| `@BeforeClass` | `@BeforeAll` |
| `@AfterClass` | `@AfterAll` |

### Code example: plain no-runner file (before/after)

**Before:**
```java
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class FooTest {

    @Before
    public void setUp() { ... }

    @Test
    public void shouldDoSomething() {
        assertEquals("message", expected, actual);
    }
}
```

**After:**
```java
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FooTest {

    @BeforeEach
    void setUp() { ... }

    @Test
    void shouldDoSomething() {
        assertEquals(expected, actual, "message");
    }
}
```

### Assertion order (compiler-guided - do not script)

After applying the above, run `compileTestJava`. Fix only the reported errors.
Common patterns:
- `"no suitable method found for assertEquals(String, int, int)"` ->
  `assertEquals("msg", 1, 2)` -> `assertEquals(1, 2, "msg")`
- `"no suitable method found for assertTrue(String, boolean)"` ->
  `assertTrue("msg", x)` -> `assertTrue(x, "msg")`
- `"no suitable method found for assertNull(String, Object)"` ->
  `assertNull("msg", x)` -> `assertNull(x, "msg")`

**Verify:** `.\gradlew.bat test --no-daemon -x spotlessCheck`

---

## Batch 4: Parameterized tests (11 files)

All 11 files are named `InternalValidatorResultMatchesSchematronValidatorTest.java`,
one per module. They all follow the same structure.

### Migration pattern

**JUnit 4 structure:**
```java
@RunWith(Parameterized.class)
public class InternalValidatorResultMatchesSchematronValidatorTest {
    private Scenario scenario;
    private boolean shouldFail;
    private static String name;           // or private String name

    public InternalValidatorResultMatchesSchematronValidatorTest(
            String name, Scenario scenario, boolean shouldFail) {
        this.scenario = scenario;
        this.shouldFail = shouldFail;
        InternalValidatorResultMatchesSchematronValidatorTest.name = name;
    }

    @Parameters(name = "{index}: Scenario: {0}")
    public static Collection<Object[]> data() throws ScenarioNotFoundException {
        // returns List<Object[]> where each entry is {name, scenario, shouldFail}
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        // field setup
    }

    @Test
    public void testScenarios() throws Exception {
        doInternalAndSchematronValidation(scenario, shouldFail);
    }
}
```

**JUnit 5 structure:**
```java
// No @RunWith/@ExtendWith needed unless Mockito mocks are used
// (add @ExtendWith(MockitoExtension.class) only if @Mock/@InjectMocks present)
class InternalValidatorResultMatchesSchematronValidatorTest {

    static Stream<Arguments> data() throws ScenarioNotFoundException {
        // same scenario loading as before, but return Stream<Arguments>
        List<Arguments> args = new ArrayList<>();
        ScenarioFinder.getInternalScenarios("fail-*").stream()
            .map(u -> Arguments.of(u.getName(), u, true))
            .forEach(args::add);
        ScenarioFinder.getInternalScenarios("pass-*").stream()
            .map(u -> Arguments.of(u.getName(), u, false))
            .forEach(args::add);
        return args.stream();
    }

    @BeforeEach
    void setUp() throws Exception {
        // MockitoExtension handles injection; keep any other setup
    }

    @ParameterizedTest(name = "{index}: Scenario: {0}")
    @MethodSource("data")
    void testScenarios(String name, Scenario scenario, boolean shouldFail) throws Exception {
        doInternalAndSchematronValidation(name, scenario, shouldFail);
    }

    // Pass name explicitly into doInternalAndSchematronValidation
    // (no more static 'name' field)
}
```

### Import changes for Batch 4

| Remove | Add |
|---|---|
| `import org.junit.runner.RunWith;` | `import org.junit.jupiter.params.ParameterizedTest;` |
| `import org.junit.runners.Parameterized;` | `import org.junit.jupiter.params.provider.MethodSource;` |
| `import org.junit.runners.Parameterized.Parameters;` | `import org.junit.jupiter.params.provider.Arguments;` |
| `import org.junit.Before;` | `import org.junit.jupiter.api.BeforeEach;` |
| `import org.junit.Test;` | `import java.util.stream.Stream;` |
| `import static org.junit.Assert.*;` | `import static org.junit.jupiter.api.Assertions.*;` |
| `@RunWith(Parameterized.class)` annotation | (removed) |

### Key differences between the 11 files

Some files have `private static @Mock` / `private static @InjectMocks` fields -
these must be changed to instance fields (remove `static`). Files without Mockito
annotations do not need `@ExtendWith(MockitoExtension.class)`.

**Verify:** `.\gradlew.bat test --no-daemon -x spotlessCheck`

---

## Batch 5: Remove JUnit 4 build dependencies

In `build.gradle` (root), remove from the `allprojects { dependencies { ... } }` block:

```groovy
// Remove these two lines:
testImplementation "junit:junit"
testRuntimeOnly "org.junit.vintage:junit-vintage-engine"
```

**Verify:** `.\gradlew.bat clean test --no-daemon`

Expected outcome: no line matching
`The JUnit Vintage engine is deprecated` in the test output.

### Final verification checklist (confirm zero JUnit 4 remains)

```powershell
# Should return nothing (no JUnit 4 imports left):
Get-ChildItem -Recurse -Filter "*.java" | Select-String "import org.junit\." | Select-String -NotMatch "jupiter"
Get-ChildItem -Recurse -Filter "*.java" | Select-String "junit\.framework"
Get-ChildItem -Recurse -Filter "*.java" | Select-String "import org\.junit\.runner"

# Should return nothing (no vintage engine dep):
Get-ChildItem -Recurse -Filter "*.gradle" | Select-String "junit-vintage-engine"
Get-ChildItem -Recurse -Filter "*.gradle" | Select-String '"junit:junit"'
```

---

## Risk Notes

| Risk | Mitigation |
|---|---|
| `assertEquals(message, expected, actual)` - JUnit 5 moves message to last param | Use compiler output to find mismatches; never script argument order fixes |
| 3-arg assertEquals script run multiple times causes rotation (not toggle) | Only script imports (idempotent); assertion fixes are manual only |
| Mockito strict stubs - `MockitoExtension` uses `STRICT_STUBS` by default | Run tests first; add `@MockitoSettings(LENIENT)` only where `UnnecessaryStubbingException` fires |
| CRLF line endings - Node.js `\n` regex fails on CRLF files | Normalize with `raw.replace(/\r\n/g, '\n')` before regex processing |
| `import org.junit.Assert` (non-static) - removing import leaves qualified calls unresolved | Replace with `import static org.junit.jupiter.api.Assertions.*` AND remove `Assert.` qualifier |
| Parameterized: static `@Mock`/`@InjectMocks` fields | MockitoExtension does not support static fields; remove `static` keyword |