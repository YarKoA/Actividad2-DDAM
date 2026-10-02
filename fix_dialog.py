# -*- coding: utf-8 -*-
import re
file_path = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\components\EventComponents.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

new_dismiss = '''    var showDeleteDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart || dismissValue == SwipeToDismissBoxValue.StartToEnd) {
                showDeleteDialog = true
                true // Keep it dismissed while asking
            } else {
                false
            }
        }
    )

    if (showDeleteDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { 
                showDeleteDialog = false
                scope.launch { dismissState.reset() }
            },
            title = { androidx.compose.material3.Text("Borrar Tarea") },
            text = { androidx.compose.material3.Text("Estas seguro de que deseas eliminar esta tarea?") },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = { 
                    showDeleteDialog = false
                    onDelete() 
                }) {
                    androidx.compose.material3.Text("Borrar", color = Color.Red)
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { 
                    showDeleteDialog = false
                    scope.launch { dismissState.reset() }
                }) {
                    androidx.compose.material3.Text("Cancelar")
                }
            }
        )
    }'''

content = re.sub(r'val dismissState =\s*rememberSwipeToDismissBoxState\(\s*confirmValueChange = \{\s*dismissValue ->\s*if \(\s*dismissValue ==\s*SwipeToDismissBoxValue\s*\.EndToStart\s*\|\|\s*dismissValue ==\s*SwipeToDismissBoxValue\s*\.StartToEnd\s*\)\s*\{\s*onDelete\(\)\s*true\s*\}\s*else\s*\{\s*false\s*\}\s*\}\s*\)', new_dismiss, content, flags=re.DOTALL)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
