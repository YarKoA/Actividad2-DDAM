import re
file_path = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens\EventListScreen.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('modoOverlay =\n                        true,', '')
content = content.replace('modoOverlay = true,', '')

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
