from cs50 import get_int

# Prompts user for card number
n = get_int("Card Number: ")

# Store first 2 digits
start = int(str(n)[0]) * 10 + int(str(n)[1])

# Converts int to string and reverse it's order
n = str(n)[::-1]

sumA, sumB = (0, 0)

# Iterates through the card digits
for i in range(len(n)):
    # Sums for doubled values and non doubled values
    if (i + 1) % 2:
        sumA += int(n[i])
    else:
        j = int(n[i]) * 2
        # Https://stackoverflow.com/questions/14939953/sum-the-digits-of-a-number-python
        while j:
            sumB += j % 10
            j //= 10

# Checks if valid and brand
if (sumA + sumB) % 10 == 0:
    if start == 34 or start == 37:
        print("AMEX")
    if start >= 51 and start <= 55:
        print("MASTERCARD")
    if start // 10 == 4:
        print("VISA")
else:
    print("INVALID")