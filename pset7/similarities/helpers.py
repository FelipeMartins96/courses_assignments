from nltk.tokenize import sent_tokenize


def lines(a, b):
    """Return lines in both a and b"""

    # split strings into lines and transforms it in a set
    set_lines_a = set(a.splitlines())
    set_lines_b = set(b.splitlines())

    # transform the intersection of both sets in a list
    return list(set_lines_a & set_lines_b)


def sentences(a, b):
    """Return sentences in both a and b"""

    set_sentences_a = set(sent_tokenize(a))
    set_sentences_b = set(sent_tokenize(b))

    # transform the intersection of both sets in a list
    return list(set_sentences_a & set_sentences_b)


def substrings(a, b, n):
    """Return substrings of length n in both a and b"""

    # initialize a list for substrings
    list_substrings_a = []
    list_substrings_b = []

    # iterate over the string addind substrings to the list
    for i in range(len(a)-(n-1)):
        list_substrings_a.append(a[i:i+n])

    for i in range(len(b)-(n-1)):
        list_substrings_b.append(b[i:i+n])

    # transform the list into a set
    set_substrings_a = set(list_substrings_a)
    set_substrings_b = set(list_substrings_b)

    # transform the intersection of both sets in a list
    return list(set_substrings_a & set_substrings_b)
