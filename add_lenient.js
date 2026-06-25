/**
 * Add @MockitoSettings(strictness = Strictness.LENIENT) to test classes
 * that use @ExtendWith(MockitoExtension.class) and have @BeforeEach stubs
 * that might not be used in every test.
 * 
 * This matches the JUnit 4 MockitoJUnitRunner behavior where @Before stubs
 * didn't need to be used in every individual test.
 */
const fs = require('fs');
const path = require('path');
const ROOT = process.argv[2] || process.cwd();

// Files that need @MockitoSettings(strictness = Strictness.LENIENT)
const FILES = [
  'ts/ts-bas/src/test/java/se/inera/intyg/common/ts_bas/v6/model/converter/util/WebcertModelFactoryTest.java',
  'ts/ts-bas/src/test/java/se/inera/intyg/common/ts_bas/v7/model/converter/util/WebcertModelFactoryTest.java',
  'ts/tstrk1009/src/test/java/se/inera/intyg/common/tstrk1009/v1/model/converter/WebcertModelFactoryTest.java',
  'ts/tstrk1062/src/test/java/se/inera/intyg/common/tstrk1062/v1/model/converter/WebCertModelFactoryImplTest.java',
];

for (const relPath of FILES) {
  const fullPath = path.join(ROOT, relPath.replace(/\//g, path.sep));
  if (!fs.existsSync(fullPath)) { console.log('SKIP:', relPath); continue; }
  
  let content = fs.readFileSync(fullPath, 'utf8');
  
  // Skip if already has @MockitoSettings
  if (content.includes('@MockitoSettings')) { console.log('Already has @MockitoSettings:', relPath); continue; }
  
  // Add imports if not present
  if (!content.includes('import org.mockito.junit.jupiter.MockitoSettings;')) {
    content = content.replace(
      'import org.mockito.junit.jupiter.MockitoExtension;',
      'import org.mockito.junit.jupiter.MockitoExtension;\nimport org.mockito.junit.jupiter.MockitoSettings;\nimport org.mockito.quality.Strictness;'
    );
  }
  
  // Add @MockitoSettings annotation before @ExtendWith(MockitoExtension.class)
  content = content.replace(
    '@ExtendWith(MockitoExtension.class)',
    '@ExtendWith(MockitoExtension.class)\n@MockitoSettings(strictness = Strictness.LENIENT)'
  );
  
  fs.writeFileSync(fullPath, content, 'utf8');
  console.log('Fixed:', path.basename(fullPath));
}

console.log('\nDone.');
