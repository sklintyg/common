# JUnit 4 -> JUnit 5 Migration Progress

## Summary

| Batch | Description | Files | Status | Commit |
|---|---|---:|---|---|
| Batch 2 | @RunWith(MockitoJUnitRunner) | 59 | Done | 6a920adb2a |
| Batch 3 | @RunWith(SpringJUnit4ClassRunner/SpringRunner) | 43 | Done | db3e846cb8 |
| Step 0 | Fix assertion order + trim @MockitoSettings(LENIENT) | 59 | Done | — |
| Batch 1 | Plain JUnit 4, no @RunWith | ~79 | **To do** | — |
| Batch 4 | @RunWith(Parameterized) | 11 | **To do** | — |
| Batch 5 | Remove JUnit 4 build deps | — | **To do** | — |

Note: Batches were executed out of original plan order (2 and 3 before 1).

---

## Batch 2 — Done (commit 6a920adb2a)

**59 files** with `@RunWith(MockitoJUnitRunner.class)` migrated to JUnit 5.

Changes applied:
- `@RunWith(MockitoJUnitRunner.class)` → `@ExtendWith(MockitoExtension.class)`
- Runner imports updated
- `@Before` → `@BeforeEach`
- JUnit 4 Test/Assert imports → JUnit 5 equivalents
- `@Test(expected=X.class)` → `assertThrows(X.class, () -> {...})` in 29 files
- Message-first assertion order fixed
- `junit.framework.TestCase` static imports → JUnit 5 equivalents (3 tstrk1062 files)
- `assertThat` import fixed in CustomObjectMapperTest (use `MatcherAssert.assertThat`)

**Post-commit fixes (see Step 0):**
- Wrong-order assertions corrected in UtlatandeToIntygTest.java
- `@MockitoSettings(LENIENT)` trimmed to only 22 files that genuinely need it

---

## Batch 3 — Done (commit db3e846cb8)

**43 files** with `@RunWith(SpringJUnit4ClassRunner.class)` or
`@RunWith(SpringRunner.class)` migrated to JUnit 5.

Changes applied:
- Runner annotation → `@ExtendWith(SpringExtension.class)`
- Runner imports updated
- `@Before`/`@BeforeClass` → `@BeforeEach`/`@BeforeAll`
- JUnit 4 Test/Assert imports → JUnit 5 equivalents
- `@Test(expected=X.class)` → `assertThrows(X.class, () -> {...})` in 3 files
  (FkParentModuleApiTest, ts-diabetes v2 and v3 WebcertModelFactoryTest)
- Message-first assertion order fixed (compiler-guided for some files)

---

## Step 0 — Done: Fix assertion order + trim @MockitoSettings(LENIENT)

Two fixes applied to Batch 2 files, all tests pass:

**1. Wrong-order assertions fixed in UtlatandeToIntygTest.java (tstrk1062)**
- File still used `import static junit.framework.TestCase.*` — replaced with JUnit 5 Assertions imports
- ~61 assertions had JUnit 4 message-first order: `assertEquals("msg", expected, actual)`
- Fixed to JUnit 5 message-last order: `assertEquals(expected, actual, "msg")`
- Affected: single-line and multi-line forms across all private helper assert methods

**2. @MockitoSettings(LENIENT) trimmed from 58 → 22 files**
- Removed from all 58 files, ran tests, re-added only where `UnnecessaryStubbingException` fired
- 22 files genuinely need LENIENT (listed below); 36 files were cleaned
- Files that keep @MockitoSettings(strictness = Strictness.LENIENT):
  - fk/fk-parent: InternalToTransportUtilTest, ValidatorUtilFKTest
  - fk/lisjp: WebcertModelFactoryTest, InternalDraftValidatorTest
  - fk/luae_fs: WebcertModelFactoryTest, InternalDraftValidatorTest
  - fk/luae_na: WebcertModelFactoryTest, InternalDraftValidatorTest
  - fk/luse: WebcertModelFactoryTest
  - fk7263: Fk7263ModelCompareUtilTest
  - af/af00213: WebcertModelFactoryTest
  - af/af00251: WebcertModelFactoryTest
  - skl/ag114: WebcertModelFactoryTest
  - skl/ag7804: WebcertModelFactoryTest, InternalDraftValidatorTest
  - sos/db: WebcertModelFactoryTest
  - sos/doi: WebcertModelFactoryTest
  - support: IntygModuleRegistryImplTest
  - ts/ts-bas: v6 WebcertModelFactoryTest, v7 WebcertModelFactoryTest
  - ts/tstrk1009: WebcertModelFactoryTest
  - ts/tstrk1062: WebCertModelFactoryImplTest

---

## Batch 1 — To do: Plain JUnit 4, no @RunWith (~79 files)

Files identified (as of Batch 3 completion):

```
af/af00213  - SchematronValidatorTest
af/af00251  - SchematronValidatorTest (x2)
fk/fk-parent - CertificateStateHolderConverterTest, TransportToInternalUtilTest
fk/lisjp    - PrefillHandlerTest, PrefillUtilsTest, SchematronValidatorTest,
              BaseLisjpPdfDefinitionBuilderTest
fk/luae_fs  - SchematronValidatorTest
fk/luae_na  - SchematronValidatorTest
fk/luse     - SchematronValidatorTest, InternalDraftValidatorTest
fk7263      - InternalToTransportConverterTest, WebcertModelFactoryTest,
              ConverterUtilTest, PdfDefaultGeneratorTest, and 5 more
schemas     - CertificateStateHolderConverterTest, ModelConverterTest
skl/ag114   - SchematronValidatorTest
skl/ag7804  - SchematronValidatorTest
sos/db      - TransportToInternalTest, SchematronValidatorTest
sos/doi     - TransportToInternalTest, SchematronValidatorTest
support     - DiagnoskodverkTest, InternalDateTest, InternalLocalDateIntervalTest,
              StatusKodTest, WebcertModelFactoryUtilTest, PatientDetailResolveOrderTest,
              PersonnummerCommonTest, DaoUtilTest, MedicalCertificatesStoreTest,
              HsaIdValidatorTest, PatientValidatorTest, PersonnummerValidatorTest,
              SamordningsnummerValidatorTest, SimpleHsaIdValidatorTest,
              StringValidatorTest, ValidatorUtilTest, XmlMarshallerHelperTest
ts/ts-bas   - TransportToInternalTest (v6, v7), TsBasMetaDataConverterTest (v6, v7),
              DomainTransportModelValidatorTest, InternalValidatorTest (v6, v7),
              transformation tests (v6: 6 files), SchematronValidatorTest (v6, v7)
ts/ts-diabetes - ScenarioTest, DomainTransportModelValidatorTest, InternalValidatorTest,
                 TransportValidatorTest, TSDiabetesTransformerTest,
                 TsDiabetesTransformerXpathTest, TSDiabetesCertificateMetaTypeConverterTest
ts/ts-parent - EnumSetSerializerDeserializerTest
ts/tstrk1009 - Tstrk1009MetaDataConverterTest, UtlatandeToIntygTest,
               SchematronValidatorTest
               NOTE: Tstrk1009ModuleApiTest has a commented-out
               @RunWith line and all test methods commented out.
               Do NOT migrate - leave in current state.
ts/tstrk1062 - TransportToInternalTest, SchematronValidatorTest, PDFGeneratorTest
util         - CustomLocalDateDeserializerTest, InternalDateDeserializerTest,
               PartialDateAdapterTest
```

---

## Batch 4 — To do: Parameterized tests (11 files)

```
fk/lisjp    - InternalValidatorResultMatchesSchematronValidatorTest
fk/luae_fs  - InternalValidatorResultMatchesSchematronValidatorTest
fk/luae_na  - InternalValidatorResultMatchesSchematronValidatorTest
fk/luse     - InternalValidatorResultMatchesSchematronValidatorTest
skl/ag114   - InternalValidatorResultMatchesSchematronValidatorTest
skl/ag7804  - InternalValidatorResultMatchesSchematronValidatorTest
sos/db      - InternalValidatorResultMatchesSchematronValidatorTest
sos/doi     - InternalValidatorResultMatchesSchematronValidatorTest
ts/ts-bas   - InternalValidatorResultMatchesSchematronValidatorTest (v6)
ts/ts-bas   - InternalValidatorResultMatchesSchematronValidatorTest (v7)
ts/tstrk1009 - InternalValidatorResultMatchesSchematronValidatorTest
```

---

## Batch 5 — To do: Remove JUnit 4 deps

Lines to remove from root `build.gradle` allprojects block:
```groovy
testImplementation "junit:junit"
testRuntimeOnly "org.junit.vintage:junit-vintage-engine"
```
