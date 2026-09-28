import re

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\navigation\AppNavigation.kt'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# Replace the fadeOut tween for SPLASH
content = content.replace('animationSpec = tween(942)', 'animationSpec = tween(300)')

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)
