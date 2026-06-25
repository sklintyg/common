#!/usr/bin/env node
/**
 * Batch 3: Migrate SpringJUnit4ClassRunner / SpringRunner tests to JUnit 5 SpringExtension
 *
 * Changes:
 *   @RunWith(SpringJUnit4ClassRunner.class) → @ExtendWith(SpringExtension.class)
 *   @RunWith(SpringRunner.class)            → @ExtendWith(SpringExtension.class)
 *   import org.junit.runner.RunWith         → import org.junit.jupiter.api.extension.ExtendWith
 *   import org.springframework.test.context.junit4.SpringJUnit4ClassRunner → import ...junit.jupiter.SpringExtension
 *   import org.springframework.test.context.junit4.SpringRunner             → import ...junit.jupiter.SpringExtension
 *   import org.junit.Test                   → import org.junit.jupiter.api.Test
 *   import org.junit.Before                 → import org.junit.jupiter.api.BeforeEach
 *   import org.junit.After                  → import org.junit.jupiter.api.AfterEach
 *   import org.junit.BeforeClass            → import org.junit.jupiter.api.BeforeAll
 *   import org.junit.AfterClass             → import org.junit.jupiter.api.AfterAll
 *   import static org.junit.Assert.*        → import static org.junit.jupiter.api.Assertions.*
 *   import org.junit.Assert                 → import org.junit.jupiter.api.Assertions (qualified usage)
 *   @Before void                            → @BeforeEach void
 *   @After void                             → @AfterEach void
 *   @BeforeClass                            → @BeforeAll
 *   @AfterClass                             → @AfterAll
 */

const fs = require('fs');
const path = require('path');

function findJavaFiles(dir) {
  const results = [];
  const entries = fs.readdirSync(dir, { withFileTypes: true });
  for (const entry of entries) {
    const full = path.join(dir, entry.name);
    if (entry.isDirectory()) {
      results.push(...findJavaFiles(full));
    } else if (entry.isFile() && entry.name.endsWith('.java')) {
      results.push(full);
    }
  }
  return results;
}

function migrateFile(filePath) {
  let raw = fs.readFileSync(filePath, 'utf8');
  const hasCrlf = raw.includes('\r\n');

  // Normalize to LF for regex processing
  let content = hasCrlf ? raw.replace(/\r\n/g, '\n') : raw;

  // Only process files with Spring runner
  if (!/\@RunWith\s*\(\s*(SpringJUnit4ClassRunner|SpringRunner)/.test(content)) {
    return false;
  }

  // Replace @RunWith(SpringJUnit4ClassRunner.class) or @RunWith(SpringRunner.class)
  content = content.replace(/@RunWith\s*\(\s*SpringJUnit4ClassRunner\.class\s*\)/g, '@ExtendWith(SpringExtension.class)');
  content = content.replace(/@RunWith\s*\(\s*SpringRunner\.class\s*\)/g, '@ExtendWith(SpringExtension.class)');

  // Replace runner imports
  content = content.replace(/import\s+org\.junit\.runner\.RunWith\s*;\n/g, 'import org.junit.jupiter.api.extension.ExtendWith;\n');

  // Replace SpringJUnit4ClassRunner import with SpringExtension
  content = content.replace(
    /import\s+org\.springframework\.test\.context\.junit4\.SpringJUnit4ClassRunner\s*;\n/g,
    'import org.springframework.test.context.junit.jupiter.SpringExtension;\n'
  );
  // Replace SpringRunner import with SpringExtension
  content = content.replace(
    /import\s+org\.springframework\.test\.context\.junit4\.SpringRunner\s*;\n/g,
    'import org.springframework.test.context.junit.jupiter.SpringExtension;\n'
  );

  // Deduplicate SpringExtension import if added twice
  const springExtImport = 'import org.springframework.test.context.junit.jupiter.SpringExtension;\n';
  const count = (content.match(/import org\.springframework\.test\.context\.junit\.jupiter\.SpringExtension;/g) || []).length;
  if (count > 1) {
    // Remove all, then add one
    content = content.replace(/import org\.springframework\.test\.context\.junit\.jupiter\.SpringExtension;\n/g, '');
    // Find good insertion point (after last import)
    content = content.replace(/(import [^\n]+;\n)(\n*(?:\/\*\*|@|public|class|abstract))/, '$1' + springExtImport + '$2');
  }

  // Replace JUnit 4 Test/lifecycle imports with JUnit 5
  content = content.replace(/import\s+org\.junit\.Test\s*;\n/g, 'import org.junit.jupiter.api.Test;\n');
  content = content.replace(/import\s+org\.junit\.Before\s*;\n/g, 'import org.junit.jupiter.api.BeforeEach;\n');
  content = content.replace(/import\s+org\.junit\.After\s*;\n/g, 'import org.junit.jupiter.api.AfterEach;\n');
  content = content.replace(/import\s+org\.junit\.BeforeClass\s*;\n/g, 'import org.junit.jupiter.api.BeforeAll;\n');
  content = content.replace(/import\s+org\.junit\.AfterClass\s*;\n/g, 'import org.junit.jupiter.api.AfterAll;\n');

  // Replace static Assert imports
  content = content.replace(
    /import\s+static\s+org\.junit\.Assert\.([\w*]+)\s*;\n/g,
    'import static org.junit.jupiter.api.Assertions.$1;\n'
  );
  // Replace qualified Assert import (non-static) with JUnit 5 static wildcard import
  content = content.replace(/import\s+org\.junit\.Assert\s*;\n/g, 'import static org.junit.jupiter.api.Assertions.*;\n');

  // Replace annotations on methods
  content = content.replace(/(\s)@Before(\s)/g, '$1@BeforeEach$2');
  content = content.replace(/(\s)@After(\s)/g, '$1@AfterEach$2');
  content = content.replace(/(\s)@BeforeClass(\s)/g, '$1@BeforeAll$2');
  content = content.replace(/(\s)@AfterClass(\s)/g, '$1@AfterAll$2');

  // Replace qualified Assert.X( calls (but not import lines)
  content = content.replace(/\bAssert\.(assertEquals|assertNotNull|assertNull|assertTrue|assertFalse|assertSame|assertNotSame|assertArrayEquals|assertThat|fail)\(/g, '$1(');

  // Replace @Test(expected=X.class) — convert to assertThrows
  // This is done as a separate script, but flag if found
  if (/@Test\s*\(\s*expected\s*=/.test(content)) {
    console.log(`  [WARN] Has @Test(expected=...) - needs fix_expected.js: ${filePath}`);
  }

  fs.writeFileSync(filePath, content, 'utf8');
  return true;
}

// Find all Java test files
const rootDir = process.argv[2] || '.';
const files = findJavaFiles(rootDir);
let migrated = 0;
for (const f of files) {
  if (migrateFile(f)) {
    console.log(`Migrated: ${f}`);
    migrated++;
  }
}
console.log(`\nTotal migrated: ${migrated}`);
