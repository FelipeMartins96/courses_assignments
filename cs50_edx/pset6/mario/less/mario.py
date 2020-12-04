from cs50 import get_int

# Prompts user for a integer from 1 to 8
while True:
    n = get_int("Half-pyramid height: ")
    if n <= 8 and n > 0:
        break

# Iterates for rows
for i in range(n):
    # Iterates for columns
    for j in range(n):
        if i + j >= n - 1:
            print("#", end="")

        else:
            print(" ", end="")
    print("")
