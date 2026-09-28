# -*- coding: utf-8 -*-
import re
file_path = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens\SettingsScreen.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

new_btn = '''                    var showLogoutDialog by remember { mutableStateOf(false) }
                    
                    Button(
                        onClick = { showLogoutDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cerrar sesión", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    if (showLogoutDialog) {
                        androidx.compose.material3.AlertDialog(
                            onDismissRequest = { showLogoutDialog = false },
                            title = { androidx.compose.material3.Text("Cerrar sesión") },
                            text = { androidx.compose.material3.Text("Estas seguro de que deseas cerrar sesión?") },
                            confirmButton = {
                                androidx.compose.material3.TextButton(onClick = { 
                                    showLogoutDialog = false
                                    onCerrarSesion() 
                                }) {
                                    androidx.compose.material3.Text("Salir", color = Color.Red)
                                }
                            },
                            dismissButton = {
                                androidx.compose.material3.TextButton(onClick = { showLogoutDialog = false }) {
                                    androidx.compose.material3.Text("Cancelar")
                                }
                            }
                        )
                    }'''

# Replace only the button
content = re.sub(r'Button\(\s*onClick = \{ onCerrarSesion\(\) \},.*?Text\("Cerrar sesi.n", color = Color\.White, fontWeight = FontWeight\.Bold\)\s*\}', new_btn, content, flags=re.DOTALL)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
