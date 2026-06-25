/**
 * Convert @Test(expected = SomeException.class) to assertThrows in JUnit 5.
 * Usage: node fix_expected.js [file1 file2 ...]
 * If no files given, scans the whole directory tree.
 */

const fs = require('fs');
const path = require('path');
const ROOT = process.cwd();

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

function fixExpected(source) {
  // Match @Test(expected = SomeException.class) or @Test(expected=SomeException.class)
  const annotationRe = /@Test\s*\(\s*expected\s*=\s*([\w.]+)\.class\s*\)/g;

  if (!annotationRe.test(source)) return source;
  annotationRe.lastIndex = 0;

  let result = source;
  let match;

  // Process from end to start to preserve offsets
  const matches = [];
  while ((match = annotationRe.exec(source)) !== null) {
    matches.push({ start: match.index, end: match.index + match[0].length, exClass: match[1] });
  }

  // For each match, find the method body and wrap it with assertThrows
  // Process in reverse order
  for (let i = matches.length - 1; i >= 0; i--) {
    const { start, end, exClass } = matches[i];

    // Find the method signature after the annotation
    // Look for 'public void ...' or 'void ...' pattern after the annotation
    let methodStart = end;
    // skip whitespace/newlines
    while (methodStart < result.length && /[\s]/.test(result[methodStart])) methodStart++;

    // Find opening brace of method body
    let braceStart = result.indexOf('{', methodStart);
    if (braceStart === -1) continue;

    // Find matching closing brace
    let depth = 1;
    let j = braceStart + 1;
    let inStr = false, esc = false, inChr = false;
    while (j < result.length && depth > 0) {
      const ch = result[j];
      if (esc) { esc = false; }
      else if (inStr) { if (ch === '\\') esc = true; else if (ch === '"') inStr = false; }
      else if (inChr) { if (ch === '\\') esc = true; else if (ch === "'") inChr = false; }
      else if (ch === '"') inStr = true;
      else if (ch === "'") inChr = true;
      else if (ch === '{') depth++;
      else if (ch === '}') depth--;
      j++;
    }
    const braceEnd = j - 1; // position of closing brace

    // Extract method body (between { and })
    const bodyContent = result.substring(braceStart + 1, braceEnd);

    // Build new method body
    const indent = '    '; // 4 spaces
    const newBody = `{\n${indent}    assertThrows(${exClass}.class, () -> {${bodyContent}});\n${indent}}`;

    // Replace @Test(expected=...) with @Test
    // and replace method body with assertThrows-wrapped version
    result = result.substring(0, start) + '@Test' + result.substring(end, braceStart) + newBody + result.substring(braceEnd + 1);
  }

  // Add assertThrows import if not present
  if (result.includes('assertThrows') && !result.includes('import static org.junit.jupiter.api.Assertions.assertThrows') && !result.includes('import static org.junit.jupiter.api.Assertions.*')) {
    // Find the last Assertions import and add after it
    const assertImportRe = /import static org\.junit\.jupiter\.api\.Assertions\.\w+;/g;
    let lastImportMatch = null;
    let m;
    while ((m = assertImportRe.exec(result)) !== null) {
      lastImportMatch = m;
    }
    if (lastImportMatch) {
      const insertPos = lastImportMatch.index + lastImportMatch[0].length;
      result = result.substring(0, insertPos) + '\nimport static org.junit.jupiter.api.Assertions.assertThrows;' + result.substring(insertPos);
    }
  }

  return result;
}

const files = process.argv.slice(2);
let changed = 0;

function processFile(filePath) {
  if (!fs.existsSync(filePath)) { console.log('NOT FOUND:', filePath); return; }
  const content = fs.readFileSync(filePath, 'utf8');
  if (!content.includes('@Test(expected') && !content.includes('@Test( expected')) return;
  const fixed = fixExpected(content);
  if (fixed !== content) {
    fs.writeFileSync(filePath, fixed, 'utf8');
    console.log('Fixed:', path.basename(filePath));
    changed++;
  } else {
    console.log('No change:', path.basename(filePath));
  }
}

if (files.length > 0) {
  files.forEach(processFile);
  console.log(`Fixed ${changed} of ${files.length} files.`);
} else {
  walkDir(ROOT, (filePath) => {
    const content = fs.readFileSync(filePath, 'utf8');
    if (!content.includes('@Test(expected') && !content.includes('@Test( expected')) return;
    const fixed = fixExpected(content);
    if (fixed !== content) {
      fs.writeFileSync(filePath, fixed, 'utf8');
      console.log('Fixed:', path.relative(ROOT, filePath));
      changed++;
    }
  });
  console.log(`Total fixed: ${changed}`);
}
