with open("app/src/main/java/com/example/actividad2_ddam/MainActivity.kt", "r") as f:
    content = f.read()

imports_to_add = """
import androidx.lifecycle.lifecycleScope
import com.example.actividad2_ddam.data.SettingsManager
import kotlinx.coroutines.launch
import javax.inject.Inject
"""

content = content.replace("import dagger.hilt.android.AndroidEntryPoint\n", "import dagger.hilt.android.AndroidEntryPoint\n" + imports_to_add)

class_start = """class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        lifecycleScope.launch { settingsManager.modoOscuroFlow.collect { Repo.modoOscuro = it } }
        lifecycleScope.launch { settingsManager.letraGrandeFlow.collect { Repo.letraGrande = it } }
        lifecycleScope.launch { settingsManager.grosorGruesoFlow.collect { Repo.grosorGrueso = it } }
"""

content = content.replace("class MainActivity : ComponentActivity() {\n    override fun onCreate(savedInstanceState: Bundle?) {\n        super.onCreate(savedInstanceState)", class_start)

with open("app/src/main/java/com/example/actividad2_ddam/MainActivity.kt", "w") as f:
    f.write(content)
