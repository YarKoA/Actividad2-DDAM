with open("app/src/main/java/com/example/actividad2_ddam/ui/screens/EventListScreen.kt", "r") as f:
    lines = f.readlines()

new_lines = []
skip = 0
for line in lines:
    if skip > 0:
        skip -= 1
        continue
    if "val primeraLetra =" in line:
        new_lines.append("                val primeraLetra = \"U\"\n")
        skip = 5  # Skip the next 5 lines
    elif "val inicial =" in line:
        new_lines.append("                        val inicial = \"U\"\n")
        skip = 5  # Skip the next 5 lines
    else:
        new_lines.append(line)

with open("app/src/main/java/com/example/actividad2_ddam/ui/screens/EventListScreen.kt", "w") as f:
    f.writelines(new_lines)
