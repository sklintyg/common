/**
 * Fix message-first assertions in specific files only.
 * Usage: node fix_msg_first_targeted.js <file1> <file2> ...
 */

const fs = require('fs');
const path = require('path');

function splitArgs(argsStr) {
  const args = [];
  let depth = 0, current = '', inString = false, escape = false, inChar = false;
  for (let i = 0; i < argsStr.length; i++) {
    const ch = argsStr[i];
    if (escape) { current += ch; escape = false; continue; }
    if (inString) { current += ch; if (ch === '\\') escape = true; else if (ch === '"') inString = false; }
    else if (inChar) { current += ch; if (ch === '\\') escape = true; else if (ch === "'") inChar = false; }
    else if (ch === '"') { inString = true; current += ch; }
    else if (ch === "'") { inChar = true; current += ch; }
    else if (ch === '(' || ch === '[' || ch === '{') { depth++; current += ch; }
    else if (ch === ')' || ch === ']' || ch === '}') { if (depth === 0) return null; depth--; current += ch; }
    else if (ch === ',' && depth === 0) { args.push(current); current = ''; }
    else { current += ch; }
  }
  if (current !== '' || args.length > 0) args.push(current);
  return args;
}

function fixMessageFirstAssertions(source) {
  const methods2 = ['assertTrue', 'assertFalse', 'assertNull', 'assertNotNull'];
  const methods3 = ['assertEquals'];
  const allMethods = [...methods2, ...methods3];
  let result = '', i = 0;
  while (i < source.length) {
    let handled = false;
    for (const method of allMethods) {
      if (!source.startsWith(method + '(', i)) continue;
      if (i > 0 && (/\w/.test(source[i - 1]) || source[i - 1] === '.')) break;
      const parenStart = i + method.length;
      if (source[parenStart] !== '(') break;
      let depth = 1, j = parenStart + 1, inStr = false, esc = false, inChr = false;
      while (j < source.length && depth > 0) {
        const ch = source[j];
        if (esc) { esc = false; }
        else if (inStr) { if (ch === '\\') esc = true; else if (ch === '"') inStr = false; }
        else if (inChr) { if (ch === '\\') esc = true; else if (ch === "'") inChr = false; }
        else if (ch === '"') inStr = true;
        else if (ch === "'") inChr = true;
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
      if (methods2.includes(method) && args.length === 2) { newArgsStr = args[1] + ',' + args[0]; }
      else if (methods3.includes(method) && args.length === 3) { newArgsStr = args[1] + ',' + args[2] + ',' + args[0]; }
      if (newArgsStr !== null) {
        result += source.substring(i, parenStart + 1);
        result += newArgsStr;
        result += ')';
        i = closeParen + 1;
        handled = true;
      }
      break;
    }
    if (!handled) { result += source[i]; i++; }
  }
  return result;
}

// Process files passed as arguments
const files = process.argv.slice(2);
let changed = 0;
for (const filePath of files) {
  if (!fs.existsSync(filePath)) { console.log('NOT FOUND:', filePath); continue; }
  const content = fs.readFileSync(filePath, 'utf8');
  const fixed = fixMessageFirstAssertions(content);
  if (fixed !== content) {
    fs.writeFileSync(filePath, fixed, 'utf8');
    console.log('Fixed:', path.basename(filePath));
    changed++;
  } else {
    console.log('No change:', path.basename(filePath));
  }
}
console.log(`Fixed ${changed} of ${files.length} files.`);
