// Felipe Martins
// Validates a credit card number

#include <stdio.h>
#include <cs50.h>
#include <math.h>

int main(void)
{
    long long int n;

    // Gets a non negative card number
    do
    {
        n = get_long_long("Card Number: ");
    }
    while (n < 0);

    // Mark card as invalid if number is longer than 16-digits
    if (n >= 10000000000000000)
    {
        printf("INVALID\n");
    }
    else
    {
        int sum;

        // Splits card digits into an array
        int digits[16];
        for (int i = 15; i >= 0; i--)
        {
            digits[i] = n / pow(10, (i));
            n = n % (long long int)pow(10, (i));
        }

        // Calculate the sum of the non doubled digits
        for (int i = 0; i < 16; i = i + 2)
        {
            sum = sum + digits[i];
        }

        // Calcultes the sum of the doubled digits
        for (int i = 1; i < 16; i = i + 2)
        {
            int dd = digits[i] * 2;
            if (dd < 10)
            {
                sum = sum + dd;
            }
            else
            {
                sum = sum + 1 + (dd % 10);
            }
        }

        if (sum % 10 == 0)
        {
            // Checks for Visa
            if (digits[15] == 4 || (digits[15] == 0 && digits[14] == 0 && digits[13] == 0 && digits[12] == 4))
            {
                printf("VISA\n");
            }
            // Checks for MasterCard
            else if (digits[15] == 5 && digits[14] > 0 && digits[14] < 6)
            {
                printf("MASTERCARD\n");
            }
            // Checks for AMEX
            else if (digits[15] == 0 && digits[14] == 3 && (digits[13] == 4 || digits[13] == 7))
            {
                printf("AMEX\n");
            }
            // If the number is validated thru Luhn's Algorithm, but doesn't match neither of the 3 types
            else
            {
                printf("INVALID\n");
            }
        }
        else
        {
            printf("INVALID\n");
        }
    }
}