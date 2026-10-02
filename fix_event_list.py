import re

with open('app/src/main/java/com/example/actividad2_ddam/ui/screens/EventListScreen.kt', 'r') as f:
    content = f.read()

# 1. Remove FormularioCuentaDialog import
content = content.replace("import com.example.actividad2_ddam.FormularioCuentaDialog\n", "")

# 2. Replace user initialization
target2 = """    var usuario by remember {
        mutableStateOf(
            Repo.usuarioActual
        )
    }"""
repl2 = """    val firebaseUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
    val userName = firebaseUser?.email?.substringBefore("@")?.replaceFirstChar { it.uppercase() } ?: "Usuario" """
content = content.replace(target2, repl2)

# 3. Replace primeraLetra init
target3 = """                val primeraLetra =
                    usuario
                        ?.nombre
                        ?.trim()
                        ?.firstOrNull()
                        ?.uppercase()
                        ?: "U" """
repl3 = """                val primeraLetra =
                    userName.firstOrNull()?.uppercase() ?: "U" """
content = content.replace(target3.strip(), repl3.strip())

# 4. Replace welcome message
target4 = """                    Text(
                        text =
                            "Bienvenido, ${usuario?.nombre ?: "Usuario"}",

                        color =
                            Color.White,"""
repl4 = """                    Text(
                        text =
                            "Bienvenido, $userName",

                        color =
                            Color.White,"""
content = content.replace(target4, repl4)

# 5. Replace inicial in dialog
target5 = """                        val inicial =
                            usuario
                                ?.nombre
                                ?.trim()
                                ?.firstOrNull()
                                ?.uppercase()
                                ?: "U" """
repl5 = """                        val inicial =
                            userName.firstOrNull()?.uppercase() ?: "U" """
content = content.replace(target5.strip(), repl5.strip())

# 6. Replace user name in dialog
target6 = """                        Text(
                            text =
                                usuario?.nombre
                                    ?: "",

                            fontSize =
                                20.sp,"""
repl6 = """                        Text(
                            text =
                                userName,

                            fontSize =
                                20.sp,"""
content = content.replace(target6, repl6)

# 7. Replace email in dialog
target7 = """                                Text(
                                    text =
                                        "Correo: ${usuario?.correo ?: ""}",

                                    color =
                                        if ("""
repl7 = """                                Text(
                                    text =
                                        "Correo: ${firebaseUser?.email ?: "No disponible"}",

                                    color =
                                        if ("""
content = content.replace(target7, repl7)

# 8. Remove telefono and edad in dialog (just replace with empty string)
telefono_edad_pattern = re.compile(r'                                Text\(\s*text =\s*"Teléfono:.*?Color\.Black\s*}\s*\)\s*Text\(\s*text =\s*"Edad:.*?Color\.Black\s*}\s*\)', re.DOTALL)
content = telefono_edad_pattern.sub('', content)

# 9. Replace mostrarEditar logic in Button
target9 = """                                mostrarPerfil =
                                    false

                                mostrarEditar =
                                    true"""
repl9 = """                                android.widget.Toast.makeText(ctx, "Edición de perfil deshabilitada", android.widget.Toast.LENGTH_SHORT).show()"""
content = content.replace(target9, repl9)

# 10. Remove FormularioCuentaDialog usage entirely
form_dialog_pattern = re.compile(r'        if \(mostrarEditar\) \{\s*FormularioCuentaDialog\([\s\S]*?mostrarEditar =\s*false\s*\}\s*\)\s*\}', re.DOTALL)
content = form_dialog_pattern.sub('', content)

with open('app/src/main/java/com/example/actividad2_ddam/ui/screens/EventListScreen.kt', 'w') as f:
    f.write(content)
