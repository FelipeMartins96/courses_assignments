import sys
from cs50 import get_string

# Checks for correct arguments usage
if len(sys.argv) != 2:
    sys.exit("Usage: python caesar.py n")

# Prompts user for text
s = get_string("plaintext: ")

# Convert input argument into an integer
k = int(sys.argv[1])

print("ciphertext: ", end="")

# Ciphers the text iterating through the chars of the text string
for c in s:
    if c.isalpha():
        if c.isupper():
            c = chr(((ord(c) - ord('A')) + k) % 26 + ord('A'))
        else:
            c = chr(((ord(c) - ord('a')) + k) % 26 + ord('a'))
    print(c, end="")
print()
