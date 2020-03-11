#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <cs50.h>

int main(int argc, string argv[])
{
    // Checks if command-line arguments was used correctly
    if (argc == 2)
    {
        // Converts the command-line argumento from string to a int
        int k = atoi(argv[1]);

        // Prompts user for text
        string s = get_string("plaintext: ");

        // Loop to go through the string
        for (int i = 0; i < strlen(s); i++)
        {
            // Checks if it is alphabetic upper or lower and cipher it
            if (isalpha(s[i]))
            {
                if (islower(s[i]))
                {
                    s[i] = 'a' + ((s[i] - 'a') + k) % 26;
                }
                else
                {
                    s[i] = 'A' + ((s[i] - 'A') + k) % 26;
                }
            }

        }
        printf("ciphertext: %s\n", s);
    }
    else
    {
        // Prints error message if command-line argument was used incorrectly
        printf("Error\n");
        return 1;
    }
}