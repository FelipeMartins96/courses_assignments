import sys
import crypt


# Iterates through all possible passwords for each lenght and checks it


def crack(n, s=""):
    for c in alphabet:
        password = s + c
        if n == 0:
            print(password)
            if crypt.crypt(password, salt) == sys.argv[1]:
                print(password)
                sys.exit(1)
        else:
            crack(n - 1, password)


# Checks for correct arguments usage
if len(sys.argv) != 2:
    sys.exit("Usage: python caesar.py n")

# Store salt from hash
salt = sys.argv[1][0] + sys.argv[1][1]

# Define an alphabet
alphabet = "abcdefghijklmnopqrstuvwyxz"
alphabet += alphabet.upper()

# Iterates through possible passworld lengths
for i in range(5):
    crack(i)

