import re
file_path = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\viewmodel\EventViewModel.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace(
    'private val _todasLasTareas = repository.todasLasTareas',
    'val todasLasTareas = repository.todasLasTareas\n    private val _todasLasTareas = repository.todasLasTareas'
)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
