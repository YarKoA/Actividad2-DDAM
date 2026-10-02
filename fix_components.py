import re
file_path = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\components\EventComponents.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Increase icon button sizes
content = re.sub(r'\.size\(24\.dp\)\s*\n\s*\.scale\(escalaAlarma\)', '.size(40.dp)\n                            .scale(escalaAlarma)', content)
content = re.sub(r'\.size\(24\.dp\)\s*\n\s*\.scale\(escalaEditar\)', '.size(40.dp)\n                            .scale(escalaEditar)', content)

# Fallback if no newlines
content = re.sub(r'\.size\(24\.dp\)\s*\.scale\(escalaAlarma\)', '.size(40.dp).scale(escalaAlarma)', content)
content = re.sub(r'\.size\(24\.dp\)\s*\.scale\(escalaEditar\)', '.size(40.dp).scale(escalaEditar)', content)

new_swipe = '''        backgroundContent = {
            val direction = dismissState.dismissDirection
            if (direction == SwipeToDismissBoxValue.StartToEnd || direction == SwipeToDismissBoxValue.EndToStart) {
                val color = Color(0xFFFF5B5B)
                val alignment = if (direction == SwipeToDismissBoxValue.StartToEnd) Alignment.CenterStart else Alignment.CenterEnd
                
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color, RoundedCornerShape(20.dp))
                        .padding(horizontal = 24.dp),
                    contentAlignment = alignment
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        },'''

content = re.sub(r'backgroundContent = \{.*?Icon\(.*?\}\s*\}\s*\},', new_swipe, content, flags=re.DOTALL)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
