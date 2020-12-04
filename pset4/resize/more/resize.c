// Copies a BMP file

#include <stdio.h>
#include <stdlib.h>

#include "bmp.h"

int main(int argc, char *argv[])
{
    // ensure proper usage
    if (argc != 4)
    {
        fprintf(stderr, "Usage: resize n infile outfile\n");
        return 1;
    }

    // remember resize factor
    float f = atof(argv[1]);

    // remember filenames
    char *infile = argv[2];
    char *outfile = argv[3];

    // open input file
    FILE *inptr = fopen(infile, "r");
    if (inptr == NULL)
    {
        fprintf(stderr, "Could not open %s.\n", infile);
        return 2;
    }

    // open output file
    FILE *outptr = fopen(outfile, "w");
    if (outptr == NULL)
    {
        fclose(inptr);
        fprintf(stderr, "Could not create %s.\n", outfile);
        return 3;
    }

    // read infile's BITMAPFILEHEADER
    BITMAPFILEHEADER bf;
    fread(&bf, sizeof(BITMAPFILEHEADER), 1, inptr);

    // read infile's BITMAPINFOHEADER
    BITMAPINFOHEADER bi;
    fread(&bi, sizeof(BITMAPINFOHEADER), 1, inptr);

    // ensure infile is (likely) a 24-bit uncompressed BMP 4.0
    if (bf.bfType != 0x4d42 || bf.bfOffBits != 54 || bi.biSize != 40 ||
        bi.biBitCount != 24 || bi.biCompression != 0)
    {
        fclose(outptr);
        fclose(inptr);
        fprintf(stderr, "Unsupported file format.\n");
        return 4;
    }

    // determine padding for scanlines on input
    int padding_in = (4 - (bi.biWidth * sizeof(RGBTRIPLE)) % 4) % 4;

    //update outfile's header info
    BITMAPFILEHEADER bf_out = bf;
    BITMAPINFOHEADER bi_out = bi;

    if (f >= 1)
    {
        int n = f;
        bi_out.biWidth = bi.biWidth * n;
        bi_out.biHeight = bi.biHeight * n;

        int padding_out = (4 - (bi_out.biWidth * sizeof(RGBTRIPLE)) % 4) % 4;

        bi_out.biSizeImage = ((sizeof(RGBTRIPLE) * bi_out.biWidth) + padding_out) * abs(bi_out.biHeight);
        bf_out.bfSize = bi_out.biSizeImage + sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER);


        // write outfile's BITMAPFILEHEADER
        fwrite(&bf_out, sizeof(BITMAPFILEHEADER), 1, outptr);

        // write outfile's BITMAPINFOHEADER
        fwrite(&bi_out, sizeof(BITMAPINFOHEADER), 1, outptr);

        // iterate over infile's scanlines
        for (int i = 0, biHeight = abs(bi.biHeight); i < biHeight; i++)
        {
            for (int p = 0; p < n; p++)
            {
                // iterate over pixels in scanline
                for (int j = 0; j < bi.biWidth; j++)
                {
                    // temporary storage
                    RGBTRIPLE triple;

                    // read RGB triple from infile
                    fread(&triple, sizeof(RGBTRIPLE), 1, inptr);


                    // write RGB triple to outfile
                    for (int m = 0; m < n; m++)
                    {
                        fwrite(&triple, sizeof(RGBTRIPLE), 1, outptr);
                    }
                }

                // then add it back (to demonstrate how)
                for (int k = 0; k < padding_out; k++)
                {
                    fputc(0x00, outptr);
                }

                // return to start of the line if there are vertical iterations left
                if (p == n - 1)
                {
                    fseek(inptr, padding_in, SEEK_CUR);
                }
                else
                {
                    fseek(inptr, bi.biWidth * -sizeof(RGBTRIPLE), SEEK_CUR);
                }
            }
        }
    }
    else
    {
        int n = 1 / f;

        bi_out.biWidth = bi.biWidth / n;
        bi_out.biHeight = bi.biHeight / n;

        int padding_out = (4 - (bi_out.biWidth * sizeof(RGBTRIPLE)) % 4) % 4;

        bi_out.biSizeImage = ((sizeof(RGBTRIPLE) * bi_out.biWidth) + padding_out) * abs(bi_out.biHeight);
        bf_out.bfSize = bi_out.biSizeImage + sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER);


        // write outfile's BITMAPFILEHEADER
        fwrite(&bf_out, sizeof(BITMAPFILEHEADER), 1, outptr);

        // write outfile's BITMAPINFOHEADER
        fwrite(&bi_out, sizeof(BITMAPINFOHEADER), 1, outptr);

        // iterate over infile's scanlines
        for (int i = 0, biHeight = abs(bi.biHeight); i < biHeight; i = i + (1 * n))
        {
            // iterate over pixels in scanline
            for (int j = 0; j < bi.biWidth; j = j + (1 * n))
            {
                // temporary storage
                RGBTRIPLE triple;

                // read RGB triple from infile
                fread(&triple, sizeof(RGBTRIPLE), 1, inptr);

                // write RGB triple to outfile
                fwrite(&triple, sizeof(RGBTRIPLE), 1, outptr);

                fseek(inptr, sizeof(RGBTRIPLE) * (n - 1), SEEK_CUR);
            }

            // add output paddin
            for (int k = 0; k < padding_out; k++)
            {
                fputc(0x00, outptr);
            }

            // jump input padding
            fseek(inptr, ((bi.biWidth * sizeof(RGBTRIPLE)) + padding_in) * (n - 1), SEEK_CUR);
            fseek(inptr, padding_in, SEEK_CUR);
        }
    }
    // close infile
    fclose(inptr);

    // close outfile
    fclose(outptr);

    // success
    return 0;
}
