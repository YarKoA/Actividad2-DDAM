with open("app/src/main/java/com/example/actividad2_ddam/ui/screens/SettingsScreen.kt", "r") as f:
    content = f.read()

import_statement = "import androidx.hilt.navigation.compose.hiltViewModel\nimport com.example.actividad2_ddam.viewmodel.SettingsViewModel\n"
content = content.replace("import androidx.compose.ui.unit.sp\n", "import androidx.compose.ui.unit.sp\n" + import_statement)

content = content.replace("fun SettingsScreen(navController: NavController, onCerrarSesion: () -> Unit) {", "fun SettingsScreen(navController: NavController, onCerrarSesion: () -> Unit, vm: SettingsViewModel = hiltViewModel()) {")

content = content.replace("onCheckedChange = { Repo.modoOscuro = it }", "onCheckedChange = { Repo.modoOscuro = it; vm.setModoOscuro(it) }")
content = content.replace("onCheckedChange = { Repo.letraGrande = it }", "onCheckedChange = { Repo.letraGrande = it; vm.setLetraGrande(it) }")
content = content.replace("onCheckedChange = { Repo.grosorGrueso = it }", "onCheckedChange = { Repo.grosorGrueso = it; vm.setGrosorGrueso(it) }")

with open("app/src/main/java/com/example/actividad2_ddam/ui/screens/SettingsScreen.kt", "w") as f:
    f.write(content)
