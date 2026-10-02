import re

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens\SettingsScreen.kt'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# Add haptic import if not there
if 'import androidx.compose.ui.hapticfeedback.HapticFeedbackType' not in content:
    content = re.sub(r'(import [^\n]+\n)(?!.*import )', r'\1import androidx.compose.ui.hapticfeedback.HapticFeedbackType\nimport androidx.compose.ui.platform.LocalHapticFeedback\n', content, count=1, flags=re.DOTALL)

# Add val haptic = LocalHapticFeedback.current
if 'val haptic = LocalHapticFeedback.current' not in content:
    content = content.replace('var showLogoutDialog by remember { mutableStateOf(false) }', 'var showLogoutDialog by remember { mutableStateOf(false) }\n    val haptic = LocalHapticFeedback.current')

# Trigger on logout button click
content = content.replace('onClick = { showLogoutDialog = true }', 'onClick = { \n                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)\n                            showLogoutDialog = true \n                        }')

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)
print("Updated SettingsScreen.kt")
