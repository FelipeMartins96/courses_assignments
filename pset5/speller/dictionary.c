// Implements a dictionary's functionality

#include <stdbool.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

#include "dictionary.h"

typedef struct node
{
    char word[LENGTH + 1];
    struct node *next;
}
node;

// makes a list for each letter
node *dictionary_mem[26] = {NULL};
// number of words
int now = 0;

// free a list by recursion
void free_list(node *ptr)
{
    if (ptr != NULL)
    {
        free_list(ptr->next);
        free(ptr);
    }
}

// Returns true if word is in dictionary else false
bool check(const char *word)
{
    // creates a temp word array to make it all lowercase
    int n = strlen(word);
    char lower[n + 1];
    for (int i = 0; i <= n; i++)
    {
        lower[i] = tolower(word[i]);
    }

    // checks each letter it is to check with correspondent list
    int alpha = lower[0] - 'a';
    if (alpha < 0 || alpha > 26)
    {
        return false;
    }
    node *ptr;
    ptr = dictionary_mem[alpha];
    // goes through the list
    while (ptr != NULL)
    {
        // checks if word match a word in the list
        if (strcmp(ptr->word, lower) == 0)
        {
            return true;
        }
        ptr = ptr->next;
    }

    return false;
}

// Loads dictionary into memory, returning true if successful else false
bool load(const char *dictionary)
{
    // Open dictionary archive to pointer dictionary_file
    FILE *dictionary_file = fopen(dictionary, "r");
    if (dictionary_file == NULL)
    {
        fprintf(stderr, "Could not open %s.\n", dictionary);
        return false;
    }

    // Load words from file to memory
    int index = 0;
    int alpha;
    node *ptr;
    for (char c = fgetc(dictionary_file); c != EOF; c = fgetc(dictionary_file))
    {
        if (c != '\n')
        {
            //checks if its a new word
            if (index == 0)
            {
                alpha = c - 'a';
                // checks if list for the letter has already started
                if (dictionary_mem[alpha] == NULL)
                {
                    dictionary_mem[alpha] = malloc(sizeof(node));
                    // checks if memory was allocated
                    if (dictionary_mem[alpha] == NULL)
                    {
                        fprintf(stderr, "Could not allocate memory.\n");
                        return false;
                    }
                    dictionary_mem[alpha]->word[index] = c;
                    dictionary_mem[alpha]->next = NULL;
                }
                else
                {
                    ptr = dictionary_mem[alpha];
                    dictionary_mem[alpha] = malloc(sizeof(node));
                    // checks if memory was allocated
                    if (dictionary_mem[alpha] == NULL)
                    {
                        fprintf(stderr, "Could not allocate memory.\n");
                        return false;
                    }
                    dictionary_mem[alpha]->next = ptr;
                    dictionary_mem[alpha]->word[index] = c;
                }
                index++;
            }
            else
            {
                dictionary_mem[alpha]->word[index] = c;
                index++;
            }
        }
        else
        {
            dictionary_mem[alpha]->word[index] = '\0';
            index = 0;
            now++;
        }
    }
    fclose(dictionary_file);
    return true;
}

// Returns number of words in dictionary if loaded else 0 if not yet loaded
unsigned int size(void)
{
    return now;
}

// Unloads dictionary from memory, returning true if successful else false
bool unload(void)
{
    // iterates through each letter list
    for (int i = 0; i < 26; i++)
    {
        free_list(dictionary_mem[i]);
    }
    return true;
}
