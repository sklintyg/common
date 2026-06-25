# Lifecycle Migration Plan – intyg/common

## State at Start of Plan

| Component   | Starting state | Target                 | Status  |
|-------------|---------------|------------------------|---------|
| Java        | 21            | 25                     | Pending |
| Gradle      | 8.14.4 → **9.x** (done) | Latest Gradle 9   | ✅ Done |
| Gradle plugins | various   | Latest Gradle-9-compatible | ✅ Done |
| Spring Boot | 3.5.10 (BOM)  | Latest Spring Boot 4.x | Pending |
| Jackson     | 2 (via SB BOM)| 3 (via SB 4 BOM)       | Pending |

**common** is a pure code library (no runnable application). Each phase is verified by running `./gradlew build`
(compile + unit tests) before proceeding to the next.

No wsdl2java plugin is used here — that concern belongs to other projects (webcert, intyg-proxy-service).

> Gradle wrapper upgrade and plugin version bumps have already been performed manually. Work starts at Phase 2.

---

## ~~Phase 1 – Gradle 8 → 9~~ ✅ DONE

Gradle wrapper updated and all plugin versions bumped to latest Gradle-9-compatible releases. Build verified green.

---

## Phase 2 – Java 21 → 25

**Files:** `build.gradle` (root, toolchain setting)

1. Change the Java toolchain in root `build.gradle`:
   ```groovy
   java {
     toolchain {
       languageVersion = JavaLanguageVersion.of(25)
     }
   }
   ```
2. Bump `googleJavaFormatVersion` if the current version does not support Java 25 syntax.
3. Verify that `org.openjdk.nashorn:nashorn-core` (used in `common-pdf`) still compiles and tests pass — upgrade if needed.
4. Run `./gradlew build` and fix any Java 25 compiler errors.

**Verification:** `./gradlew build` green.

---

## Phase 3 – Spring Boot BOM 3.x → 4.x

> Spring Boot 4 requires Spring Framework 7 and mandates Jackson 3. Perform Phase 4 (Jackson migration)
> immediately after this phase — the build will not be green until both are complete.

**Files:** `build.gradle` (root, `springbootMavenBomVersion`)

1. Set `springbootMavenBomVersion` to the latest Spring Boot 4.x release.
2. Review the [Spring Boot 4.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide) for library-relevant breaking changes:
   - Spring Framework 7: stricter constructor injection, `@Autowired` field injection changes.
   - `jakarta.*` namespace (already migrated — verify no regressions).
   - Changes to `HttpMethod`, `MediaType`, web binding types.
   - Auto-configuration registration format (`AutoConfiguration.imports` — already the SB3 format).
3. Run `./gradlew build` (expect Jackson-related failures until Phase 4 is done).

---

## Phase 4 – Jackson 2 → Jackson 3

This is the most extensive phase. Approximately **345 Java source files** across all subprojects use
Jackson imports and will need updating.

### 4a – Build file changes

In Jackson 3 the Maven group **and** Java package prefix changed:

| Jackson 2 artifact                              | Jackson 3 artifact                         |
|-------------------------------------------------|--------------------------------------------|
| `com.fasterxml.jackson.core:jackson-core`       | `tools.jackson.core:jackson-core`          |
| `com.fasterxml.jackson.core:jackson-databind`   | `tools.jackson.core:jackson-databind`      |
| `com.fasterxml.jackson.core:jackson-annotations`| `tools.jackson.core:jackson-annotations`   |
| `com.fasterxml.jackson.datatype:jackson-datatype-jsr310` | **Dropped** — built into `jackson-databind` in Jackson 3 |

Files to update: `build.gradle` in `support`, `util/integration-util`, `services`, `pdf`, `fk/fk-parent`,
`fk/luse`, `fk/lisjp`, `fk/luae_na`, `fk/luae_fs`, `ts/ts-parent`, `ts/ts-bas`, `ts/ts-diabetes`,
`ts/tstrk1062`, `ts/tstrk1009`, `sos/sos_parent`, `sos/db`, `sos/doi`, `skl/ag-parent`, `skl/ag114`,
`skl/ag7804`, `af/af_parent`, `af/af00213`, `af/af00251`, `fk7263`.

### 4b – Java source bulk rename

Replace all imports in `.java` files:
```
import com.fasterxml.jackson.  →  import tools.jackson.
```
Use IDE refactoring or a bulk-replace script. After bulk rename, do a targeted review of the files below.

### 4c – Manual review — key files

1. **`CustomObjectMapper`** (`util/integration-util/…/json/CustomObjectMapper.java`):
   - Remove `private static final long serialVersionUID` — `ObjectMapper` no longer implements `Serializable` in Jackson 3.
   - Same for the inner `CustomModule` class.
   - Verify `configure(DeserializationFeature…)` / `configure(SerializationFeature…)` method signatures still work.

2. **Date/time serializers/deserializers** in `util/integration-util/…/json/`:
   - `jackson-datatype-jsr310` types (`LocalDateSerializer`, etc.) move to `tools.jackson.datatype.jsr310.*` or become part of core — verify exact package.
   - `CustomLocalDateTimeSerializer`, `CustomLocalDateTimeDeserializer`, `CustomLocalDateDeserializer`, `InternalDateSerializer`, `InternalDateDeserializer`, `TemporalSerializer`, `TemporalDeserializer` — update imports and verify constructors/method signatures.

3. **All `@JsonProperty`, `@JsonIgnore`, `@JsonFormat`, `@JsonSerialize`, `@JsonDeserialize`** — import path change only; no functional changes expected.

4. **Any direct `ObjectMapper` instantiation** in test utilities (e.g. `ScenarioFinder` in module tests) — check constructor compatibility.

### 4d – Incremental verification per subproject

```
./gradlew :logging-util:build
./gradlew :integration-util:build
./gradlew :common-support:build
./gradlew :common-services:build
./gradlew :common-web:build
./gradlew :common-pdf:build
./gradlew :fk-parent:build  :luse:build  :lisjp:build  :luae_na:build  :luae_fs:build
./gradlew :fk7263:build
./gradlew :ts-parent:build  :ts-bas:build  :ts-diabetes:build  :tstrk1062:build  :tstrk1009:build
./gradlew :sos_parent:build  :db:build  :doi:build
./gradlew :ag-parent:build  :ag114:build  :ag7804:build
./gradlew :af-parent:build  :af00213:build  :af00251:build
./gradlew build   # full build
```

**Verification:** `./gradlew build` green.

---

## Phase 5 – Bump remaining dependencies and plugins

Once the core stack is stable, update remaining version properties in root `build.gradle`:

- `autovalueVersion`
- `cxfVersion` — verify CXF compatibility with Spring Framework 7
- `commonsCollectionsVersion`, `commonsTextVersion`, `commonsCsvVersion`, `commonsIoVersion`
- `equalsVerifierVersion`
- `guavaVersion`
- `googleJavaFormatVersion`
- `itextVersion`, `itext7Version`, `itextPdfHtmlVersion`
- `nashornVersion`
- `phSchematronVersion`, `saxonVersion`
- `spotbugsAnnotationVersion`
- Internal intyg schema JARs (`intygClinicalprocess*`, `rivta*`, `schemasContract*`) — coordinate with schema library owners

**Verification:** `./gradlew build` green.

---

## Phase 6 – Spring Boot autoconfig / starter review (optional, discuss with team)

`common` is consumed by Spring Boot applications via `@ComponentScan`. Consider migrating `@Configuration`
classes to Spring Boot auto-configuration registration (`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`),
removing the need for consuming apps to scan common's packages.

Candidates: all `*ModuleConfig.java` classes across every submodule.

> Discuss with team before implementing — consuming applications must be ready for this change.

---

## Risk Register

| Risk | Mitigation |
|------|-----------|
| Jackson 3 rename is very broad (~345 files) | Bulk IDE/script rename; incremental subproject builds |
| Internal schema JARs may depend on Jackson 2 | Coordinate with owners before Phase 4 |
| CXF may not yet support Spring Framework 7 | Check CXF release notes; pin version if blocked |
| `nashorn-core` compatibility with Java 25 | Run `:common-pdf:build` early; upgrade if needed |
| iText 5 (AGPL) compatibility with Java 25 | Run pdf tests; flag licensing if commercial use applies |

---

## Execution Order

```
Phase 1  →  Gradle 9 upgrade          (low risk, fast)
Phase 2  →  Java 25 toolchain         (low risk, fast)
Phase 3+4 → Spring Boot 4 + Jackson 3 (high effort — do together)
Phase 5  →  Dependency bumps          (low risk)
Phase 6  →  Autoconfig review         (optional / team decision)
```
