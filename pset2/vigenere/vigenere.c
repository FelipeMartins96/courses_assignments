#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <cs50.h>

int main(int argc, string argv[])
{
    // Checks if the number of command-line arguments is valid
    if (argc != 2)
    {
        printf("error\n");
        return 1;
    }
    // Checks if theres is non alphabetic character in the argument and convert the key to uppercase
    for (int i = 0; i < strlen(argv[1]); i++)
    {
        argv[1][i] = toupper(argv[1][i]);
        if (isalpha(argv[1][i]) == false)
        {
            printf("error\n");
            return 1;
        }
    }

    // Prompts user for a text
    string s = get_string("plaintext: ");

    for (int i = 0, j = 0; i < strlen(s); i++)
    {
        // Loops the key if it's shorter than the text
        if (j == strlen(argv[1]))
        {
            j = 0;
        }


        int k = argv[1][j] - 'A';

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
            // Go through the key only after a alphabetic
            j++;
        }
    }

    printf("ciphertext: %s\n", s);

}