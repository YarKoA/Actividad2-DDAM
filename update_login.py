import re

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens\LoginScreen.kt'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# Replace the current email initialization
old_email_line = 'var email by rememberSaveable { mutableStateOf(com.example.actividad2_ddam.model.Repo.usuarioActual?.correo ?: "") }'

new_email_logic = '''val sharedPref = remember { context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE) }
    var email by rememberSaveable { mutableStateOf(sharedPref.getString("last_email", "") ?: "") }'''

# We must ensure 'val context = LocalContext.current' comes before the new logic
# In LoginScreen.kt, context is defined a bit lower down:
# val context = LocalContext.current
# var isLoading by remember { mutableStateOf(false) }

# Let's move context up or just redefine it.
# It's easier to just replace the email initialization and move the context up.

content = content.replace('val context = LocalContext.current', '')

replacement = '''val context = LocalContext.current
    val sharedPref = remember { context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE) }
    var email by rememberSaveable { mutableStateOf(sharedPref.getString("last_email", "") ?: "") }'''

content = content.replace(old_email_line, replacement)

# Save the email on login click
old_login_click = '''vm.signIn(email, pass)'''
new_login_click = '''sharedPref.edit().putString("last_email", email.trim()).apply()
                        vm.signIn(email, pass)'''

content = content.replace(old_login_click, new_login_click)

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)
