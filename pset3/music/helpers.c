// Helper functions for music

#include <cs50.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#include "helpers.h"

// Converts a fraction formatted as X/Y to eighths
int duration(string fraction)
{
    //parse the string
    string numerator = strtok(fraction, "/");
    string denominator = strtok(NULL, "/");

    //transform the string into integer
    int num = atoi(numerator);
    int den = atoi(denominator);

    //returns the lenght in eights
    return (8 / den) * num;
}

// Calculates frequency (in Hz) of a note
int frequency(string note)
{
    char letter = note[0];
    char octave = note[1];
    int semitones;


    //calculate semitones distance by letters
    //first if to adjust for octave starting at C
    if (letter < 'C')
    {
        semitones = (letter - 'A') * 2;
    }
    else
    {
        //if to adjust for E being only 1 semitone apart from F, normally it's 2 for each letter apart
        if (letter < 'F')
        {
            //C is 9 semitones away from A
            semitones = ((letter - 'C') * 2) - 9;
        }
        else
        {
            semitones = ((letter - 'F') * 2) - 4;
        }
    }

    //checks if the note has a accidental and adjust octave value to the correct one
    if (note[2] != '\0')
    {
        if (note[1] == '#')
        {
            semitones++;
        }
        else
        {
            semitones--;
        }
        octave = note[2];
    }

    //check the difference of the octave from 4
    int delta_octave = (octave - '0') - 4;
    semitones = semitones + (delta_octave * 12);

    //calculate the frequency
    float power = (float)semitones / 12;
    float frequency = 440 * pow(2, power);

    //return the frequency
    return round(frequency);



}

// Determines whether a string represents a rest
bool is_rest(string s)
{
    if (strcmp(s, "") == 0)
    {
        return true;
    }
    else
    {
        return false;
    }
}
