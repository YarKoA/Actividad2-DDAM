import re

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens\RegisterScreen.kt'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# Fix the LaunchedEffect
old_effect = '''    LaunchedEffect(Unit) {
        vm.event.collect{ event ->
            if (event is RegisterViewModel.RegisterEvent.Success){
                val sharedPref = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
                sharedPref.edit().putString("last_email", email.trim()).apply()
                FirebaseAuth.getInstance().signOut()
                onRegistered()
            }
        }
    }'''

new_effect = '''    val currentEmail by rememberUpdatedState(email)
    LaunchedEffect(Unit) {
        vm.event.collect{ event ->
            if (event is RegisterViewModel.RegisterEvent.Success){
                val sharedPref = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
                sharedPref.edit().putString("last_email", currentEmail.trim()).apply()
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(context, "Exito al crear cuenta", Toast.LENGTH_SHORT).show()
                onRegistered()
            }
        }
    }'''
content = content.replace(old_effect, new_effect)

# Remove the fake toast lines
content = re.sub(r'Toast\.makeText\(context,\s*".*?[Ee]xito.*?".*?\)\.show\(\)', '', content)
content = re.sub(r'Toast\.makeText\(context,\s*".*?xito.*?".*?\)\.show\(\)', '', content)

error_text = '''
                ui.error?.let { err ->
                    Text(text = err, color = Color.Red, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
                }
'''

content = re.sub(r'(Spacer\(modifier = Modifier\.height\(16\.dp\)\)\s+// BOT[^\n]*CANCELAR)', error_text + r'\1', content)

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)
