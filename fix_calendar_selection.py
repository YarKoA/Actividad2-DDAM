# -*- coding: utf-8 -*-
import re
file_path = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens\EventFormScreen.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Change MiniCalendar call
content = content.replace(
    'MiniCalendar()',
    'MiniCalendar(selectedDate = fechaSeleccionada, onDateSelected = { fechaSeleccionada = it })'
)

# Change MiniCalendar signature
old_sig = '''fun MiniCalendar() {'''
new_sig = '''fun MiniCalendar(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {'''
content = content.replace(old_sig, new_sig)

# Change Grid rendering logic inside MiniCalendar
old_grid = '''                    items(totalCells) { index ->
                        val isCurrentMonth = index in startOffset until (startOffset + daysInMonth)
                        val dayNumber = if (isCurrentMonth) index - startOffset + 1 else -1

                        val bgColor = if (isCurrentMonth) Color(0xFFF3EDF7) else Color(0xFF90A5B8)
                        
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(bgColor)
                                .padding(4.dp)
                        ) {
                            if (dayNumber > 0) {
                                Text(dayNumber.toString(), color = Color.Black, fontSize = 12.sp)
                            }
                        }
                    }'''

new_grid = '''                    items(totalCells) { index ->
                        val isCurrentMonth = index in startOffset until (startOffset + daysInMonth)
                        val dayNumber = if (isCurrentMonth) index - startOffset + 1 else -1

                        val isSelected = isCurrentMonth && selectedDate?.dayOfMonth == dayNumber && selectedDate?.year == yearMonth.year && selectedDate?.month == yearMonth.month

                        val bgColor = if (isSelected) Color(0xFF4B6B94) else if (isCurrentMonth) Color(0xFFF3EDF7) else Color(0xFF90A5B8)
                        val txtColor = if (isSelected) Color.White else Color.Black
                        
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(bgColor)
                                .clickable(enabled = isCurrentMonth) {
                                    if (isCurrentMonth) {
                                        onDateSelected(yearMonth.atDay(dayNumber))
                                    }
                                }
                                .padding(4.dp)
                        ) {
                            if (dayNumber > 0) {
                                Text(dayNumber.toString(), color = txtColor, fontSize = 12.sp)
                            }
                        }
                    }'''
content = content.replace(old_grid, new_grid)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
