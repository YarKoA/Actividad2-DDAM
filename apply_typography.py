import os
import re

directories = [
    r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens',
    r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\components'
]

imports_to_add = "import com.example.actividad2_ddam.ui.theme.scaledSp\nimport com.example.actividad2_ddam.ui.theme.scaledWeight\n"

for d in directories:
    for root, dirs, files in os.walk(d):
        for file in files:
            if file.endswith('.kt'):
                filepath = os.path.join(root, file)
                with open(filepath, 'r', encoding='utf-8') as f:
                    content = f.read()

                # Don't scale twice
                if '.scaledSp' in content or '.scaledWeight' in content:
                    continue
                
                # Replace fontSize = 14.sp -> fontSize = 14.sp.scaledSp
                new_content = re.sub(r'(\d+)\.sp', r'\1.sp.scaledSp', content)
                
                # Replace FontWeight.Medium -> FontWeight.Medium.scaledWeight
                new_content = re.sub(r'(FontWeight\.[A-Za-z]+)', r'\1.scaledWeight', new_content)
                
                if new_content != content:
                    # Add imports
                    new_content = re.sub(r'(import [^\n]+\n)(?!.*import )', r'\1' + imports_to_add, new_content, count=1, flags=re.DOTALL)
                    with open(filepath, 'w', encoding='utf-8') as f:
                        f.write(new_content)
                    print(f"Updated {file}")
