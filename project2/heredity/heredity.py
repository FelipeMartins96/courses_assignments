import csv
import itertools
import sys

PROBS = {

    # Unconditional probabilities for having gene
    "gene": {
        2: 0.01,
        1: 0.03,
        0: 0.96
    },

    "trait": {

        # Probability of trait given two copies of gene
        2: {
            True: 0.65,
            False: 0.35
        },

        # Probability of trait given one copy of gene
        1: {
            True: 0.56,
            False: 0.44
        },

        # Probability of trait given no gene
        0: {
            True: 0.01,
            False: 0.99
        }
    },

    # Mutation probability
    "mutation": 0.01
}


def main():

    # Check for proper usage
    if len(sys.argv) != 2:
        sys.exit("Usage: python heredity.py data.csv")
    people = load_data(sys.argv[1])

    # Keep track of gene and trait probabilities for each person
    probabilities = {
        person: {
            "gene": {
                2: 0,
                1: 0,
                0: 0
            },
            "trait": {
                True: 0,
                False: 0
            }
        }
        for person in people
    }

    # Loop over all sets of people who might have the trait
    names = set(people)
    for have_trait in powerset(names):

        # Check if current set of people violates known information
        fails_evidence = any(
            (people[person]["trait"] is not None and
             people[person]["trait"] != (person in have_trait))
            for person in names
        )
        if fails_evidence:
            continue

        # Loop over all sets of people who might have the gene
        for one_gene in powerset(names):
            for two_genes in powerset(names - one_gene):

                # Update probabilities with new joint probability
                p = joint_probability(people, one_gene, two_genes, have_trait)
                update(probabilities, one_gene, two_genes, have_trait, p)

    # Ensure probabilities sum to 1
    normalize(probabilities)

    # Print results
    for person in people:
        print(f"{person}:")
        for field in probabilities[person]:
            print(f"  {field.capitalize()}:")
            for value in probabilities[person][field]:
                p = probabilities[person][field][value]
                print(f"    {value}: {p:.4f}")


def load_data(filename):
    """
    Load gene and trait data from a file into a dictionary.
    File assumed to be a CSV containing fields name, mother, father, trait.
    mother, father must both be blank, or both be valid names in the CSV.
    trait should be 0 or 1 if trait is known, blank otherwise.
    """
    data = dict()
    with open(filename) as f:
        reader = csv.DictReader(f)
        for row in reader:
            name = row["name"]
            data[name] = {
                "name": name,
                "mother": row["mother"] or None,
                "father": row["father"] or None,
                "trait": (True if row["trait"] == "1" else
                          False if row["trait"] == "0" else None)
            }
    return data


def powerset(s):
    """
    Return a list of all possible subsets of set s.
    """
    s = list(s)
    return [
        set(s) for s in itertools.chain.from_iterable(
            itertools.combinations(s, r) for r in range(len(s) + 1)
        )
    ]


def joint_probability(people, one_gene, two_genes, have_trait):
    """
    Compute and return a joint probability.

    The probability returned should be the probability that
        * everyone in set `one_gene` has one copy of the gene, and
        * everyone in set `two_genes` has two copies of the gene, and
        * everyone not in `one_gene` or `two_gene` does not have the gene, and
        * everyone in set `have_trait` has the trait, and
        * everyone not in set` have_trait` does not have the trait.
    """

    probability = 1
    
    for person in people:
        # give default values as no parents defined and 0 genes
        geneProb = PROBS["gene"][0]
        traitProb = PROBS["trait"][0][person in have_trait]
        personGenes = 0
        
        # Change values if not 0 genes
        if person in one_gene:
            traitProb = PROBS["trait"][1][person in have_trait]
            geneProb = PROBS["gene"][1]
            personGenes = 1
        elif person in two_genes:
            traitProb = PROBS["trait"][2][person in have_trait]
            geneProb = PROBS["gene"][2]
            personGenes = 2

        # change value if parents are defined
        motherGenes = 0
        fatherGenes = 0
        if people[person]["mother"] is not None:
            if people[person]["mother"] in one_gene:
                motherGenes = 1
            elif people[person]["mother"] in two_genes:
                motherGenes = 2
            if people[person]["father"] in one_gene:
                fatherGenes = 1
            elif people[person]["father"] in two_genes:
                fatherGenes = 2
            
            geneProb = get_gene_prob(personGenes, motherGenes, fatherGenes)

        probability *= (geneProb * traitProb)
            
    return probability


def update(probabilities, one_gene, two_genes, have_trait, p):
    """
    Add to `probabilities` a new joint probability `p`.
    Each person should have their "gene" and "trait" distributions updated.
    Which value for each distribution is updated depends on whether
    the person is in `have_gene` and `have_trait`, respectively.
    """
    for person in probabilities:
        probabilities[person]["trait"][person in have_trait] += p
        if person in one_gene:
            probabilities[person]["gene"][1] += p
        elif person in two_genes:
            probabilities[person]["gene"][2] += p
        else:
            probabilities[person]["gene"][0] += p


def normalize(probabilities):
    """
    Update `probabilities` such that each probability distribution
    is normalized (i.e., sums to 1, with relative proportions the same).
    """
    for person in probabilities:
        geneFactor = 1 / sum(probabilities[person]["gene"].values())
        traitFactor = 1 / sum(probabilities[person]["trait"].values())
        probabilities[person]["gene"][0] *= geneFactor
        probabilities[person]["gene"][1] *= geneFactor
        probabilities[person]["gene"][2] *= geneFactor
        probabilities[person]["trait"][True] *= traitFactor
        probabilities[person]["trait"][False] *= traitFactor
    

def get_gene_prob(nPerson, nMother, nFather):
    passingProb = {
        0: PROBS["mutation"],
        1: 0.5,
        2: (1 - PROBS["mutation"])
    }

    if nPerson == 0:
        # Probability is probability of father not passing and(*) mother not passing
        # not passing porbability = (1 - passing probability)
        return (1 - passingProb[nFather]) * (1 - passingProb[nMother])
    elif nPerson == 1:
        # Probability is probability of father passing and(*) mother not passing
        # or(+) father not passing and mother passing
        # not passing porbability = (1 - passing probability)
        return (passingProb[nFather] * (1 - passingProb[nMother])) + ((1 - passingProb[nFather]) * passingProb[nMother])
    elif nPerson == 2:
        # Probability is probability of father passin and(*) mother passing
        return (passingProb[nFather] * passingProb[nMother])


if __name__ == "__main__":
    main()
