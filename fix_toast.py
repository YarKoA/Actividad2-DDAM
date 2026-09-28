import re

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens\RegisterScreen.kt'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('FirebaseAuth.getInstance().signOut()\n                \n                onRegistered()', 'FirebaseAuth.getInstance().signOut()\n                Toast.makeText(context, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()\n                onRegistered()')

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)
