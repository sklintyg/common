# Lifecycle Migration Progress – intyg/common

## Status Overview

| Phase | Description                          | Status     |
|-------|--------------------------------------|------------|
| 1     | Gradle 8 → 9 + plugin version bumps  | ✅ Done    |
| 2     | Java 21 → 25                         | ✅ Done    |
| 3+4   | Spring Boot 4 BOM + Jackson 3        | ✅ Done    |
| 5     | Bump remaining dependencies          | ✅ Done    |
| 6     | Spring Boot autoconfig review        | ✅ Done    |

---

## Phase 1 – Gradle 8 → 9 ✅

**Completed manually.**
- Gradle wrapper updated to Gradle 9.x.
- Plugin versions bumped to latest Gradle-9-compatible releases.
- Build verified green.

---

## Phase 2 – Java 21 → 25 ✅

**Completed.**

### Changes Made
- `build.gradle` (root): toolchain `languageVersion` changed from 21 → 25.
- `gradle.properties`: added `org.gradle.java.installations.paths=~/.jdks/temurin-25.0.3`.

### Notes
- Build: `BUILD SUCCESSFUL` — 225 tasks, all green.
- `common-pdf` (Nashorn): compiled and all tests passed without changes.
- Build warnings deferred to Phase 5 (general version bump).

---

## Phase 3+4 – Spring Boot 4 BOM + Jackson 3 ✅

**Status:** Complete — compilation clean, tests pass (one known external dependency issue, see below).

### Change category summary (319 files total)

| Category | File count |
|---|---:|
| Jackson import/package renames only | 230 |
| Exception handling: `catch (IOException)` → `catch (JacksonException)` | 40 |
| Type/API renames (`JsonSerializer`→`ValueSerializer`, etc.) | 25 |
| Method renames (`getCurrentToken`→`currentToken`, etc.) | 7 |
| Build / config files (`*.gradle`) | 17 |

### Changes Made

**Build configuration**
- `build.gradle` (root): `springbootMavenBomVersion` bumped to `4.1.0`.
- All module `build.gradle` files: `com.fasterxml.jackson.core:jackson-core/jackson-databind` → `tools.jackson.core:jackson-core/jackson-databind`.
- `jackson-annotations` stays at `com.fasterxml.jackson.core:jackson-annotations` (version 2.21) — package name unchanged in Jackson 3.
- Removed `jackson-datatype-jsr310` from all build files — Java time support built into `jackson-databind` in Jackson 3.

**Bulk Java source changes (mechanical, all verified equivalent)**
- 345 Java files: bulk import rename `import com.fasterxml.jackson.` → `import tools.jackson.`
- 124 files: reverted annotation imports back to `com.fasterxml.jackson.annotation.*`
- `JsonSerializer<T>` → `ValueSerializer<T>`, `JsonDeserializer<T>` → `ValueDeserializer<T>`
- `SerializerProvider` → `SerializationContext`
- `JsonProcessingException` / `JsonMappingException` → `JacksonException` / `DatabindException`
- `getCurrentToken()` → `currentToken()`, `getCurrentName()` → `currentName()`
- `writeStringField()` → `writeStringProperty()`, `writeBooleanField()` → `writeBooleanProperty()`
- Removed `throws IOException` from all serializer/deserializer `serialize()`/`deserialize()` overrides (Jackson 3 exceptions are unchecked)
- `JsonFactory` → `tools.jackson.core.json.JsonFactory`, `JsonParseException` → `StreamReadException`

**Notable production file changes**
- `CustomObjectMapper`: refactored from `extends ObjectMapper` (post-construction mutation) → `extends JsonMapper` (builder pattern). Same effective settings applied: NON_NULL inclusion, dates not as timestamps, unknown properties ignored, custom module registered.
- `MessagesParser`: `new ObjectMapper() + JsonParser.Feature` → `JsonMapper.builder().enable(JsonReadFeature.ALLOW_JAVA_COMMENTS/ALLOW_SINGLE_QUOTES)`. Equivalent JSON parsing behavior.
- `CustomLocalDateTimeDeserializer`: `getText().trim()` → `getString().trim()` (Jackson 3 rename), `isEmpty()` cleanup, `DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS` → `DateTimeFeature.*`.
- `UnitMappingConfigLoader`: removed explicit `registerModule(new JavaTimeModule())` — auto-registered in Jackson 3.
- All parent ModuleApi classes (Af, Ag, Fk, Sos, Fk7263): `catch (IOException e)` → `catch (JacksonException e)` around Jackson calls.

**Test file changes**
- `assertThrows(IOException.class, ...)` → `assertThrows(JacksonException.class, ...)` in `Ag114ModuleApiTest` and `TsParentModuleApiTest` — **correct**, tests the actual type now thrown.
- `CustomObjectMapperTest`: now tests the real `CustomObjectMapper` instead of an inline-configured `ObjectMapper` — improved test fidelity.
- `readValue(Resources.getResource(...), Class)` → `readValue(Resources.getResource(...).openStream(), Class)` in `Ag114ModuleApiTest` — `readValue(URL, Class)` removed in Jackson 3.
- Removed `JavaTimeModule` import/registration from test helpers where applicable.

### Exception contract change (intentional, downstream impact)

Methods like `getUtlatandeFromJson(String)` in all parent ModuleApi classes now throw
unchecked **`JacksonException`** instead of checked **`IOException`** on invalid JSON input.

- In Jackson 2: `JsonProcessingException extends IOException` (checked).
- In Jackson 3: `JacksonException extends RuntimeException` (unchecked).

This is an **expected, intentional API contract change** for Jackson 3. Downstream
callers in other applications that catch `IOException` to handle malformed JSON will
need to update their catch blocks when they migrate to Spring Boot 4 / Jackson 3.

### Problems Encountered

**1. `jackson-datatype-jsr310` doesn't exist in Jackson 3 Maven repository**
- Listed in the Jackson 3 BOM but not published on Maven Central.
- **Solution:** Removed entirely. Java time support is built into `jackson-databind`.

**2. `jackson-annotations` package unchanged in Jackson 3**
- Only `jackson-core` and `jackson-databind` moved to `tools.jackson.*`.
- `jackson-annotations` stays at `com.fasterxml.jackson.core:jackson-annotations:2.21`.
- **Solution:** Reverted annotation imports back to `com.fasterxml.jackson.annotation.*`.

**3. `IOException` no longer thrown by Jackson 3 methods**
- All Jackson `ObjectMapper` methods now throw unchecked `JacksonException`.
- Dead `catch (IOException)` blocks cause compile errors in Java 25.
- **Solution:** Changed all affected `catch (IOException)` to `catch (JacksonException)`.

**4. `gradle.properties` machine-specific JDK path (corrected)**
- During Java 25 setup, `org.gradle.java.installations.paths=C:/Users/mheglind/.jdks/temurin-25.0.3` was added to the project-level `gradle.properties`.
- This is machine-specific and must not be committed.
- **Solution:** Removed from project `gradle.properties`. Must be set in `~/.gradle/gradle.properties` on each developer machine.

---

## Phase 5 – Bump Remaining Dependencies

**Status:** ✅ Done — build successful, all tests pass.

### Changes Made

All version bumps are in `build.gradle` (root). Only library versions were updated;
no Gradle plugin versions required changes in this phase (plugins were already
at current compatible versions).

#### Dependencies bumped to latest

| Dependency | From | To |
|---|---|---|
| Apache CXF (`cxfVersion`) | 4.1.4 | 4.2.2 |
| commons-io (`commonsIoVersion`) | 2.21.0 | 2.22.0 |
| EqualsVerifier (`equalsVerifierVersion`) | 4.3.1 | 4.5 |
| Guava (`guavaVersion`) | 33.5.0-jre | 33.6.0-jre |
| iText 5 (`itextVersion`) | 5.5.13.4 | 5.5.13.5 |
| ph-schematron (`phSchematronVersion`) | 9.1.1 | 9.2.0 |
| Saxon-HE (`saxonVersion`) | 12.9 | 13.0 |
| SpotBugs annotations (`spotbugsAnnotationVersion`) | 4.9.8 | 4.10.2 |

#### Dependencies intentionally kept at current version

| Dependency | Version | Reason |
|---|---|---|
| iText 7 core (`itext7Version`) | 7.2.6 | Next available update is a new major version requiring code changes. iText PDF generation is being migrated to a separate service using PDFBox (Apache-licensed). Current version has no known vulnerabilities (verified via Sonatype). |
| iText pdfHTML (`itextPdfHtmlVersion`) | 5.0.5 | Same rationale as iText 7 above. |

#### Dependencies with no update available

| Dependency | Version | Note |
|---|---|---|
| AutoValue (`autovalueVersion`) | 1.11.1 | Already at latest |
| commons-collections (`commonsCollectionsVersion`) | 4.5.0 | Already at latest |
| commons-text (`commonsTextVersion`) | 1.15.0 | Already at latest |
| commons-csv (`commonsCsvVersion`) | 1.14.1 | Already at latest |
| Google Java Format (`googleJavaFormatVersion`) | 1.35.0 | Already at latest |
| Jakarta JWS API (`jakartaJwsApiVersion`) | 3.0.0 | Already at latest |
| JAXB2 (`jaxb2Version`) | 3.0.0 | Already at latest |
| Nashorn (`nashornVersion`) | 15.7 | Already at latest |
| Spring Boot BOM (`springbootMavenBomVersion`) | 4.1.0 | Updated in Phase 3+4 |
| Jackson BOM | Managed by Spring Boot BOM | — |
| Schema versions (intyg/rivta) | Various | Managed separately |

### Problems Encountered

_None — build green, all tests pass._

---

## Phase 6 – Spring Boot Autoconfig Review

**Status:** ✅ Done — analysis complete, one improvement applied.

### Analysis

**Production Spring dependencies — no change needed.**
The library uses individual Spring modules (`spring-core`, `spring-beans`,
`spring-context`, `spring-oxm`, `spring-tx`). This is the **correct pattern for a
library**: Spring Boot starters are designed for applications. They bring in
auto-configuration and heavy transitive deps that would leak into consuming
applications. Versions are already managed by the Spring Boot BOM.

**Auto-configuration migration — out of scope.**
Migrating `@Configuration` classes to Spring Boot auto-configuration registration
is not planned. The certificate module beans require explicit registration by
consuming applications.

### Change Made

**Consolidated test dependencies onto `spring-boot-starter-test`** in root
`build.gradle`. Replaced 10 individual declarations with a single starter.

The following were removed (now covered by the starter):
- `org.assertj:assertj-core`
- `org.junit.jupiter:junit-jupiter-api`
- `org.junit.jupiter:junit-jupiter-params`
- `org.mockito:mockito-core`
- `org.mockito:mockito-junit-jupiter`
- `org.skyscreamer:jsonassert`
- `org.springframework:spring-test`
- `org.junit.jupiter:junit-jupiter-engine` (testRuntimeOnly)

Kept explicitly (not in starter or requiring explicit declaration):
- `junit:junit` + `junit-vintage-engine` — Spring Boot 4 removed vintage engine
  from `spring-boot-starter-test`; needed for the 191 remaining JUnit 4 test files
- `com.sun.xml.messaging.saaj:saaj-impl` — not in starter
- `org.glassfish.jaxb:jaxb-runtime` — not in starter
- `org.xmlunit:xmlunit-core` — not in starter

Also removed redundant `assertj-core` redeclarations from `fk/lisjp/build.gradle`
and `support/build.gradle` (already covered by root allprojects block).

### Deferred improvements

| Item | Rationale |
|---|---|
| JUnit 4 → JUnit 5 migration (191 files) | Moderate effort; deferred to separate task |
| Gradle configuration cache | Low effort; pending team decision |
| `-Xlint:deprecation` compiler flag | Low effort; pending team decision |

---

## Notes & Decisions Log

| Date | Note |
|------|------|
| 2026-06-24 | Plan created. Gradle 9 + plugin bumps already done manually. Work starts at Phase 2. |
| 2026-06-25 | Phase 3+4 complete. 319 files changed. All changes verified behaviorally equivalent except intentional exception contract change (IOException→JacksonException). |
| 2026-06-25 | Phase 5 complete. 8 deps bumped to latest. iText 7 / pdfHTML held at current versions intentionally (no vulnerabilities; moving to PDFBox in separate service). |
| 2026-06-25 | Phase 6 complete. Spring starters analysis: no changes to production deps (library pattern correct). Test deps consolidated onto spring-boot-starter-test. |
