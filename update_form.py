import re

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens\EventFormScreen.kt'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# Add haptic import if not there
if 'import androidx.compose.ui.hapticfeedback.HapticFeedbackType' not in content:
    content = re.sub(r'(import [^\n]+\n)(?!.*import )', r'\1import androidx.compose.ui.hapticfeedback.HapticFeedbackType\nimport androidx.compose.ui.platform.LocalHapticFeedback\n', content, count=1, flags=re.DOTALL)

# Add val haptic = LocalHapticFeedback.current
if 'val haptic = LocalHapticFeedback.current' not in content:
    content = content.replace('val context = LocalContext.current', 'val context = LocalContext.current\n    val haptic = LocalHapticFeedback.current')

# Trigger on successful save
# The old code is:
# val t = Tarea(
#    ...
# )
# viewModel.addEvent(t)
# onCerrar()

old_save = '''viewModel.addEvent(t)
                            onCerrar()'''
new_save = '''viewModel.addEvent(t)
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onCerrar()'''
content = content.replace(old_save, new_save)

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)
print("Updated EventFormScreen.kt")
