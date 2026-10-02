import re
file_path = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens\LoginScreen.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

imports = '''import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
'''
if 'import androidx.compose.ui.draw.drawWithCache' not in content:
    content = content.replace('import androidx.compose.ui.graphics.Color', imports + 'import androidx.compose.ui.graphics.Color')

new_logo = '''                Image(
                    painter = painterResource(id = R.drawable.logo_tareum),
                    contentDescription = "Logo TAREUM",
                    modifier = Modifier
                        .size(130.dp)
                        .graphicsLayer { alpha = 0.99f }
                        .drawWithCache {
                            val gradient = Brush.linearGradient(
                                colors = listOf(Color(0xFF8BB5CE), Color(0xFF2C3E6B)),
                                start = Offset(0f, 0f),
                                end = Offset(size.width, size.height * 0.75f)
                            )
                            onDrawWithContent {
                                drawContent()
                                drawRect(
                                    brush = gradient,
                                    size = Size(size.width, size.height * 0.75f),
                                    blendMode = BlendMode.SrcAtop
                                )
                            }
                        }
                )'''

content = re.sub(r'Image\(\s*painter = painterResource\(id = R\.drawable\.logo_tareum\),\s*contentDescription = "Logo TAREUM",\s*modifier = Modifier\.size\(130\.dp\)\s*\)', new_logo, content)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
