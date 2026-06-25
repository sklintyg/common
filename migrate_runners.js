/**
 * Complete runner migration for the partially-migrated restored files.
 * These files already have JUnit 5 @Test but still have @RunWith.
 */

const fs = require('fs');
const path = require('path');
const ROOT = process.argv[2] || process.cwd();

const SPRING_RUNNER_FILES = [
  'af/af00213/src/test/java/se/inera/intyg/common/af00213/v1/model/converter/InternalToTransportTest.java',
  'fk/fk-parent/src/test/java/se/inera/intyg/common/fkparent/rest/FkParentModuleApiTest.java',
  'ts/ts-diabetes/src/test/java/se/inera/intyg/common/ts_diabetes/v2/model/converter/WebcertModelFactoryTest.java',
];

const MOCKITO_RUNNER_FILES = [
  'ts/ts-bas/src/test/java/se/inera/intyg/common/ts_bas/v6/model/converter/util/WebcertModelFactoryTest.java',
  'ts/ts-bas/src/test/java/se/inera/intyg/common/ts_bas/v7/model/converter/util/WebcertModelFactoryTest.java',
  'ts/tstrk1009/src/test/java/se/inera/intyg/common/tstrk1009/v1/model/converter/WebcertModelFactoryTest.java',
  'ts/tstrk1062/src/test/java/se/inera/intyg/common/tstrk1062/v1/model/converter/WebCertModelFactoryImplTest.java',
  'ts/tstrk1062/src/test/java/se/inera/intyg/common/tstrk1062/v1/rest/TsTrk1062ModuleApiV1Test.java',
];

function migrateSpringRunner(content) {
  // Replace runner annotation and imports
  content = content.replace(/@RunWith\(SpringJUnit4ClassRunner\.class\)/g, '@ExtendWith(SpringExtension.class)');
  content = content.replace(/@RunWith\(SpringRunner\.class\)/g, '@ExtendWith(SpringExtension.class)');
  content = content.replace(
    /import org\.junit\.runner\.RunWith;/g,
    'import org.junit.jupiter.api.extension.ExtendWith;'
  );
  content = content.replace(
    /import org\.springframework\.test\.context\.junit4\.SpringJUnit4ClassRunner;/g,
    'import org.springframework.test.context.junit.jupiter.SpringExtension;'
  );
  content = content.replace(
    /import org\.springframework\.test\.context\.junit4\.SpringRunner;/g,
    'import org.springframework.test.context.junit.jupiter.SpringExtension;'
  );
  return content;
}

function migrateMockitoRunner(content) {
  // Replace runner annotation and imports
  content = content.replace(/@RunWith\(MockitoJUnitRunner\.class\)/g, '@ExtendWith(MockitoExtension.class)');
  content = content.replace(/@RunWith\(MockitoJUnitRunner\.Silent\.class\)/g, '@ExtendWith(MockitoExtension.class)');
  content = content.replace(/@RunWith\(MockitoJUnitRunner\.StrictStubs\.class\)/g, '@ExtendWith(MockitoExtension.class)');
  content = content.replace(
    /import org\.junit\.runner\.RunWith;/g,
    'import org.junit.jupiter.api.extension.ExtendWith;'
  );
  content = content.replace(
    /import org\.mockito\.junit\.MockitoJUnitRunner;/g,
    'import org.mockito.junit.jupiter.MockitoExtension;'
  );
  return content;
}

function processFile(relPath, migrateFn) {
  const fullPath = path.join(ROOT, relPath.replace(/\//g, path.sep));
  if (!fs.existsSync(fullPath)) {
    console.log('SKIP:', relPath);
    return;
  }
  const content = fs.readFileSync(fullPath, 'utf8');
  const fixed = migrateFn(content);
  if (fixed !== content) {
    fs.writeFileSync(fullPath, fixed, 'utf8');
    console.log('Fixed:', path.basename(fullPath));
  } else {
    console.log('No change:', path.basename(fullPath));
  }
}

for (const f of SPRING_RUNNER_FILES) processFile(f, migrateSpringRunner);
for (const f of MOCKITO_RUNNER_FILES) processFile(f, migrateMockitoRunner);

console.log('\nDone.');
