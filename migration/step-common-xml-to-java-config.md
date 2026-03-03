# Convert Common Library XML Bean Configuration to Java Config

## Overview

The `common` library ships three categories of XML configuration files that are loaded by consuming applications (e.g. `intygstjanst`) via
`@ImportResource`:

```java
@ImportResource({
    "classpath:common-config.xml",       // from support module
    "classpath*:module-config.xml",      // from every certificate module (16 files)
    "classpath*:it-module-cxf-servlet.xml" // from fk7263, ts-bas, ts-diabetes (3 files)
})
```

Additionally, there are `wc-module-cxf-servlet.xml` and `mi-module-cxf-servlet.xml` files loaded by Webcert and Mina intyg respectively (not
relevant for intygstjänst, but included for completeness).

The goal is to eliminate **all** XML configuration from the common library so that consuming applications no longer need `@ImportResource`
for any of these files.

---

## Progress Tracker

| Step     | Description                                                                            | Status | Verified | Notes                                                                                                                                                                                                                                                                                                                                                                                                             |
|----------|----------------------------------------------------------------------------------------|--------|----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **C.1**  | Convert `common-config.xml` → `@ComponentScan` on support module                       | ✅ DONE | ✅        | `CommonSupportConfig.java` created in `se.inera.intyg.common.support.config`; all tests pass                                                                                                                                                                                                                                                                                                                      |
| **C.2**  | Convert simple `module-config.xml` + `*-beans.xml` (component-scan only modules)       | ✅ DONE | ✅        | 12 `@Configuration` classes created; 6 empty `*-ws-stub.xml` deleted; dangling `<import>` lines removed                                                                                                                                                                                                                                                                                                           |
| **C.3**  | Convert `fk7263` `module-config.xml` + `fk7263-beans.xml` (explicit bean declarations) | ✅ DONE | ✅        | `Fk7263ModuleConfig.java` created; `@Component` added to `WebcertModelFactoryImpl`, `Fk7263ModelCompareUtil`, `InternalDraftValidator`, `Fk7263EntryPoint`; `fk7263-beans.xml` replaced with component-scan                                                                                                                                                                                                       |
| **C.4**  | Convert `fk-parent` `module-config.xml` (stub endpoint + ValidatorUtilFK bean)         | ✅ DONE | ✅        | `FkParentModuleConfig.java` + `FkParentStubConfig.java` created; `RegisterCertificateResponderStub.java` fixed (duplicate imports); `@Component` added to `ValidatorUtilFK`; `cxf-rt-frontend-jaxws` + `spring-context` added to `fk-parent/build.gradle`                                                                                                                                                         |
| **C.5**  | Convert `ag114` `module-config.xml` (ValidatorUtilSKL bean)                            | ✅ DONE | ✅        | `Ag114ModuleConfig.java` created in `se.inera.intyg.common.ag114.config`; `@Component` added to `ValidatorUtilSKL`; all tests pass                                                                                                                                                                                                                                                                                |
| **C.6**  | Convert `ts-diabetes` `module-config.xml` (empty profile blocks + component-scan)      | ✅ DONE | ✅        | `TsDiabetesModuleConfig.java` created in `se.inera.intyg.common.ts_diabetes.config`; empty profile blocks dropped; all tests pass                                                                                                                                                                                                                                                                                 |
| **C.7**  | Convert `it-module-cxf-servlet.xml` — fk7263 (CXF endpoints + client)                  | ✅ DONE | ✅        | `Fk7263ItCxfConfig.java` created in `se.inera.intyg.common.fk7263.config`; `@Profile("intygstjanst")` guards IT endpoints; `cxf-core` + `cxf-rt-frontend-jaxws` + `integration-util` promoted to `implementation` in `fk7263/build.gradle`                                                                                                                                                                        |
| **C.8**  | Convert `it-module-cxf-servlet.xml` — ts-bas (bean declarations, no CXF endpoints)     | ✅ DONE | ✅        | `TsBasItCxfConfig.java` created in `se.inera.intyg.common.ts_bas.config`; `@Profile("intygstjanst")` guards the beans; no new build.gradle dependencies needed (ts-parent already on `implementation` path)                                                                                                                                                                                                       |
| **C.9**  | Convert `it-module-cxf-servlet.xml` — ts-diabetes (CXF endpoints + beans)              | ✅ DONE | ✅        | `TsDiabetesItCxfConfig.java` created in `se.inera.intyg.common.ts_diabetes.config`; `@Profile("intygstjanst")` guards IT endpoints; `cxf-core` + `cxf-rt-frontend-jaxws` promoted to `implementation` in `ts-diabetes/build.gradle`                                                                                                                                                                               |
| **C.10** | Convert all `*-ws-stub.xml` files (stub CXF endpoints, profile-conditional)            | ✅ DONE | ✅        | `Fk7263StubConfig.java` created in `se.inera.intyg.common.fk7263.config` (`@Profile("dev","it-fk-stub")`); `ts-diabetes-ws-stub.xml` deleted (dead code — implementor class `RegisterCertificateResponderStub` never existed); `ag114-ws-stub.xml` deleted (empty file); all tests pass                                                                                                                           |
| **C.11** | Convert `wc-module-cxf-servlet.xml` and `mi-module-cxf-servlet.xml` (Webcert/MI only)  | ✅ DONE | ✅        | `Fk7263WcCxfConfig.java` + `Fk7263MiCxfConfig.java` created in `se.inera.intyg.common.fk7263.config` (`@Profile("webcert")` / `@Profile("minaintyg")`); `TsDiabetesWcCxfConfig.java` + `TsDiabetesMiCxfConfig.java` created in `se.inera.intyg.common.ts_diabetes.config`; note: `mi-module-cxf-servlet.xml` source files do not exist (only IntelliJ out/ artifact for fk7263 — never committed); all tests pass |
| **C.12** | Delete all XML files + remove `@ImportResource` from intygstjänst                      | ⬜ TODO |          |                                                                                                                                                                                                                                                                                                                                                                                                                   |

**Deployment batches:**

- 🚀 **Batch 1:** Steps C.1–C.6 (component-scan + simple bean migration — safe, no CXF)
- 🚀 **Batch 2:** Steps C.7–C.9 (CXF endpoint + client migration)
- 🚀 **Batch 3:** Step C.10 (stub endpoints)
- 🚀 **Batch 4:** Step C.11 (Webcert/MI — coordinate with those apps)
- 🚀 **Batch 5:** Step C.12 (final cleanup — delete XML files, remove `@ImportResource`)

---

## Current State: Complete XML File Inventory

### 1. `common-config.xml` (support module)

| File                | Location                      | Content                                                                                    |
|---------------------|-------------------------------|--------------------------------------------------------------------------------------------|
| `common-config.xml` | `support/src/main/resources/` | `<context:component-scan base-package="se.inera.intyg.common.support.modules.converter"/>` |

**Analysis:** Single component-scan. All classes in this package already have `@Component` annotations (`UnitMapperUtil`,
`UnitMappingConfigLoader`, `TransportConverterUtil`, `SummaryConverter`, `InternalConverterUtil`). This scan just needs to be picked up by
the host application.

### 2. `module-config.xml` (16 files, one per certificate module)

These files follow a consistent pattern. Each one does `<context:annotation-config/>` + imports a `*-beans.xml` and optionally a
`*-ws-stub.xml`.

| Module          | module-config.xml content                                                        | Beans XML content                                  | Stub XML content                         |
|-----------------|----------------------------------------------------------------------------------|----------------------------------------------------|------------------------------------------|
| **lisjp**       | imports `lisjp-beans.xml` + `lisjp-ws-stub.xml`                                  | component-scan `se.inera.intyg.common.lisjp`       | Empty (profile `dev,it-fk-stub`)         |
| **luae_fs**     | imports `luae_fs-beans.xml` + `luae_fs-ws-stub.xml`                              | component-scan `se.inera.intyg.common.luae_fs`     | Empty (profile `dev,it-fk-stub`)         |
| **luae_na**     | imports `luae_na-beans.xml` + `luae_na-ws-stub.xml`                              | component-scan `se.inera.intyg.common.luae_na`     | Empty (profile `dev,it-fk-stub`)         |
| **luse**        | imports `luse-beans.xml` + `luse-ws-stub.xml`                                    | component-scan `se.inera.intyg.common.luse`        | Empty (profile `dev,it-fk-stub`)         |
| **af00213**     | imports `af00213-beans.xml` + `af00213-ws-stub.xml`                              | component-scan `se.inera.intyg.common.af00213`     | Empty (profile `dev,it-af-stub`)         |
| **af00251**     | imports `af00251-beans.xml` + `af00251-ws-stub.xml`                              | component-scan `se.inera.intyg.common.af00251`     | Empty (profile `dev,it-af-stub`)         |
| **db** (sos)    | imports `db-beans.xml`                                                           | component-scan `se.inera.intyg.common.db`          | No stub file                             |
| **doi** (sos)   | imports `doi-beans.xml`                                                          | component-scan `se.inera.intyg.common.doi`         | No stub file                             |
| **ts-bas**      | imports `ts-bas-beans.xml`                                                       | component-scan `se.inera.intyg.common.ts_bas`      | No stub file                             |
| **ts-diabetes** | imports `ts-diabetes-beans.xml` + component-scan + empty profile blocks          | component-scan `se.inera.intyg.common.ts_diabetes` | Stub endpoint (profile `dev,test,local`) |
| **tstrk1009**   | imports `tstrk1009-beans.xml`                                                    | component-scan `se.inera.intyg.common.tstrk1009`   | No stub file                             |
| **tstrk1062**   | imports `tstrk1062-beans.xml`                                                    | component-scan `se.inera.intyg.common.tstrk1062`   | No stub file                             |
| **ag7804**      | imports `ag7804-beans.xml`                                                       | component-scan `se.inera.intyg.common.ag7804`      | No stub file                             |
| **ag114**       | imports `ag114-beans.xml` + `ag114-ws-stub.xml` + ValidatorUtilSKL bean          | component-scan `se.inera.intyg.common.ag114`       | Empty (profile `dev,it-fk-stub`)         |
| **fk-parent**   | RegisterCertificateResponderStub bean + CXF stub endpoint + ValidatorUtilFK bean | N/A (inline beans)                                 | Inline (profile `dev,it-fk-stub`)        |
| **fk7263**      | imports `fk7263-beans.xml` + `fk7263-ws-stub.xml`                                | **Explicit bean declarations** (see below)         | Stub endpoint (profile `dev,it-fk-stub`) |

### 3. `it-module-cxf-servlet.xml` (3 files — loaded only by intygstjänst)

| Module          | Content                                                                                                                                                                                         |
|-----------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **fk7263**      | CXF bus, 3 `jaxws:endpoint` (GetCertificate, GetMedicalCertificate, RegisterMedicalCertificate with outFaultInterceptors), 1 `jaxws:client` (registerMedicalCertificateClient)                  |
| **ts-bas**      | CXF bus, 4 bean declarations (`tsBasRegisterCertificateVersion`, `tsBasRegisterCertificateV1Client`, `tsBasRegisterCertificateV3Client`, `sendTSClientFactory` with map) — **no CXF endpoints** |
| **ts-diabetes** | CXF bus, 2 `jaxws:endpoint` (getTSDiabetes, registerTSDiabetes with outFaultInterceptors + schemaLocations), 2 bean declarations (`tsDiabetesXslTransformer`, `sendTsDiabetesClient`)           |

### 4. `wc-module-cxf-servlet.xml` (2 files — loaded only by Webcert)

| Module          | Content                                                                                      |
|-----------------|----------------------------------------------------------------------------------------------|
| **fk7263**      | CXF bus, 2 `jaxws:client` (registerMedicalCertificateClient, getMedicalCertificateResponder) |
| **ts-diabetes** | CXF bus, 2 `jaxws:client` (diabetesGetClient, diabetesRegisterClient)                        |

### 5. `mi-module-cxf-servlet.xml` (2 files — loaded only by Mina intyg)

| Module          | Content                                           |
|-----------------|---------------------------------------------------|
| **fk7263**      | 1 `jaxws:client` (getMedicalCertificateResponder) |
| **ts-diabetes** | 1 `jaxws:client` (diabetesGetClient)              |

---

## Dynamic Module Loading Mechanism

The module discovery mechanism works as follows:

1. **`classpath*:module-config.xml`** — Spring's wildcard classpath loading picks up every `module-config.xml` from every JAR on the
   classpath. Each module JAR has one.

2. Each `module-config.xml` does a `<context:component-scan>` of its module's package, which discovers:
    - `ModuleEntryPoint` implementations (annotated with `@Component`) — these get `@Autowired` into
      `IntygModuleRegistryImpl.moduleEntryPoints`
    - `ModuleApi` implementations (annotated with `@Component("moduleapi.xxx.vN")`) — these get looked up by bean name

3. **`classpath*:it-module-cxf-servlet.xml`** — loads CXF endpoints and clients specific to intygstjänst

**Key insight for migration:** The `classpath*:` wildcard loading means we can't use a single `@Configuration` class. Each module needs to
be self-registering. The replacement mechanism should use *
*Spring's `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`** or a similar auto-discovery mechanism.

### Recommended Approach: `@Configuration` + Spring SPI

Since this is NOT a Spring Boot application (it uses a traditional WAR deployment with `AnnotationConfigWebApplicationContext`), we can't
use Spring Boot auto-configuration. Instead, we have two options:

#### Option A: Explicit component-scan from host app (Simplest)

The host application (`intygstjänst`) adds all module base packages to its `@ComponentScan`. This is simple but requires the host to know
about all modules.

#### Option B: `@Configuration` classes per module + Spring's `spring.factories` / component index (Recommended)

Each module provides a `@Configuration` class. The host application discovers them via:

- A shared base package convention (e.g., all modules under `se.inera.intyg.common.*`)
- Or a marker interface/annotation that the host scans for

#### Option C: `@Configuration` per module + `@Import` via `spring.factories` (Best for library modules)

Use `META-INF/spring.factories` with `org.springframework.context.annotation.ImportSelector` or simply list configuration classes. However,
this is a Spring Boot mechanism.

### **Chosen Approach: Module `@Configuration` Classes + Host Component-Scan**

Each module gets a `@Configuration` class (e.g., `LisjpModuleConfig.java`) placed in its base package. The host application does:

```java
@ComponentScan(basePackages = {
    "se.inera.intyg.common"  // catches all module @Configuration classes
})
```

Or more specifically, lists each module's package. Since the host already knows which module JARs are on the classpath, this is safe and
explicit.

For the **CXF endpoints and clients** (`it-module-cxf-servlet.xml`), these are application-specific (different for intygstjänst vs Webcert).
The `@Configuration` classes should use Spring `@Profile` or `@ConditionalOnProperty` to ensure they only activate in the correct
application. Alternatively, these configurations can be moved to use a naming convention like `ItModuleCxfConfig.java` that the host
explicitly imports.

---

## Step C.1 — Convert `common-config.xml` → Java Config

**What:** `common-config.xml` in `support/src/main/resources/` contains only:

```xml

<context:component-scan base-package="se.inera.intyg.common.support.modules.converter"/>
```

All classes in this package already have `@Component` annotations.

**Changes:**

1. Create `CommonSupportConfig.java` in `se.inera.intyg.common.support.modules.converter` (or in `se.inera.intyg.common.support.config`):
   ```java
   @Configuration
   @ComponentScan(basePackages = "se.inera.intyg.common.support.modules.converter")
   public class CommonSupportConfig {
   }
   ```

2. **Alternative (simpler):** Since all classes already have `@Component`, the host application just needs to include
   `se.inera.intyg.common.support.modules.converter` in its `@ComponentScan`. No new class needed in the library — just update the host app.

3. **However**, `fk7263-beans.xml` also declares `UnitMappingConfigLoader`, `UnitMapperUtil`, and `InternalConverterUtil` as explicit
   `<bean>` elements with constructor args. These classes already have `@Component` **and** proper constructor injection (`@Component` +
   constructor with `@Autowired` implicit). The XML `<bean>` declarations in `fk7263-beans.xml` create **duplicate beans**. The XML beans
   will need to be removed (Step C.3) to avoid conflicts.

**Decision:** Create a `@Configuration` class in the `support` module that the host can `@Import` or pick up via component-scan. This
replaces the `common-config.xml` import.

**Files changed:**

- New: `support/src/main/java/se/inera/intyg/common/support/config/CommonSupportConfig.java`
- Keep `common-config.xml` for now (deleted in Step C.12)

**Verify:**

- `./gradlew test` — all tests pass in the `support` module

---

## Step C.2 — Convert Simple Module Configs (component-scan only)

**What:** 11 modules have a trivial pattern:

- `module-config.xml` → `<context:annotation-config/>` + imports `*-beans.xml` (+ optionally `*-ws-stub.xml`)
- `*-beans.xml` → `<context:component-scan base-package="se.inera.intyg.common.MODULE_PACKAGE"/>`
- `*-ws-stub.xml` → Empty (just the `<beans>` root element with a profile attribute)

These modules are: **lisjp, luae_fs, luae_na, luse, af00213, af00251, db, doi, ts-bas, tstrk1009, tstrk1062, ag7804**

For all of these, the `module-config.xml` → `*-beans.xml` chain is equivalent to just doing a `@ComponentScan` of the module's package.
Since all the important classes (`EntryPoint`, `ModuleApi`, validators, converters, etc.) already have `@Component` or similar annotations,
the XML files are redundant once the component-scan is triggered.

**Changes for each module:**

1. Create a `@Configuration` class in each module's base package:

   Example for `lisjp`:
   ```java
   package se.inera.intyg.common.lisjp.config;

   @Configuration
   @ComponentScan(basePackages = "se.inera.intyg.common.lisjp")
   public class LisjpModuleConfig {
   }
   ```

   Repeat for: `luae_fs`, `luae_na`, `luse`, `af00213`, `af00251`, `db`, `doi`, `ts_bas`, `tstrk1009`, `tstrk1062`, `ag7804`

2. Keep XML files for now (deleted in Step C.12)

**Note on empty stub files:** The empty `*-ws-stub.xml` files (lisjp, luae_fs, luae_na, luse, af00213, af00251, ag114) can simply be
deleted — they contain no beans. They were likely placeholders for future stub endpoints.

**Modules and their config class names:**

| Module    | Package                           | Config Class            |
|-----------|-----------------------------------|-------------------------|
| lisjp     | `se.inera.intyg.common.lisjp`     | `LisjpModuleConfig`     |
| luae_fs   | `se.inera.intyg.common.luae_fs`   | `LuaefsModuleConfig`    |
| luae_na   | `se.inera.intyg.common.luae_na`   | `LuaenaModuleConfig`    |
| luse      | `se.inera.intyg.common.luse`      | `LuseModuleConfig`      |
| af00213   | `se.inera.intyg.common.af00213`   | `Af00213ModuleConfig`   |
| af00251   | `se.inera.intyg.common.af00251`   | `Af00251ModuleConfig`   |
| db        | `se.inera.intyg.common.db`        | `DbModuleConfig`        |
| doi       | `se.inera.intyg.common.doi`       | `DoiModuleConfig`       |
| ts_bas    | `se.inera.intyg.common.ts_bas`    | `TsBasModuleConfig`     |
| tstrk1009 | `se.inera.intyg.common.tstrk1009` | `Tstrk1009ModuleConfig` |
| tstrk1062 | `se.inera.intyg.common.tstrk1062` | `Tstrk1062ModuleConfig` |
| ag7804    | `se.inera.intyg.common.ag7804`    | `Ag7804ModuleConfig`    |

**Files changed:**

- New: 12 `@Configuration` classes (one per module)
- Empty stub XMLs can be deleted immediately (they have no content)

**Verify:**

- `./gradlew test` — all tests pass

---

## Step C.3 — Convert `fk7263` Module Config (Explicit Bean Declarations)

**What:** `fk7263-beans.xml` is unique — it doesn't just do a component-scan. It explicitly declares 8 beans:

```xml

<bean class="se.inera.intyg.common.fk7263.model.converter.WebcertModelFactoryImpl"/>
<bean name="moduleapi.fk7263.v1" class="se.inera.intyg.common.fk7263.rest.Fk7263ModuleApi"/>
<bean class="se.inera.intyg.common.fk7263.model.util.Fk7263ModelCompareUtil"/>
<bean class="se.inera.intyg.common.fk7263.validator.InternalDraftValidator"/>
<bean id="Fk7263EntryPoint" class="se.inera.intyg.common.fk7263.support.Fk7263EntryPoint"/>
<bean id="unitMappingConfigLoader" class="...UnitMappingConfigLoader"/>
<bean id="unitMapperUtil" class="...UnitMapperUtil">
<constructor-arg ref="unitMappingConfigLoader"/>
</bean>
<bean id="internalConverterUtil" class="...InternalConverterUtil">
<constructor-arg ref="unitMapperUtil"/>
</bean>
<bean id="transportToInternal" class="...fk7263.model.converter.TransportToInternal">
<constructor-arg ref="unitMapperUtil"/>
</bean>
```

**Analysis:**

| Bean                      | Has `@Component`?                     | Action                                                                    |
|---------------------------|---------------------------------------|---------------------------------------------------------------------------|
| `WebcertModelFactoryImpl` | ❌ No                                  | Add `@Component`                                                          |
| `Fk7263ModuleApi`         | ✅ `@Component("moduleapi.fk7263.v1")` | Already annotated — **remove from XML** (duplicate!)                      |
| `Fk7263ModelCompareUtil`  | ❌ No                                  | Add `@Component`                                                          |
| `InternalDraftValidator`  | ❌ No                                  | Add `@Component`                                                          |
| `Fk7263EntryPoint`        | ❌ No                                  | Add `@Component("Fk7263EntryPoint")`                                      |
| `UnitMappingConfigLoader` | ✅ `@Component`                        | Already annotated — **remove from XML** (duplicate via common-config.xml) |
| `UnitMapperUtil`          | ✅ `@Component`                        | Already annotated — **remove from XML** (duplicate)                       |
| `InternalConverterUtil`   | ✅ `@Component`                        | Already annotated — **remove from XML** (duplicate)                       |
| `TransportToInternal`     | ✅ `@Component`                        | Already annotated — **remove from XML** (duplicate)                       |

**Changes:**

1. Add `@Component` to the 4 classes that don't have it:
    - `WebcertModelFactoryImpl` → `@Component`
    - `Fk7263ModelCompareUtil` → `@Component`
    - `InternalDraftValidator` → `@Component`
    - `Fk7263EntryPoint` → `@Component("Fk7263EntryPoint")`

2. Create `Fk7263ModuleConfig.java`:
   ```java
   @Configuration
   @ComponentScan(basePackages = "se.inera.intyg.common.fk7263")
   public class Fk7263ModuleConfig {
   }
   ```

3. Keep XML for now (deleted in Step C.12)

**Risk:** The XML declares duplicate beans for classes that already have `@Component` (`Fk7263ModuleApi`, `UnitMappingConfigLoader`,
`UnitMapperUtil`, `InternalConverterUtil`, `TransportToInternal`). Once we switch from XML to component-scan, these duplicates go away.
During the transition period where BOTH XML and component-scan are active, we may get "duplicate bean" errors. To avoid this, in the
transition period the `module-config.xml` should remain active and we should NOT add a component-scan config class. Instead, just add
`@Component` to the missing classes and remove the XML bean declarations for the already-annotated ones. The `module-config.xml` file will
still import `fk7263-beans.xml` which will still have the remaining bean declarations.

**Revised approach:**

1. Add `@Component` to the 4 unannotated classes
2. Remove the 5 already-annotated bean declarations from `fk7263-beans.xml`
3. Replace the remaining 4 bean declarations with a `@ComponentScan` in `fk7263-beans.xml` OR create a `Fk7263ModuleConfig.java` and update
   `module-config.xml` to import it
4. Simplest: Just replace the entire `fk7263-beans.xml` content with a component-scan now that all classes have `@Component`:
   ```xml
   <context:component-scan base-package="se.inera.intyg.common.fk7263"/>
   ```

**Files changed:**

- Modify: `Fk7263EntryPoint.java` (add `@Component("Fk7263EntryPoint")`)
- Modify: `WebcertModelFactoryImpl.java` (add `@Component`)
- Modify: `Fk7263ModelCompareUtil.java` (add `@Component`)
- Modify: `InternalDraftValidator.java` (add `@Component`)
- Modify: `fk7263-beans.xml` → replace with `<context:component-scan base-package="se.inera.intyg.common.fk7263"/>`
- New: `Fk7263ModuleConfig.java` (for the final stage when XML is deleted)

**Verify:**

- `./gradlew :fk7263:test` — all tests pass
- Start intygstjänst → fk7263 module loads correctly

---

## Step C.4 — Convert `fk-parent` Module Config

**What:** `fk-parent/src/main/resources/module-config.xml` contains:

```xml
<!-- Profile-conditional stub -->
<beans profile="dev,it-fk-stub">
	<bean id="register-fk-stub" class="...RegisterCertificateResponderStub"/>
	<jaxws:endpoint address="/stubs/clinicalprocess/.../RegisterCertificate/3/rivtabp21"
			implementor="#register-fk-stub">
		<jaxws:schemaLocations>
			<!-- 7 schema locations -->
		</jaxws:schemaLocations>
	</jaxws:endpoint>
</beans>

		<!-- Unconditional bean -->
<beans>
<bean class="se.inera.intyg.common.fkparent.model.validator.ValidatorUtilFK"/>
</beans>
```

**Analysis:**

| Bean                               | Has `@Component`? | Action                                                |
|------------------------------------|-------------------|-------------------------------------------------------|
| `ValidatorUtilFK`                  | ❌ No              | Add `@Component`                                      |
| `RegisterCertificateResponderStub` | ❌ No              | Add `@Component("register-fk-stub")` + `@Profile("dev | it-fk-stub")` or declare as `@Bean` in config |
| CXF stub endpoint                  | N/A               | Move to `@Configuration` class                        |

**Changes:**

1. Add `@Component` to `ValidatorUtilFK`
2. Create `FkParentModuleConfig.java`:
   ```java
   @Configuration
   @ComponentScan(basePackages = "se.inera.intyg.common.fkparent")
   public class FkParentModuleConfig {
   }
   ```

3. Create `FkParentStubConfig.java` for the stub endpoint:
   ```java
   @Configuration
   @Profile({"dev", "it-fk-stub"})
   public class FkParentStubConfig {
   
       @Autowired private Bus bus;
   
       @Bean
       public RegisterCertificateResponderStub registerFkStub() {
           return new RegisterCertificateResponderStub();
       }
   
       @Bean
       public Endpoint registerFkStubEndpoint(RegisterCertificateResponderStub implementor) {
           EndpointImpl endpoint = new EndpointImpl(bus, implementor);
           endpoint.setSchemaLocations(List.of(
               "classpath:/core_components/clinicalprocess_healthcond_certificate_3.3.xsd",
               "classpath:/core_components/clinicalprocess_healthcond_certificate_types_3.2.xsd",
               "classpath:/core_components/clinicalprocess_healthcond_certificate_3.2_ext.xsd",
               "classpath:/core_components/clinicalprocess_healthcond_certificate_3.4_ext.xsd",
               "classpath:/core_components/xmldsig-core-schema_0.1.xsd",
               "classpath:/core_components/xmldsig-filter2.xsd",
               "classpath:/interactions/RegisterCertificateInteraction/RegisterCertificateResponder_3.1.xsd"
           ));
           endpoint.publish("/stubs/clinicalprocess/healthcond/certificate/RegisterCertificate/3/rivtabp21");
           return endpoint;
       }
   }
   ```

**Files changed:**

- Modify: `ValidatorUtilFK.java` (add `@Component`)
- New: `FkParentModuleConfig.java`
- New: `FkParentStubConfig.java`
- Keep `module-config.xml` for now

**Verify:**

- `./gradlew test` — all tests pass

---

## Step C.5 — Convert `ag114` Module Config

**What:** `ag114/module-config.xml` has an extra bean beyond the standard pattern:

```xml

<bean class="se.inera.intyg.common.ag114.v1.validator.ValidatorUtilSKL"/>
```

**Changes:**

1. Add `@Component` to `ValidatorUtilSKL`
2. Create `Ag114ModuleConfig.java` (component-scan class, similar to other modules)

**Files changed:**

- Modify: `ValidatorUtilSKL.java` (add `@Component`)
- New: `Ag114ModuleConfig.java`

**Verify:**

- `./gradlew :skl:ag114:test` — all tests pass

---

## Step C.6 — Convert `ts-diabetes` Module Config

**What:** `ts-diabetes/module-config.xml` has empty profile blocks (`dev`, `test`, `qa`, `prod`) and a component-scan. The
`ts-diabetes-beans.xml` is a duplicate component-scan of the same package.

**Changes:**

1. The empty profile blocks are no-ops — safe to remove
2. Create `TsDiabetesModuleConfig.java` (already covered in Step C.2 if ts-diabetes was included there)
3. If not already done: this module needs its own config class

**Files changed:**

- New: `TsDiabetesModuleConfig.java` (if not already created in C.2)

**Verify:**

- `./gradlew :ts:ts-diabetes:test` — all tests pass

---

## Step C.7 — Convert `it-module-cxf-servlet.xml` — fk7263

**What:** This file (loaded ONLY by intygstjänst via `classpath*:it-module-cxf-servlet.xml`) contains:

1. **CXF Bus configuration** with logging feature
2. **3 JAX-WS endpoints:**
    - `/get-certificate/v1.0` → `GetCertificateResponderImpl` (with outFaultInterceptor)
    - `/get-medical-certificate/v1.0` → `GetMedicalCertificateResponderImpl` (with outFaultInterceptor)
    - `/register-certificate/v3.0` → `RegisterMedicalCertificateResponderImpl` (with outFaultInterceptor)
3. **1 JAX-WS client:**
    - `registerMedicalCertificateClient` → `RegisterMedicalCertificateResponderInterface` at `${registermedicalcertificatev3.endpoint.url}`

**Approach:** Create a `@Configuration` class in the fk7263 module that is activated only in the intygstjänst context. Use a naming
convention or `@ConditionalOnProperty` to differentiate from Webcert/MI configurations.

**Changes:**

1. Create `Fk7263ItCxfConfig.java`:
   ```java
   @Configuration
   @ConditionalOnProperty(name = "application.origin", havingValue = "INTYGSTJANST")
   // OR use @Profile("it") if intygstjänst uses such a profile
   public class Fk7263ItCxfConfig {
   
       @Autowired private Bus bus;
   
       @Bean
       public GetCertificateResponderImpl getCertificateResponder() {
           return new GetCertificateResponderImpl();
       }
       
       @Bean
       public Endpoint getCertificateEndpoint(GetCertificateResponderImpl implementor) {
           EndpointImpl ep = new EndpointImpl(bus, implementor);
           ep.getOutFaultInterceptors().add(
               new SoapFaultToSoapResponseTransformerInterceptor("transform/get-certificate-transform.xslt"));
           ep.publish("/get-certificate/v1.0");
           return ep;
       }
       
       // ... similar for GetMedicalCertificate, RegisterMedicalCertificate
       
       @Bean("registerMedicalCertificateClient")
       public RegisterMedicalCertificateResponderInterface registerMedicalCertificateClient(
               @Value("${registermedicalcertificatev3.endpoint.url}") String address) {
           JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
           factory.setServiceClass(RegisterMedicalCertificateResponderInterface.class);
           factory.setAddress(address);
           return (RegisterMedicalCertificateResponderInterface) factory.create();
       }
   }
   ```

**Important consideration:** The CXF endpoint implementors (`GetCertificateResponderImpl`, etc.) don't have `@Component` — they rely on CXF
to instantiate them. When converting, we either:

- a) Declare them as `@Bean` in the config class (recommended — explicit)
- b) Add `@Component` to them (risk: they get created even when not needed)

Option (a) is better because these endpoints should only exist in intygstjänst, not in Webcert or MI.

**Alternative approach — Move to intygstjänst:** Since these endpoints are specific to intygstjänst, it may be cleaner to move the
`@Configuration` class to intygstjänst itself (in `CxfEndpointConfig.java` which already exists from Step 9.8). This would mean NO new Java
config in common — just deleting the XML file. The config already exists in intygstjänst's `CxfEndpointConfig.java` since Step 9.8 already
converted these. **If Step 9.8 is already done, this XML file is already dead code.**

**Decision:** Since Steps 9.7–9.8 in intygstjänst already converted CXF endpoints/clients to Java, the `it-module-cxf-servlet.xml` files may
already be redundant. Verify whether intygstjänst's `CxfEndpointConfig.java` already handles these endpoints. If yes, just delete the XML
files.

**Files changed:**

- If already handled by intygstjänst: Delete `fk7263/src/main/resources/it-module-cxf-servlet.xml`
- If NOT yet handled: Create `Fk7263ItCxfConfig.java` OR move the endpoint declarations to intygstjänst

**Verify:**

- Start intygstjänst → all fk7263 SOAP endpoints still work

---

## Step C.8 — Convert `it-module-cxf-servlet.xml` — ts-bas

**What:** This file contains NO CXF endpoints. It declares 4 beans:

```xml

<bean id="tsBasRegisterCertificateVersion" class="java.lang.String">
	<constructor-arg value="${tsbas.send.certificate.to.recipient.registercertificate.version}"/>
</bean>

<bean id="tsBasRegisterCertificateV1Client" class="...RegisterCertificateV1Client">
<constructor-arg value="${registercertificatev1.endpoint.url}"/>
</bean>

<bean id="tsBasRegisterCertificateV3Client" class="...RegisterCertificateV3Client">
<constructor-arg value="${registercertificatev3.endpoint.url}"/>
</bean>

<util:map id="registerCertificateClientMap">
<entry key="v1" value-ref="tsBasRegisterCertificateV1Client"/>
<entry key="v3" value-ref="tsBasRegisterCertificateV3Client"/>
</util:map>

<bean id="sendTSClientFactory" class="...SendTSClientFactory">
<constructor-arg ref="registerCertificateClientMap"/>
</bean>
```

**Changes:**

1. Create `TsBasItCxfConfig.java` (or add to intygstjänst's config):
   ```java
   @Configuration
   public class TsBasItCxfConfig {
   
       @Bean("tsBasRegisterCertificateVersion")
       public String tsBasRegisterCertificateVersion(
               @Value("${tsbas.send.certificate.to.recipient.registercertificate.version}") String version) {
           return version;
       }
   
       @Bean("tsBasRegisterCertificateV1Client")
       public RegisterCertificateV1Client tsBasRegisterCertificateV1Client(
               @Value("${registercertificatev1.endpoint.url}") String url) {
           return new RegisterCertificateV1Client(url);
       }
   
       @Bean("tsBasRegisterCertificateV3Client")
       public RegisterCertificateV3Client tsBasRegisterCertificateV3Client(
               @Value("${registercertificatev3.endpoint.url}") String url) {
           return new RegisterCertificateV3Client(url);
       }
   
       @Bean
       public SendTSClientFactory sendTSClientFactory(
               @Qualifier("tsBasRegisterCertificateV1Client") RegisterCertificateV1Client v1,
               @Qualifier("tsBasRegisterCertificateV3Client") RegisterCertificateV3Client v3) {
           Map<String, SendTSClient> map = new HashMap<>();
           map.put("v1", v1);
           map.put("v3", v3);
           return new SendTSClientFactory(map);
       }
   }
   ```

**Decision:** These beans are intygstjänst-specific. Move to intygstjänst's config or keep in common with a condition.

**Files changed:**

- New: `TsBasItCxfConfig.java` (in ts-bas or intygstjänst)
- Delete: `ts-bas/src/main/resources/it-module-cxf-servlet.xml` (after config is in place)

**Verify:**

- Start intygstjänst → ts-bas certificate sending still works

---

## Step C.9 — Convert `it-module-cxf-servlet.xml` — ts-diabetes

**What:** Contains:

- 2 CXF endpoints (get + register ts-diabetes, with outFaultInterceptors and schemaLocations)
- 2 bean declarations (`XslTransformer`, `RegisterCertificateV1Client`)

**Changes:**

1. Create `TsDiabetesItCxfConfig.java`:
   ```java
   @Configuration
   public class TsDiabetesItCxfConfig {
   
       @Autowired private Bus bus;
   
       @Bean
       public GetTSDiabetesResponderImpl getTSDiabetesResponder() {
           return new GetTSDiabetesResponderImpl();
       }
       
       @Bean
       public Endpoint getTSDiabetesEndpoint(GetTSDiabetesResponderImpl impl) {
           EndpointImpl ep = new EndpointImpl(bus, impl);
           ep.getOutFaultInterceptors().add(
               new SoapFaultToSoapResponseTransformerInterceptor(
                   "transform/se-intygstjanster-ts-services/get-ts-diabetes-transform.xslt"));
           ep.publish("/get-ts-diabetes/v1.0");
           return ep;
       }
       
       @Bean
       public RegisterTSDiabetesResponderImpl registerTSDiabetesResponder() {
           return new RegisterTSDiabetesResponderImpl();
       }
       
       @Bean
       public Endpoint registerTSDiabetesEndpoint(RegisterTSDiabetesResponderImpl impl) {
           EndpointImpl ep = new EndpointImpl(bus, impl);
           ep.getOutFaultInterceptors().add(
               new SoapFaultToSoapResponseTransformerInterceptor(
                   "transform/se-intygstjanster-ts-services/register-ts-diabetes-transform.xslt"));
           ep.getProperties().put("schema-validation-enabled", "true");
           ep.setSchemaLocations(List.of(
               "classpath:/core_components/se_intygstjanster_services_1.0.xsd",
               "classpath:/core_components/se_intygstjanster_services_types_1.0.xsd",
               "classpath:/interactions/RegisterTSDiabetesInteraction/RegisterTSDiabetesResponder_1.0.xsd"
           ));
           ep.publish("/register-ts-diabetes/v1.0");
           return ep;
       }
       
       @Bean("tsDiabetesXslTransformer")
       public XslTransformer tsDiabetesXslTransformer() {
           return new XslTransformer("xsl/transform-ts-diabetes.xsl");
       }
       
       @Bean("sendTsDiabetesClient")
       public RegisterCertificateV1Client sendTsDiabetesClient(
               @Value("${registercertificatev1.endpoint.url}") String url) {
           return new RegisterCertificateV1Client(url);
       }
   }
   ```

**Files changed:**

- New: `TsDiabetesItCxfConfig.java`
- Delete: `ts-diabetes/src/main/resources/it-module-cxf-servlet.xml`

**Verify:**

- Start intygstjänst → ts-diabetes SOAP endpoints work

---

## Step C.10 — Convert Stub WS Endpoints

**What:** Two modules have non-empty stub files:

### fk7263-ws-stub.xml (profile `dev,it-fk-stub`)

```xml

<jaxws:endpoint address="/stubs/RegisterMedicalCertificate/3/rivtabp20"
		implementor="...RegisterMedicalCertificateResponderStub"/>
```

### ts-diabetes-ws-stub.xml (profile `dev,test,local`)

```xml

<jaxws:endpoint address="/register-diabetes-certificate-stub"
		implementor="se.inera.intyg.common.ts_diabetes.integration.stub.RegisterCertificateResponderStub"/>
```

⚠️ **Note:** The class `se.inera.intyg.common.ts_diabetes.integration.stub.RegisterCertificateResponderStub` doesn't appear to exist in the
codebase! This may be dead code.

### fk-parent module-config.xml stub (already handled in Step C.4)

**Changes:**

1. For **fk7263 stub**: Create `Fk7263StubConfig.java`:
   ```java
   @Configuration
   @Profile({"dev", "it-fk-stub"})
   public class Fk7263StubConfig {
       @Autowired private Bus bus;
       
       @Bean
       public RegisterMedicalCertificateResponderStub registerMedicalCertificateStub() {
           return new RegisterMedicalCertificateResponderStub();
       }
       
       @Bean
       public Endpoint registerMedicalCertificateStubEndpoint(RegisterMedicalCertificateResponderStub impl) {
           EndpointImpl ep = new EndpointImpl(bus, impl);
           ep.publish("/stubs/RegisterMedicalCertificate/3/rivtabp20");
           return ep;
       }
   }
   ```

2. For **ts-diabetes stub**: Verify if the implementor class exists. If not, this is dead code — just delete the XML file.

**Files changed:**

- New: `Fk7263StubConfig.java`
- Delete: `fk7263-ws-stub.xml`
- Delete: `ts-diabetes-ws-stub.xml` (if dead code)

**Verify:**

- Start with stub profile → stub endpoints respond

---

## Step C.11 — Convert `wc-module-cxf-servlet.xml` and `mi-module-cxf-servlet.xml`

**What:** These are loaded by Webcert and Mina intyg respectively:

### fk7263 wc-module-cxf-servlet.xml (Webcert)

- 2 JAX-WS clients: `registerMedicalCertificateClient`, `getMedicalCertificateResponder`

### fk7263 mi-module-cxf-servlet.xml (Mina intyg)

- 1 JAX-WS client: `getMedicalCertificateResponder`

### ts-diabetes wc-module-cxf-servlet.xml (Webcert)

- 2 JAX-WS clients: `diabetesGetClient`, `diabetesRegisterClient`

### ts-diabetes mi-module-cxf-servlet.xml (Mina intyg)

- 1 JAX-WS client: `diabetesGetClient`

**Changes:**

Create application-specific config classes (or move to the respective applications):

1. `Fk7263WcCxfConfig.java` (for Webcert)
2. `Fk7263MiCxfConfig.java` (for Mina intyg)
3. `TsDiabetesWcCxfConfig.java` (for Webcert)
4. `TsDiabetesMiCxfConfig.java` (for Mina intyg)

**Decision:** Since these are application-specific JAX-WS clients, it's cleaner to move these configurations to the respective
applications (Webcert, Mina intyg). But if that's not possible (because the common library is being migrated independently), create
`@Configuration` classes in the common library with `@ConditionalOnProperty` for the application origin.

**Files changed:**

- New: 4 config classes (or move to consuming apps)
- Delete: 4 XML files

**⚠️ Coordination required:** This step requires coordination with Webcert and Mina intyg teams.

---

## Step C.12 — Delete All XML Files + Remove `@ImportResource`

**What:** After all previous steps, delete all remaining XML config files and update the consuming applications.

**Changes in `common` library:**

Delete all remaining XML config files:

| File                             | Module                          |
|----------------------------------|---------------------------------|
| `common-config.xml`              | support                         |
| `module-config.xml` (×16)        | all certificate modules         |
| `*-beans.xml` (×15)              | all certificate modules         |
| `*-ws-stub.xml` (×9)             | fk modules + af modules + ag114 |
| `it-module-cxf-servlet.xml` (×3) | fk7263, ts-bas, ts-diabetes     |
| `wc-module-cxf-servlet.xml` (×2) | fk7263, ts-diabetes             |
| `mi-module-cxf-servlet.xml` (×2) | fk7263, ts-diabetes             |

**Changes in `intygstjänst`:**

Remove from `ApplicationConfig.java`:

```java
// REMOVE:
@ImportResource({
    "classpath:common-config.xml",
    "classpath*:module-config.xml",
    "classpath*:it-module-cxf-servlet.xml"
})

// REPLACE WITH (if not already covered by existing @ComponentScan):
@ComponentScan(basePackages = {
    // ... existing packages ...
    "se.inera.intyg.common"  // picks up all module @Configuration classes
})
```

**Files changed:**

- Delete: ~47 XML files
- Modify: intygstjänst `ApplicationConfig.java`

**Verify:**

- `./gradlew test` — all tests pass in all modules
- Start intygstjänst → all SOAP endpoints work, all modules load

---

## Risk Assessment

| Risk                                                                                | Likelihood | Impact | Mitigation                                                                   |
|-------------------------------------------------------------------------------------|------------|--------|------------------------------------------------------------------------------|
| Duplicate bean definitions during transition (XML + annotations)                    | High       | Medium | Remove XML bean declarations for already-annotated classes first             |
| fk7263 classes without `@Component` not picked up by component-scan                 | Medium     | High   | Verify each class gets `@Component` before removing XML                      |
| CXF endpoints behave differently when registered programmatically                   | Medium     | High   | Test each SOAP endpoint individually                                         |
| Module discovery breaks (modules not loaded)                                        | Medium     | High   | Verify `IntygModuleRegistryImpl.initModulesList()` logs all expected modules |
| Cross-application contamination (IT endpoints in Webcert)                           | Medium     | Medium | Use `@Profile` or `@ConditionalOnProperty` on application-specific configs   |
| `ts-diabetes` stub references non-existent class                                    | Low        | Low    | Verify and delete if dead code                                               |
| `wc-module-cxf-servlet.xml` / `mi-module-cxf-servlet.xml` removal breaks Webcert/MI | High       | High   | Coordinate with those applications; do Step C.11 last                        |

---

## Summary: File Inventory

### Files to Create (in `common`)

| File                          | Module                        | Step |
|-------------------------------|-------------------------------|------|
| `CommonSupportConfig.java`    | support                       | C.1  |
| `LisjpModuleConfig.java`      | lisjp                         | C.2  |
| `LuaefsModuleConfig.java`     | luae_fs                       | C.2  |
| `LuaenaModuleConfig.java`     | luae_na                       | C.2  |
| `LuseModuleConfig.java`       | luse                          | C.2  |
| `Af00213ModuleConfig.java`    | af00213                       | C.2  |
| `Af00251ModuleConfig.java`    | af00251                       | C.2  |
| `DbModuleConfig.java`         | db                            | C.2  |
| `DoiModuleConfig.java`        | doi                           | C.2  |
| `TsBasModuleConfig.java`      | ts-bas                        | C.2  |
| `Tstrk1009ModuleConfig.java`  | tstrk1009                     | C.2  |
| `Tstrk1062ModuleConfig.java`  | tstrk1062                     | C.2  |
| `Ag7804ModuleConfig.java`     | ag7804                        | C.2  |
| `Fk7263ModuleConfig.java`     | fk7263                        | C.3  |
| `FkParentModuleConfig.java`   | fk-parent                     | C.4  |
| `FkParentStubConfig.java`     | fk-parent                     | C.4  |
| `Ag114ModuleConfig.java`      | ag114                         | C.5  |
| `TsDiabetesModuleConfig.java` | ts-diabetes                   | C.6  |
| `Fk7263ItCxfConfig.java`      | fk7263 (or intygstjänst)      | C.7  |
| `TsBasItCxfConfig.java`       | ts-bas (or intygstjänst)      | C.8  |
| `TsDiabetesItCxfConfig.java`  | ts-diabetes (or intygstjänst) | C.9  |
| `Fk7263StubConfig.java`       | fk7263                        | C.10 |
| `Fk7263WcCxfConfig.java`      | fk7263                        | C.11 |
| `Fk7263MiCxfConfig.java`      | fk7263                        | C.11 |
| `TsDiabetesWcCxfConfig.java`  | ts-diabetes                   | C.11 |
| `TsDiabetesMiCxfConfig.java`  | ts-diabetes                   | C.11 |

### Files to Modify (in `common`)

| File                                                       | Step |
|------------------------------------------------------------|------|
| `Fk7263EntryPoint.java` — add `@Component`                 | C.3  |
| `WebcertModelFactoryImpl.java` (fk7263) — add `@Component` | C.3  |
| `Fk7263ModelCompareUtil.java` — add `@Component`           | C.3  |
| `InternalDraftValidator.java` (fk7263) — add `@Component`  | C.3  |
| `fk7263-beans.xml` — simplify to component-scan            | C.3  |
| `ValidatorUtilFK.java` — add `@Component`                  | C.4  |
| `ValidatorUtilSKL.java` — add `@Component`                 | C.5  |

### Files to Delete (in `common`, Step C.12)

| Count  | Pattern                     | Modules                     |
|--------|-----------------------------|-----------------------------|
| 1      | `common-config.xml`         | support                     |
| 16     | `module-config.xml`         | all modules                 |
| 15     | `*-beans.xml`               | all modules                 |
| 9      | `*-ws-stub.xml`             | fk/af/ag modules            |
| 3      | `it-module-cxf-servlet.xml` | fk7263, ts-bas, ts-diabetes |
| 2      | `wc-module-cxf-servlet.xml` | fk7263, ts-diabetes         |
| 2      | `mi-module-cxf-servlet.xml` | fk7263, ts-diabetes         |
| **48** | **Total**                   |                             |

### Changes in `intygstjänst`

| File                     | Change                                                                                                                                                       |
|--------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `ApplicationConfig.java` | Remove `@ImportResource` for `common-config.xml`, `module-config.xml`, `it-module-cxf-servlet.xml`; ensure `@ComponentScan` includes `se.inera.intyg.common` |

---

## Appendix: Intygstjänst Step 9.8 Overlap Analysis

Steps 9.7 and 9.8 in the intygstjänst migration plan converted CXF endpoints and clients from `application-context-ws.xml`. The
`it-module-cxf-servlet.xml` files in the common library declare **additional** CXF endpoints that are specific to the fk7263 and ts-diabetes
modules. These are NOT the same endpoints as in `application-context-ws.xml`.

**Key difference:**

- `application-context-ws.xml` endpoints are declared in the intygstjänst app itself
- `it-module-cxf-servlet.xml` endpoints are declared in the common library JARs and loaded via `classpath*:`

Both sets of endpoints need to be converted. If intygstjänst's `CxfEndpointConfig.java` (from Step 9.8) already declares these endpoints,
the XML files in common are effectively dead code and can be deleted. If not, the Java config needs to be created (either in common or in
intygstjänst).

