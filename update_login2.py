import re

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens\LoginScreen.kt'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# Find the email initialization
# var email by rememberSaveable { mutableStateOf(sharedPref.getString("last_email", "") ?: "") }

# Add a LaunchedEffect to refresh email
refresh_logic = '''
    LaunchedEffect(Unit) {
        val lastEmail = sharedPref.getString("last_email", "") ?: ""
        if (lastEmail.isNotBlank()) {
            email = lastEmail
        }
    }
'''

if 'val lastEmail = sharedPref.getString' not in content:
    content = content.replace('var pass by rememberSaveable { mutableStateOf("") }', 'var pass by rememberSaveable { mutableStateOf("") }\n' + refresh_logic)

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)
