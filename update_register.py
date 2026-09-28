import re

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens\RegisterScreen.kt'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# Make sure imports are there
if 'import com.google.firebase.auth.FirebaseAuth' not in content:
    content = re.sub(r'(import [^\n]+\n)(?!.*import )', r'\1import com.google.firebase.auth.FirebaseAuth\nimport androidx.compose.ui.platform.LocalContext\n', content, count=1, flags=re.DOTALL)

# Find where context is defined
if 'val context = LocalContext.current' not in content:
    # insert it at the top of RegisterScreen
    content = content.replace('val ui by vm.ui.collectAsState()', 'val ui by vm.ui.collectAsState()\n    val context = LocalContext.current')

# Modify the LaunchedEffect for success
old_effect = '''    LaunchedEffect(Unit) {
        vm.event.collect{ event ->
            if (event is RegisterViewModel.RegisterEvent.Success){
                onRegistered()
            }
        }
    }'''

new_effect = '''    LaunchedEffect(Unit) {
        vm.event.collect{ event ->
            if (event is RegisterViewModel.RegisterEvent.Success){
                val sharedPref = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
                sharedPref.edit().putString("last_email", email.trim()).apply()
                FirebaseAuth.getInstance().signOut()
                onRegistered()
            }
        }
    }'''

content = content.replace(old_effect, new_effect)

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)
