import os

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens\EventFormScreen.kt'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('Color(0xFFF3EDF7)', 'if (com.example.actividad2_ddam.model.Repo.modoOscuro) Color(0xFF202020) else Color(0xFFF3EDF7)')

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens\CalendarScreen.kt'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('Color(0xFFF3EDF7)', 'if (com.example.actividad2_ddam.model.Repo.modoOscuro) Color(0xFF202020) else Color(0xFFF3EDF7)')
content = content.replace('Color(0xFFEFEAFA)', 'if (com.example.actividad2_ddam.model.Repo.modoOscuro) Color(0xFF343434) else Color(0xFFEFEAFA)')
content = content.replace('val txtColor = if (isCurrentMonth) Color.Black else Color.White', 'val txtColor = if (isCurrentMonth) (if (com.example.actividad2_ddam.model.Repo.modoOscuro) Color.White else Color.Black) else Color.LightGray')

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens\EventEditScreen.kt'
if os.path.exists(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    content = content.replace('Color(0xFFF3EDF7)', 'if (com.example.actividad2_ddam.model.Repo.modoOscuro) Color(0xFF202020) else Color(0xFFF3EDF7)')
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)

