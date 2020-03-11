#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

// create a variable named BYTE with 8 bits long
typedef uint8_t BYTE;

int main(int argc, char *argv[])
{
    // ensure proper usage
    if (argc != 2)
    {
        fprintf(stderr, "Usage: recover image\n");
        return 1;
    }

    FILE *inptr = fopen(argv[1], "r");

    // checks if file was sucessfully open
    if (inptr == NULL)
    {
        fprintf(stderr, "Could not open %s.\n", argv[1]);
        return 2;
    }

    BYTE buffer[512];

    // iterates while 512 bytes are sucessfully read
    int b = 0;
    int cont = 0;
    char FILENAME[8];
    FILE *outptr;

    while (1)
    {
        b = fread(&buffer, sizeof(BYTE), 512, inptr);
        //  checks if all 512 bytes are read
        if (b != 512)
        {
            // close outfile pointer only if it's open
            if (cont > 0)
            {
                fclose(outptr);
            }
            break;
        }

        if (buffer[0] == 0xff && buffer[1] == 0xd8  && buffer[2] == 0xff && (buffer[3] & 0xf0) == 0xe0)
        {
            // close previouly open .jpg
            if (cont > 0)
            {
                fclose(outptr);
            }

            // crete .jpg sequential name
            sprintf(FILENAME, "%03i.jpg", cont);
            // open .jpg file
            outptr = fopen(FILENAME, "w");
            // checks if file was created succesfully
            if (outptr == NULL)
            {
                fprintf(stderr, "Could not open %s.\n", FILENAME);
                return 2;
            }
            // writes data read from image to file
            fwrite(&buffer, sizeof(BYTE), 512, outptr);
            cont++;
        }
        else if (cont > 0)
        {
            // if a new .jpg is not detected writes to current .jpg
            fwrite(&buffer, sizeof(BYTE), 512, outptr);
        }
    }

    fclose(inptr);
    return 0;
}