import re

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\AndroidManifest.xml'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('android:allowBackup="true"', 'android:allowBackup="false"')

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)
