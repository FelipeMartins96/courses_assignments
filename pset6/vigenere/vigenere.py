import sys
from cs50 import get_string

# Checks for correct arguments usage
if len(sys.argv) != 2 or sys.argv[1].isalpha() == 0:
    sys.exit("Usage: python caesar.py k")

# Prompts user for text
s = get_string("plaintext: ")

# Convert input argument into an integer
k = sys.argv[1]

print("ciphertext: ", end="")

# Declares key counter
n = 0

# Ciphers the text iterating through the chars of the text string
for c in s:
    if c.isalpha():
        if c.isupper():
            c = chr(((ord(c) - ord('A')) + ord(sys.argv[1][n].upper()) - ord('A')) % 26 + ord('A'))
        else:
            c = chr(((ord(c) - ord('a')) + ord(sys.argv[1][n].upper()) - ord('A')) % 26 + ord('a'))
        n += 1
    print(c, end="")
    if n == len(sys.argv[1]):
        n = 0
print()