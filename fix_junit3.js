/**
 * Fix junit.framework.TestCase imports and message-first assertions.
 * Also handles any remaining JUnit 4 patterns in all test files.
 */
const fs = require('fs');
const path = require('path');

const ROOT = process.argv[2] || process.cwd();

// Files that are already processed (args swapped) — need import fix ONLY
const IMPORT_FIX_ONLY = [
  'ts/tstrk1062/src/test/java/se/inera/intyg/common/tstrk1062/v1/model/converter/WebCertModelFactoryImplTest.java',
  'ts/tstrk1062/src/test/java/se/inera/intyg/common/tstrk1062/v1/rest/TsTrk1062ModuleApiV1Test.java',
];

// Files still in original JUnit 4 state — need import fix AND arg swap
const IMPORT_AND_SWAP = [
  'sos/db/src/test/java/se/inera/intyg/common/db/v1/model/converter/UtlatandeToIntygTest.java',
  'ts/tstrk1062/src/test/java/se/inera/intyg/common/tstrk1062/support/TsTrk1062EntryPointTest.java',
  'ts/tstrk1062/src/test/java/se/inera/intyg/common/tstrk1062/v1/model/converter/TransportToInternalTest.java',
  'ts/tstrk1062/src/test/java/se/inera/intyg/common/tstrk1062/v1/model/converter/UtlatandeToIntygTest.java',
  'ts/tstrk1062/src/test/java/se/inera/intyg/common/tstrk1062/v1/pdf/PDFGeneratorTest.java',
];

function splitArgs(argsStr) {
  const args = [];
  let depth = 0;
  let current = '';
  let inString = false;
  let escape = false;

  for (let i = 0; i < argsStr.length; i++) {
    const ch = argsStr[i];
    if (escape) { current += ch; escape = false; continue; }
    if (inString) {
      current += ch;
      if (ch === '\\') escape = true;
      else if (ch === '"') inString = false;
    } else if (ch === '"') { inString = true; current += ch; }
    else if (ch === '(' || ch === '[' || ch === '{') { depth++; current += ch; }
    else if (ch === ')' || ch === ']' || ch === '}') {
      if (depth === 0) return null;
      depth--; current += ch;
    } else if (ch === ',' && depth === 0) { args.push(current); current = ''; }
    else { current += ch; }
  }
  if (current !== '' || args.length > 0) args.push(current);
  return args;
}

function fixMessageFirstAssertions(source) {
  const methods2 = ['assertTrue', 'assertFalse', 'assertNull', 'assertNotNull'];
  const methods3 = ['assertEquals'];
  const allMethods = [...methods2, ...methods3];
  
  let result = '';
  let i = 0;
  
  while (i < source.length) {
    let matched = false;
    for (const method of allMethods) {
      const prefix = method + '(';
      if (!source.startsWith(prefix, i)) continue;
      if (i > 0 && /\w/.test(source[i - 1])) break;
      
      const parenStart = i + method.length;
      if (source[parenStart] !== '(') break;
      
      let depth = 1, j = parenStart + 1, inStr = false, esc = false;
      while (j < source.length && depth > 0) {
        const ch = source[j];
        if (esc) { esc = false; }
        else if (inStr) { if (ch === '\\') esc = true; else if (ch === '"') inStr = false; }
        else if (ch === '"') inStr = true;
        else if (ch === '(' || ch === '[' || ch === '{') depth++;
        else if (ch === ')' || ch === ']' || ch === '}') depth--;
        j++;
      }
      if (depth !== 0) break;
      
      const closeParen = j - 1;
      const argsStr = source.substring(parenStart + 1, closeParen);
      const args = splitArgs(argsStr);
      if (!args) break;
      
      let newArgsStr = null;
      if (methods2.includes(method) && args.length === 2) {
        newArgsStr = args[1] + ',' + args[0];
      } else if (methods3.includes(method) && args.length === 3) {
        newArgsStr = args[1] + ',' + args[2] + ',' + args[0];
      }
      
      if (newArgsStr !== null) {
        result += source.substring(i, parenStart + 1);
        result += newArgsStr;
        result += ')';
        i = closeParen + 1;
        matched = false; // already advanced i
      }
      break;
    }
    
    if (!matched) {
      result += source[i];
      i++;
    }
  }
  
  return result;
}

function fixImports(content) {
  return content.replace(
    /import static junit\.framework\.TestCase\.(assertNotNull|assertNull|assertEquals|assertTrue|assertFalse|assertSame|fail);/g,
    'import static org.junit.jupiter.api.Assertions.$1;'
  ).replace(
    /import junit\.framework\.TestCase;/g,
    '// removed: import junit.framework.TestCase;'
  );
}

// Fix imports only (args already in JUnit 5 order)
for (const relPath of IMPORT_FIX_ONLY) {
  const fullPath = path.join(ROOT, relPath.replace(/\//g, path.sep));
  if (!fs.existsSync(fullPath)) { console.log('SKIP:', relPath); continue; }
  let content = fs.readFileSync(fullPath, 'utf8');
  const fixed = fixImports(content);
  fs.writeFileSync(fullPath, fixed, 'utf8');
  console.log('Import-fixed:', relPath);
}

// Fix imports + swap args (still in original JUnit 4 order)
for (const relPath of IMPORT_AND_SWAP) {
  const fullPath = path.join(ROOT, relPath.replace(/\//g, path.sep));
  if (!fs.existsSync(fullPath)) { console.log('SKIP:', relPath); continue; }
  let content = fs.readFileSync(fullPath, 'utf8');
  
  // First do basic JUnit 4 -> JUnit 5 import/annotation migration
  content = content.replace(/import org\.junit\.Test;/g, 'import org.junit.jupiter.api.Test;');
  content = content.replace(/import org\.junit\.Before;/g, 'import org.junit.jupiter.api.BeforeEach;');
  content = content.replace(/import static org\.junit\.Assert\./g, 'import static org.junit.jupiter.api.Assertions.');
  content = content.replace(/import org\.junit\.Assert;/g, 'import org.junit.jupiter.api.Assertions;');
  content = content.replace(/@Before(\s)/g, '@BeforeEach$1');
  
  // Fix junit.framework.TestCase imports
  content = fixImports(content);
  
  // Swap message-first assertions
  content = fixMessageFirstAssertions(content);
  
  fs.writeFileSync(fullPath, content, 'utf8');
  console.log('Import+swap fixed:', relPath);
}

console.log('\nDone.');
