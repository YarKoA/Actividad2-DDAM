import re

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\components\EventComponents.kt'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# Fix the trailing garbage at the very end of the file
# We'll just find the last valid } of NavItemButton and truncate everything after.
match = re.search(r'(@Composable\s+private fun NavItemButton.*?}\s*}\s*}\s*})', content, re.DOTALL)
if match:
    # Get everything up to the end of NavItemButton
    valid_content = content[:match.end()]
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(valid_content + "\n")
    print("Fixed trailing garbage!")
