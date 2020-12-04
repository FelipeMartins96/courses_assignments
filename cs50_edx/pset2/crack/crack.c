#define _XOPEN_SOURCE
#define MAXLENGHT 5
#include <stdio.h>
#include <unistd.h>
#include <cs50.h>
#include <string.h>

int main(int argc, string argv[])
{
    // Checks for the number of command-line arguments
    if (argc != 2)
    {
        printf("Error!\n");
        return 1;
    }

    // Creates a string with the first 2 digits of the hash
    char salt[] = {argv[1][0], argv[1][1], '\0'};

    // Creates an array of chars to go through the possible passwords
    // Allocated with +2 so the algorithm doesn't write on non allocated memory
    char pass[MAXLENGHT + 1];
    pass[1] = '\0';

    // Runs the algorithm until all the possibilities are checked
    while (pass[MAXLENGHT] != 'A')
    {
        // Goes thru all the letters in the alphabet
        for (int j = 0; j <= ('z' - 'A'); j++)
        {
            // Jumps the counter over the non alphabetic ASCII characters
            if (j == ('Z' - 'A') + 1)
            {
                j = j + ('a' - 'Z') - 1;
            }
            pass[0] = 'A' + j;
            // Checks if the password match
            if (strcmp((crypt(pass, salt)), argv[1]) == 0)
            {
                printf("%s\n", pass);
                return 0;
            }
        }

        pass[0]++;

        // Manipulates the array after going thru one alphabet try
        // Checks each place of the array at a time
        for (int j = 0; j < MAXLENGHT; j++)
        {

            if (pass[j] == ('z' + 1))
            {
                if (pass[j + 1] == '\0')
                {
                    pass[j + 1] = 'A';
                    pass[j + 2] = '\0';
                }
                else
                {
                    pass[j + 1]++;
                }
                pass[j] = 'A';
            }
            if (pass[j] == 'Z' + 1)
            {
                pass[j] = 'a';
            }
        }
    }
}
