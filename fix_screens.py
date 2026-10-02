import os
import re

def fix_file(filepath):
    if not os.path.exists(filepath):
        return
    with open(filepath, 'r') as f:
        content = f.read()

    # 1. EventFormScreen.kt: Remove contadorId references
    if "EventFormScreen.kt" in filepath:
        content = re.sub(r'var nuevaId = Repo\.contadorId\+\+\n\s*', '', content)
        content = content.replace("id = nuevaId,", "/* id autogenerado por Room */")

    # 2. EventListScreen.kt: Remove UI logic showing the user profile in the drawer
    if "EventListScreen.kt" in filepath:
        # Just replace the whole Profile section that uses Repo.usuarioActual with static text or null checks
        content = re.sub(r'Repo\.usuarioActual\?.nombre \?: "Usuario Invitado"', '"Usuario Activo"', content)
        content = re.sub(r'Repo\.usuarioActual\?.correo \?: "Sin correo"', '"user@firebase.com"', content)
        content = re.sub(r'Repo\.usuarioActual\?.telefono \?: "---"', '"---"', content)
        content = re.sub(r'Repo\.usuarioActual\?.edad\?.toString\(\) \?: "--"', '"--"', content)
        content = re.sub(r'Repo\.usuarioActual = Usuario\([\s\S]*?\)', '', content)
        content = re.sub(r'val usuarioPrueba = Usuario\([\s\S]*?\)', '', content)
        content = re.sub(r'if \(Repo\.usuarioActual == null\) \{[\s\S]*?Repo\.usuarioActual = usuarioPrueba\s*\}', '', content)
        
        # In EventListScreen, there's a reference to ViewModel T as generic maybe?
        content = re.sub(r'val eventViewModel = viewModel\(\)', 'val eventViewModel : com.example.actividad2_ddam.viewmodel.EventViewModel = androidx.hilt.navigation.compose.hiltViewModel()', content)
        # the error was: Cannot infer type for this parameter. EventListScreen.kt:58:20
        # Let's fix viewModel() without type arguments
        content = re.sub(r'val eventViewModel\s*=\s*viewModel\(\)', 'val eventViewModel: com.example.actividad2_ddam.viewmodel.EventViewModel = androidx.hilt.navigation.compose.hiltViewModel()', content)
        content = content.replace("import androidx.lifecycle.viewmodel.compose.viewModel", "import androidx.hilt.navigation.compose.hiltViewModel")


    # 3. RegisterScreen.kt: Remove Repo.usuarioActual creation
    if "RegisterScreen.kt" in filepath:
        content = re.sub(r'Repo\.usuarioActual = Usuario\([\s\S]*?\)', '', content)
        content = re.sub(r'import com\.example\.actividad2_ddam\.model\.Usuario', '', content)

    # 4. SettingsScreen.kt: Remove Repo.usuarioActual references
    if "SettingsScreen.kt" in filepath:
        content = re.sub(r'val userName = Repo\.usuarioActual\?.nombre \?: "Invitado"', 'val userName = "Usuario"', content)
        content = re.sub(r'val userEmail = Repo\.usuarioActual\?.correo \?: "Sin correo"', 'val userEmail = ""', content)

    # 5. Remove any leftover imports of Usuario
    content = re.sub(r'import com\.example\.actividad2_ddam\.model\.Usuario\n', '', content)

    with open(filepath, 'w') as f:
        f.write(content)

fix_file("app/src/main/java/com/example/actividad2_ddam/ui/screens/EventFormScreen.kt")
fix_file("app/src/main/java/com/example/actividad2_ddam/ui/screens/EventListScreen.kt")
fix_file("app/src/main/java/com/example/actividad2_ddam/ui/screens/RegisterScreen.kt")
fix_file("app/src/main/java/com/example/actividad2_ddam/ui/screens/SettingsScreen.kt")
