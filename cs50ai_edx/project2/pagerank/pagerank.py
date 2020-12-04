import os
import random
import re
import sys

DAMPING = 0.85
SAMPLES = 10000


def main():
    if len(sys.argv) != 2:
        sys.exit("Usage: python pagerank.py corpus")
    corpus = crawl(sys.argv[1])
    ranks = sample_pagerank(corpus, DAMPING, SAMPLES)
    print(f"PageRank Results from Sampling (n = {SAMPLES})")
    for page in sorted(ranks):
        print(f"  {page}: {ranks[page]:.4f}")
    ranks = iterate_pagerank(corpus, DAMPING)
    print(f"PageRank Results from Iteration")
    for page in sorted(ranks):
        print(f"  {page}: {ranks[page]:.4f}")


def crawl(directory):
    """
    Parse a directory of HTML pages and check for links to other pages.
    Return a dictionary where each key is a page, and values are
    a list of all other pages in the corpus that are linked to by the page.
    """
    pages = dict()

    # Extract all links from HTML files
    for filename in os.listdir(directory):
        if not filename.endswith(".html"):
            continue
        with open(os.path.join(directory, filename)) as f:
            contents = f.read()
            links = re.findall(r"<a\s+(?:[^>]*?)href=\"([^\"]*)\"", contents)
            pages[filename] = set(links) - {filename}

    # Only include links to other pages in the corpus
    for filename in pages:
        pages[filename] = set(
            link for link in pages[filename]
            if link in pages
        )

    return pages


def transition_model(corpus, page, damping_factor):
    """
    Return a probability distribution over which page to visit next,
    given a current page.

    With probability `damping_factor`, choose a link at random
    linked to by `page`. With probability `1 - damping_factor`, choose
    a link at random chosen from all pages in the corpus.
    """
    distribution = dict()

    if len(corpus[page]):
        for link in corpus:
            distribution[link] = (1 - damping_factor) / len(corpus)
            if link in corpus[page]:
                distribution[link] += damping_factor / len(corpus[page])
    else:
        # If there is no link in current page return an equal probability
        # for every page in corpus
        for link in corpus:
            distribution[link] = 1 / len(corpus)

    return distribution


def sample_pagerank(corpus, damping_factor, n):
    """
    Return PageRank values for each page by sampling `n` pages
    according to transition model, starting with a page at random.

    Return a dictionary where keys are page names, and values are
    their estimated PageRank value (a value between 0 and 1). All
    PageRank values should sum to 1.
    """
    # Initialize a counter for everytime the chain transitions 
    # into a link
    pageCount = dict()
    for link in corpus:
        pageCount[link] = 0

    # Initialize the first page randomly
    page = random.choice(list(corpus.keys()))
    
    # Transition pages using damping factor for "SAMPLES" times given
    # counting each page transitions
    for i in range(n):
        nextPage = roll_next_page(
            transition_model(corpus, page, damping_factor))
        pageCount[nextPage] += 1
        page = nextPage

    # rank is given by count of transitions to each page
    # divided by the total number of transistions (SAMPLES)
    rank = dict()
    for link in corpus:
        rank[link] = pageCount[link] / n

    return rank


def iterate_pagerank(corpus, damping_factor):
    """
    Return PageRank values for each page by iteratively updating
    PageRank values until convergence.

    Return a dictionary where keys are page names, and values are
    their estimated PageRank value (a value between 0 and 1). All
    PageRank values should sum to 1.
    """
    # two rank dicts created so updates doens't affect it's own iteration
    rank = dict()
    nextRank = dict()
    for link in corpus:
        rank[link] = 1 / len(corpus)

    biggestDelta = 1.0
    while biggestDelta > 0.001:
        biggestDelta = 0.0
        for page in corpus:
            sum = 0
            for i in corpus:
                if page in corpus[i]:
                    sum += rank[i] / len(corpus[i])
            nextRank[page] = ((1 - damping_factor) / len(corpus)) + (damping_factor * sum)

        # Calculate the biggest delta in the iteration and deepcopy the nextRank values
        # calculated to rank for next iteration
        for page in rank:
            dif = abs(rank[page] - nextRank[page])
            rank[page] = nextRank[page]
            if dif > biggestDelta:
                biggestDelta = dif

    return nextRank


def roll_next_page(distribution):
    """
    Return a randomly selected page given a probability
    distribution over which page to visit next.
    """
    lowerBound = 0.0
    upperBound = 0.0
    probRoll = random.random()
    
    for link in distribution:
        upperBound += distribution[link]
        if probRoll >= lowerBound and probRoll < upperBound:
            return link
        lowerBound = upperBound


if __name__ == "__main__":
    main()
