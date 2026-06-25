/**
 * Batch 2: Migrate all remaining @RunWith(MockitoJUnitRunner) files to JUnit 5.
 * 
 * Changes per file:
 * - @RunWith(MockitoJUnitRunner.class) -> @ExtendWith(MockitoExtension.class)
 * - import org.junit.runner.RunWith -> import org.junit.jupiter.api.extension.ExtendWith
 * - import org.mockito.junit.MockitoJUnitRunner -> import org.mockito.junit.jupiter.MockitoExtension
 * - import org.junit.Test -> import org.junit.jupiter.api.Test
 * - import org.junit.Before -> import org.junit.jupiter.api.BeforeEach
 * - import static org.junit.Assert.* -> import static org.junit.jupiter.api.Assertions.*
 * - @Before -> @BeforeEach
 */

const fs = require('fs');
const path = require('path');
const ROOT = process.argv[2] || process.cwd();

function walkDir(dir, callback) {
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const full = path.join(dir, entry.name);
    if (entry.isDirectory() && !['build', '.gradle', 'node_modules'].includes(entry.name)) {
      walkDir(full, callback);
    } else if (entry.isFile() && entry.name.endsWith('.java')) {
      callback(full);
    }
  }
}

let changed = 0, skipped = 0;

walkDir(ROOT, (filePath) => {
  const content = fs.readFileSync(filePath, 'utf8');
  
  if (!content.includes('@RunWith(MockitoJUnitRunner')) return;
  
  let fixed = content;
  
  // Runner annotation
  fixed = fixed.replace(/@RunWith\(MockitoJUnitRunner\.class\)/g, '@ExtendWith(MockitoExtension.class)');
  fixed = fixed.replace(/@RunWith\(MockitoJUnitRunner\.Silent\.class\)/g, '@ExtendWith(MockitoExtension.class)');
  fixed = fixed.replace(/@RunWith\(MockitoJUnitRunner\.StrictStubs\.class\)/g, '@ExtendWith(MockitoExtension.class)');
  
  // Runner imports
  fixed = fixed.replace(/import org\.junit\.runner\.RunWith;/g, 'import org.junit.jupiter.api.extension.ExtendWith;');
  fixed = fixed.replace(/import org\.mockito\.junit\.MockitoJUnitRunner;/g, 'import org.mockito.junit.jupiter.MockitoExtension;');
  
  // Test imports
  fixed = fixed.replace(/import org\.junit\.Test;/g, 'import org.junit.jupiter.api.Test;');
  fixed = fixed.replace(/import org\.junit\.Before;/g, 'import org.junit.jupiter.api.BeforeEach;');
  fixed = fixed.replace(/import org\.junit\.After;([^E])/g, 'import org.junit.jupiter.api.AfterEach;$1');
  fixed = fixed.replace(/import org\.junit\.BeforeClass;/g, 'import org.junit.jupiter.api.BeforeAll;');
  fixed = fixed.replace(/import org\.junit\.AfterClass;/g, 'import org.junit.jupiter.api.AfterAll;');
  fixed = fixed.replace(/import static org\.junit\.Assert\./g, 'import static org.junit.jupiter.api.Assertions.');
  fixed = fixed.replace(/import org\.junit\.Assert;/g, 'import org.junit.jupiter.api.Assertions;');
  
  // Annotations
  fixed = fixed.replace(/@Before(\s)/g, '@BeforeEach$1');
  fixed = fixed.replace(/@After([^E\s])/g, '@AfterEach$1'); // @After followed by non E/whitespace
  fixed = fixed.replace(/@BeforeClass(\s)/g, '@BeforeAll$1');
  fixed = fixed.replace(/@AfterClass(\s)/g, '@AfterAll$1');
  
  if (fixed !== content) {
    fs.writeFileSync(filePath, fixed, 'utf8');
    changed++;
  } else {
    skipped++;
  }
});

console.log(`Batch 2 done: ${changed} files migrated, ${skipped} unchanged.`);
