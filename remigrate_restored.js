/**
 * Re-apply JUnit 4 -> JUnit 5 migration to the 10 restored files
 * with proper UTF-8 encoding.
 */

const fs = require('fs');
const path = require('path');

// The 10 restored files that need re-migration
const FILES = [
  'af/af00213/src/test/java/se/inera/intyg/common/af00213/v1/model/converter/InternalToTransportTest.java',
  'fk/fk-parent/src/test/java/se/inera/intyg/common/fkparent/rest/FkParentModuleApiTest.java',
  'fk7263/src/test/java/se/inera/intyg/common/fk7263/model/converter/WebcertModelFactoryTest.java',
  'support/src/test/java/se/inera/intyg/common/support/model/InternalDateTest.java',
  'ts/ts-bas/src/test/java/se/inera/intyg/common/ts_bas/v6/model/converter/util/WebcertModelFactoryTest.java',
  'ts/ts-bas/src/test/java/se/inera/intyg/common/ts_bas/v7/model/converter/util/WebcertModelFactoryTest.java',
  'ts/ts-diabetes/src/test/java/se/inera/intyg/common/ts_diabetes/v2/model/converter/WebcertModelFactoryTest.java',
  'ts/tstrk1009/src/test/java/se/inera/intyg/common/tstrk1009/v1/model/converter/WebcertModelFactoryTest.java',
  'ts/tstrk1062/src/test/java/se/inera/intyg/common/tstrk1062/v1/model/converter/WebCertModelFactoryImplTest.java',
  'ts/tstrk1062/src/test/java/se/inera/intyg/common/tstrk1062/v1/rest/TsTrk1062ModuleApiV1Test.java',
];

const ROOT = process.argv[2] || process.cwd();

/**
 * Parenthesis-balanced argument splitter.
 * Returns array of argument strings, or null if depth goes negative.
 */
function splitArgs(argsStr) {
  const args = [];
  let depth = 0;
  let current = '';
  let inString = false;
  let escape = false;

  for (let i = 0; i < argsStr.length; i++) {
    const ch = argsStr[i];
    if (escape) {
      current += ch;
      escape = false;
      continue;
    }
    if (inString) {
      current += ch;
      if (ch === '\\') escape = true;
      else if (ch === '"') inString = false;
    } else if (ch === '"') {
      inString = true;
      current += ch;
    } else if (ch === '(' || ch === '[' || ch === '{') {
      depth++;
      current += ch;
    } else if (ch === ')' || ch === ']' || ch === '}') {
      if (depth === 0) return null; // unbalanced
      depth--;
      current += ch;
    } else if (ch === ',' && depth === 0) {
      args.push(current);
      current = '';
    } else {
      current += ch;
    }
  }
  if (current !== '' || args.length > 0) args.push(current);
  return args;
}

/**
 * Fix message-first assertion calls in Java source.
 * Rewrites:
 *   assertTrue(A, B)       -> assertTrue(B, A)
 *   assertFalse(A, B)      -> assertFalse(B, A)
 *   assertNull(A, B)       -> assertNull(B, A)
 *   assertNotNull(A, B)    -> assertNotNull(B, A)
 *   assertEquals(A, B, C)  -> assertEquals(B, C, A)
 */
function fixMessageFirstAssertions(source) {
  const methods2 = ['assertTrue', 'assertFalse', 'assertNull', 'assertNotNull'];
  const methods3 = ['assertEquals'];
  const allMethods = [...methods2, ...methods3];
  
  let result = '';
  let i = 0;
  
  while (i < source.length) {
    // Check if current position starts one of the method names followed by '('
    let matched = false;
    for (const method of allMethods) {
      if (!source.startsWith(method + '(', i) && !source.startsWith(method + ' (', i)) continue;
      // Check it's a word boundary (prev char not a word char)
      if (i > 0 && /\w/.test(source[i - 1])) break;
      
      const parenStart = source.indexOf('(', i + method.length);
      if (parenStart === -1 || parenStart > i + method.length + 5) break;
      
      // Find matching close paren using balanced parsing
      let depth = 1;
      let j = parenStart + 1;
      let inStr = false;
      let esc = false;
      
      while (j < source.length && depth > 0) {
        const ch = source[j];
        if (esc) { esc = false; }
        else if (inStr) {
          if (ch === '\\') esc = true;
          else if (ch === '"') inStr = false;
        } else if (ch === '"') { inStr = true; }
        else if (ch === '(' || ch === '[' || ch === '{') depth++;
        else if (ch === ')' || ch === ']' || ch === '}') depth--;
        j++;
      }
      if (depth !== 0) break; // unbalanced
      
      const closeParenIdx = j - 1;
      const argsStr = source.substring(parenStart + 1, closeParenIdx);
      const args = splitArgs(argsStr);
      
      if (!args) break;
      
      let newArgsStr = null;
      if (methods2.includes(method) && args.length === 2) {
        // Swap: (msg, cond) -> (cond, msg)
        newArgsStr = args[1] + ',' + args[0];
      } else if (methods3.includes(method) && args.length === 3) {
        // Rotate: (msg, exp, act) -> (exp, act, msg)
        newArgsStr = args[1] + ',' + args[2] + ',' + args[0];
      }
      
      if (newArgsStr !== null) {
        result += source.substring(i, parenStart + 1);
        result += newArgsStr;
        result += ')'; // re-add the closing paren
        i = closeParenIdx + 1; // skip past the ')'
        matched = false; // already incremented, don't increment again
      }
      break;
    }
    
    if (!matched) {
      result += source[i];
    }
    i++;
  }
  
  return result;
}

/**
 * Apply all JUnit 4 -> JUnit 5 import/annotation migrations.
 */
function migrateFile(content) {
  let result = content;
  
  // Import replacements
  result = result.replace(/import org\.junit\.Test;/g, 'import org.junit.jupiter.api.Test;');
  result = result.replace(/import org\.junit\.Before;/g, 'import org.junit.jupiter.api.BeforeEach;');
  result = result.replace(/import static org\.junit\.Assert\./g, 'import static org.junit.jupiter.api.Assertions.');
  result = result.replace(/import org\.junit\.Assert;/g, 'import org.junit.jupiter.api.Assertions;');
  
  // Also fix assertThat from JUnit 4 if it ended up as Assertions.assertThat
  result = result.replace(
    /import static org\.junit\.jupiter\.api\.Assertions\.assertThat;/g,
    'import static org.hamcrest.MatcherAssert.assertThat;'
  );
  
  // Annotation replacements
  result = result.replace(/@Before(\s)/g, '@BeforeEach$1');
  
  // Fix message-first assertions
  result = fixMessageFirstAssertions(result);
  
  // Fix @Test(expected = ...) patterns
  // This converts @Test(expected = SomeException.class) + method body
  // to @Test + assertThrows
  result = fixTestExpected(result);
  
  return result;
}

/**
 * Convert @Test(expected = ExcClass.class) methods to assertThrows.
 */
function fixTestExpected(source) {
  // Pattern: @Test(expected = SomeException.class) followed by method
  const testExpectedRe = /@Test\s*\(\s*expected\s*=\s*([\w.]+\.class)\s*\)/g;
  let match;
  let result = source;
  let offset = 0;
  
  while ((match = testExpectedRe.exec(source)) !== null) {
    const matchStart = match.index + offset;
    const matchEnd = matchStart + match[0].length;
    const excClass = match[1];
    
    // Find the method signature: skip whitespace/annotations, find { 
    let p = matchEnd;
    // Skip whitespace
    while (p < result.length && /\s/.test(result[p])) p++;
    
    // Find the opening brace of the method
    let braceOpen = result.indexOf('{', p);
    if (braceOpen === -1) continue;
    
    // Get the method signature (between matchEnd and braceOpen)
    let methodSig = result.substring(matchEnd, braceOpen);
    
    // Remove 'throws XxxException' from method signature
    // Keep the signature but remove throws clause (assertThrows handles the exception)
    methodSig = methodSig.replace(/\s+throws\s+[\w,\s]+/, '');
    
    // Find the matching close brace
    let depth2 = 1;
    let k = braceOpen + 1;
    while (k < result.length && depth2 > 0) {
      const ch = result[k];
      if (ch === '{') depth2++;
      else if (ch === '}') depth2--;
      k++;
    }
    if (depth2 !== 0) continue;
    
    const braceClose = k - 1;
    const body = result.substring(braceOpen + 1, braceClose);
    
    // Indent the body an extra 4 spaces for the lambda
    const indentedBody = body.replace(/\n/g, '\n    ');
    
    const newCode = `@Test${methodSig}{
    assertThrows(${excClass}, () -> {${indentedBody}    });
  }`;
    
    const before = result.substring(0, matchStart);
    const after = result.substring(braceClose + 1);
    const oldLen = braceClose + 1 - matchStart;
    
    result = before + newCode + after;
    offset += newCode.length - oldLen;
    testExpectedRe.lastIndex = matchStart + newCode.length - offset;
  }
  
  // Ensure assertThrows is imported
  if (result.includes('assertThrows(') && !result.includes('import static org.junit.jupiter.api.Assertions.assertThrows') && !result.includes('import static org.junit.jupiter.api.Assertions.*;')) {
    // Add assertThrows import after any existing Assertions imports
    result = result.replace(
      /(import static org\.junit\.jupiter\.api\.Assertions\.[^;]+;)/,
      '$1\nimport static org.junit.jupiter.api.Assertions.assertThrows;'
    );
  }
  
  return result;
}

// Process each file
for (const relPath of FILES) {
  const fullPath = path.join(ROOT, relPath.replace(/\//g, path.sep));
  if (!fs.existsSync(fullPath)) {
    console.log('SKIP (not found):', relPath);
    continue;
  }
  
  const original = fs.readFileSync(fullPath, 'utf8');
  const migrated = migrateFile(original);
  
  if (migrated !== original) {
    fs.writeFileSync(fullPath, migrated, 'utf8');
    console.log('Migrated:', relPath);
  } else {
    console.log('No changes:', relPath);
  }
}

console.log('\nDone processing restored files.');
