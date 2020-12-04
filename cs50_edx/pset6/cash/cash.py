from cs50 import get_float

# Prompts user for change value
while True:
    n = get_float("Change: ")
    if n > 0:
        break

# Convert change to cents
cents = int(n * 100)

# Calculate number for each coin type and value remaining
quarters = cents // 25
cents = cents % 25

dimes = cents // 10
cents = cents % 10

nickels = cents // 5
cents = cents % 5

pennies = cents // 1

# Calculate the sum of coin types
coins = quarters + dimes + nickels + pennies
print(int(coins))