#include <cs50.h>
#include <stdio.h>
#include <string.h>

// Max number of candidates
#define MAX 9

// preferences[i][j] is number of voters who prefer i over j
int preferences[MAX][MAX];

// locked[i][j] means i is locked in over j
bool locked[MAX][MAX];

// Each pair has a winner, loser
typedef struct
{
    int winner;
    int loser;
}
pair;

// Array of candidates
string candidates[MAX];
pair pairs[MAX * (MAX - 1) / 2];

int pair_count;
int candidate_count;

// Function prototypes
bool vote(int rank, string name, int ranks[]);
void record_preferences(int ranks[]);
void add_pairs(void);
void sort_pairs(void);
void lock_pairs(void);
void print_winner(void);

int main(int argc, string argv[])
{
    // Check for invalid usage
    if (argc < 2)
    {
        printf("Usage: tideman [candidate ...]\n");
        return 1;
    }

    // Populate array of candidates
    candidate_count = argc - 1;
    if (candidate_count > MAX)
    {
        printf("Maximum number of candidates is %i\n", MAX);
        return 2;
    }
    for (int i = 0; i < candidate_count; i++)
    {
        candidates[i] = argv[i + 1];
    }

    // Clear graph of locked in pairs
    for (int i = 0; i < candidate_count; i++)
    {
        for (int j = 0; j < candidate_count; j++)
        {
            locked[i][j] = false;
        }
    }

    pair_count = 0;
    int voter_count = get_int("Number of voters: ");

    // Query for votes
    for (int i = 0; i < voter_count; i++)
    {
        // ranks[i] is voter's ith preference
        int ranks[candidate_count];

        // Query for each rank
        for (int j = 0; j < candidate_count; j++)
        {
            string name = get_string("Rank %i: ", j + 1);

            if (!vote(j, name, ranks))
            {
                printf("Invalid vote.\n");
                return 3;
            }
        }

        record_preferences(ranks);

        printf("\n");
    }

    add_pairs();
    sort_pairs();
    lock_pairs();
    print_winner();
    return 0;
}

// Update ranks given a new vote
bool vote(int rank, string name, int ranks[])
{
    for (int i = 0; i < candidate_count; i++)
    {
        if (!strcmp(name, candidates[i]))
        {
            ranks[rank] = i;
            return true;
        }
    }
    return false;
}

// Update preferences given one voter's ranks
void record_preferences(int ranks[])
{
    for (int i = 0; i < candidate_count; i++)
    {
        for (int j = i + 1; j < candidate_count; j++)
        {
            preferences[ranks[i]][ranks[j]]++;
        }
    }
    return;
}

// Record pairs of candidates where one is preferred over the other
void add_pairs(void)
{
    pair_count = 0;
    for (int i = 0; i < candidate_count; i++)
    {
        for (int j = i + 1; j < candidate_count; j++)
        {
            if (preferences[i][j] > preferences[j][i])
            {
                pairs[pair_count].winner = i;
                pairs[pair_count].loser = j;
                pair_count++;
            }
            else if (preferences[i][j] < preferences[j][i])
            {
                pairs[pair_count].winner = j;
                pairs[pair_count].loser = i;
                pair_count++;
            }
        }
    }
    return;
}

// Sort pairs in decreasing order by strength of victory
void sort_pairs(void)
{
    // Sort the pairs using selection sort
    for (int i = 0; i < (pair_count - 1); i++)
    {
        int max_index = i;
        for (int j = i + 1; j < pair_count; j++)
        {
            if (preferences[pairs[max_index].winner][pairs[max_index].loser] < preferences[pairs[j].winner][pairs[j].loser])
            {
                max_index = j;
            }
        }
        // Swap the first non ordered pair for the highest prefence found pair
        if (max_index != i)
        {
            pair temp = pairs[max_index];
            pairs[max_index] = pairs[i];
            pairs[i] = temp;
        }
    }
    return;
}

// Lock pairs into the candidate graph in order, without creating cycles
void lock_pairs(void)
{
    bool loss[candidate_count];
    bool win[candidate_count];
    for (int i = 0; i < candidate_count; i++)
    {
        loss[i] = false;
        win[i] = false;
    }

    for (int i = 0; i < pair_count; i++)
    {
        loss[pairs[i].loser] = true;
        win[pairs[i].winner] = true;
        bool cycle = true;
        for (int j = 0; j < candidate_count; j++)
        {
            cycle = cycle && (!(win[j] ^ loss[j]));
        }
        if (!cycle)
        {
            locked[pairs[i].winner][pairs[i].loser] = true;
        }
        else
        {
            loss[pairs[i].loser] = false;
            win[pairs[i].winner] = false;
        }
    }
    return;
}

// Print the winner of the election
void print_winner(void)
{
    // Find the winner, the source of the graph
    for (int c = 0; c < candidate_count; c++)
    {
        bool edge = false;
        for (int r = 0; r < candidate_count; r++)
        {
            edge = edge || locked[r][c];
        }
        if (!edge)
        {
            printf("%s\n", candidates[c]);
            return;
        }
    }
    return;
}

