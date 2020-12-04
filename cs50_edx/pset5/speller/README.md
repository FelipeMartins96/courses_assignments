# Questions

## What is pneumonoultramicroscopicsilicovolcanoconiosis?

A lung disease caused by inhaling very fine ash and sand dust.

## According to its man page, what does `getrusage` do?

Gets the resource usage.

## Per that same man page, how many members are in a variable of type `struct rusage`?

16.

## Why do you think we pass `before` and `after` by reference (instead of by value) to `calculate`, even though we're not changing their contents?

To optimize resources usage since we don't need to allocate a new space in memory which needs to store a copy of the values and can store just the space needed for a pointer.

## Explain as precisely as possible, in a paragraph or more, how `main` goes about reading words from a file. In other words, convince us that you indeed understand how that function's `for` loop works.

The `for` function iterates through the file getting one char at each pass until it reaches the `EOF` which means end of file, when it reaches a alphabetical character it will trigger an if function which will start storing that character and the subsequent alphabetical ones in an character array until it reaches a non alphabetical character where it will define it as the end of the word adding a `\0` character to the end of the array to define it as the end of the string and spell check it versus the loaded dictionary

Also it checks if the word read doesn't trepass the number of letters of the longest possible word or if it is a word if numbers in it, if any of these two conditions if found it will "jump" that word from the file.

## Why do you think we used `fgetc` to read each word's characters one at a time rather than use `fscanf` with a format string like `"%s"` to read whole words at a time? Put another way, what problems might arise by relying on `fscanf` alone?

`fscanf` would require a better previouly known file formating, making it less universal, also we would may need to iterates through each scanned string to verify if it's a valid one.

## Why do you think we declared the parameters for `check` and `load` as `const` (which means "constant")?

So it's value can't be modified in the function, it can only be read since it's value is still used again in the main function.
