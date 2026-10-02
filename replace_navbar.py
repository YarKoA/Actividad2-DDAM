import re

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\components\EventComponents.kt'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# Read the new navbar code
with open('navbar.kt', 'r', encoding='utf-8') as f:
    navbar_content = f.read()

# Extract just the @Composable functions from navbar_content
match = re.search(r'(@Composable\s+fun BottomNavBar.*)', navbar_content, re.DOTALL)
if match:
    new_functions = match.group(1)
    
    # Remove old BottomNavBar and NavItemButton
    content = re.sub(r'@Composable\s+fun BottomNavBar.*?@Composable\s+private fun NavItemButton.*?}\s*}', new_functions, content, flags=re.DOTALL)
    
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)
        print("Replaced successfully!")
else:
    print("Could not find new functions")
