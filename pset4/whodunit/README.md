# Questions

## What's `stdint.h`?

It's a header for a C language library which define integers with specific widht.

## What's the point of using `uint8_t`, `uint32_t`, `int32_t`, and `uint16_t` in a program?

To declare integer variables with specific width, specific amount of bits, also for better compatibility since the amount of bites
in a int can vary from system.

## How many bytes is a `BYTE`, a `DWORD`, a `LONG`, and a `WORD`, respectively?

`BYTE`  : 1 byte
`DWORD` : 4 bytes
`LONG`  : 4 bytes
`WORD`  : 2 bytes

## What (in ASCII, decimal, or hexadecimal) must the first two bytes of any BMP file be? Leading bytes used to identify file formats (with high probability) are generally called "magic numbers."

0x42 and 0x4d.

## What's the difference between `bfSize` and `biSize`?

`bfSize` contains the size of the file in bytes containing the pixels, the headers and the padding, and `biSize` is the size of the
BITMAPINFOHEADER in bytes.

## What does it mean if `biHeight` is negative?

It means that the bitmap in top-down and the origin is the upper-left corner.

## What field in `BITMAPINFOHEADER` specifies the BMP's color depth (i.e., bits per pixel)?

`biBitCount`.

## Why might `fopen` return `NULL` in lines 24 and 32 of `copy.c`?

If it is unable to open the infile or unable to create the outfile.

## Why is the third argument to `fread` always `1` in our code?

Because when we are extracting the header files we only need to read 'one unit' of the size defined by each headers struct, for the
reding of the rgb values we are only reading one at a time at each iteration.

## What value does line 65 of `copy.c` assign to `padding` if `bi.biWidth` is `3`?

it assigns the value `3` to `padding`.

## What does `fseek` do?

It changes the position the file pointer is pointing to in the file, it moves it forward or backwards from a inputted position.

## What is `SEEK_CUR`?

Returns the value which the file pointer is currently pointing to.

## Whodunit?

Professor Plum.
