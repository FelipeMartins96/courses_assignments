// Felipe Martins
// Prints out a double half-pyramid

#include <stdio.h>
#include <cs50.h>

int main(void)
{
    int n;

    // Prompts user for a non-negative height no greater than 23
    do
    {
        n = get_int("Height: ");
    }
    while (n < 0 || n > 23);

    // Loops starting on 1 intentionally for conditions math
    // Each iteration of this loop prints one row of the pyramid
    for (int i = 1; i <= n; i++)
    {
        // Print the pyramid left half
        for (int j = 1; j <= n; j++)
        {
            if (i + j > n)
            {
                printf("#");
            }
            else
            {
                printf(" ");
            }
        }
        printf("  ");

        // Print the pyramid right half
        for (int k = 1; k <= i; k++)
        {
            printf("#");
        }
        printf("\n");
    }
}