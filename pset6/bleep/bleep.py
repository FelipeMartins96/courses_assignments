from cs50 import get_string
from sys import argv, exit


def main():

    # Checks if command line arguments is valid
    if len(argv) is not 2:
        print("Usage: python bleep.py dictionary")
        exit(1)

    # Loads dictionary words
    words = set()
    dictionary = argv[1]
    file = open(dictionary, "r")
    for line in file:
        words.add(line.rstrip("\n"))
    file.close()

    # Prompts user for text
    print("What message would you like to censor?")
    text = get_string("")

    # Prints the censored text
    for s in text.split(" "):
        if s.lower() in words:
            for i in range(len(s)):
                print("*", end="")
            print(end=" ")
        else:
            print(s, end=" ")
    print()


if __name__ == "__main__":
    main()
