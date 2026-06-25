/**
 * Fix @BeforeClass / @AfterClass in files that already have JUnit 5 @Test imports.
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

let changed = 0;
walkDir(ROOT, (filePath) => {
  const content = fs.readFileSync(filePath, 'utf8');
  
  // Only fix files that have JUnit 5 @Test imports AND JUnit 4 @BeforeClass
  if (!content.includes('import org.junit.jupiter.api.Test;')) return;
  if (!content.includes('import org.junit.BeforeClass;') && 
      !content.includes('import org.junit.AfterClass;')) return;
  
  let fixed = content;
  // Import replacements
  fixed = fixed.replace(/import org\.junit\.BeforeClass;/g, 'import org.junit.jupiter.api.BeforeAll;');
  fixed = fixed.replace(/import org\.junit\.AfterClass;/g, 'import org.junit.jupiter.api.AfterAll;');
  
  // Annotation replacements (be careful not to match @BeforeEach when replacing @Before)
  fixed = fixed.replace(/@BeforeClass(\s)/g, '@BeforeAll$1');
  fixed = fixed.replace(/@AfterClass(\s)/g, '@AfterAll$1');
  
  if (fixed !== content) {
    fs.writeFileSync(filePath, fixed, 'utf8');
    console.log('Fixed:', path.relative(ROOT, filePath));
    changed++;
  }
});

console.log(`\nFixed ${changed} files.`);
