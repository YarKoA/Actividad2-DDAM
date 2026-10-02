import re

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\MainActivity.kt'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# Add import for FirebaseAuth
if 'import com.google.firebase.auth.FirebaseAuth' not in content:
    content = re.sub(r'(import [^\n]+\n)(?!.*import )', r'\1import com.google.firebase.auth.FirebaseAuth\n', content, count=1, flags=re.DOTALL)

# Add signOut on fresh launch
signout_logic = '''        if (savedInstanceState == null) {
            FirebaseAuth.getInstance().signOut()
        }'''

if 'FirebaseAuth.getInstance().signOut()' not in content:
    content = content.replace('super.onCreate(savedInstanceState)', 'super.onCreate(savedInstanceState)\n' + signout_logic)

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)
