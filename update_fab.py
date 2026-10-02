import re

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens\EventListScreen.kt'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# Replace bottom = 100.dp with bottom = 130.dp
content = content.replace('padding(start = 24.dp, bottom = 100.dp)', 'padding(start = 24.dp, bottom = 130.dp)')

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)
print("Updated EventListScreen.kt padding")
