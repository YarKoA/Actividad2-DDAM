with open("app/src/main/java/com/example/actividad2_ddam/viewmodel/EventViewModel.kt", "r") as f:
    content = f.read()

# 1. Add AuthRepository import
content = content.replace("import javax.inject.Inject\n", "import javax.inject.Inject\nimport com.example.actividad2_ddam.auth.data.AuthRepository\n")

# 2. Inject AuthRepository
content = content.replace("class EventViewModel @Inject constructor(\n    private val repository: TareaRepository\n) : ViewModel() {", "class EventViewModel @Inject constructor(\n    private val repository: TareaRepository,\n    private val authRepo: AuthRepository\n) : ViewModel() {\n\n    private val userId = authRepo.currentUser()?.uid ?: \"\"")

# 3. Use userId in repository calls
content = content.replace("repository.todasLasTareas", "repository.getTodasLasTareas(userId)")

with open("app/src/main/java/com/example/actividad2_ddam/viewmodel/EventViewModel.kt", "w") as f:
    f.write(content)
