import re

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens\RegisterScreen.kt'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# Remove the 'val context = LocalContext.current' that is below LaunchedEffect
content = content.replace('    val context = LocalContext.current', '')

# Insert it before LaunchedEffect
content = content.replace('    LaunchedEffect(Unit) {', '    val context = LocalContext.current\n    LaunchedEffect(Unit) {')

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)
