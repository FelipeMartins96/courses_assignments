import nltk
import sys
import os
import string
import math

FILE_MATCHES = 1
SENTENCE_MATCHES = 1


def main():

    # Check command-line arguments
    if len(sys.argv) != 2:
        sys.exit("Usage: python questions.py corpus")

    # Calculate IDF values across files
    files = load_files(sys.argv[1])
    file_words = {
        filename: tokenize(files[filename])
        for filename in files
    }
    file_idfs = compute_idfs(file_words)

    # Prompt user for query
    query = set(tokenize(input("Query: ")))

    # Determine top file matches according to TF-IDF
    filenames = top_files(query, file_words, file_idfs, n=FILE_MATCHES)

    # Extract sentences from top files
    sentences = dict()
    for filename in filenames:
        for passage in files[filename].split("\n"):
            for sentence in nltk.sent_tokenize(passage):
                tokens = tokenize(sentence)
                if tokens:
                    sentences[sentence] = tokens

    # Compute IDF values across sentences
    idfs = compute_idfs(sentences)

    # Determine top sentence matches
    matches = top_sentences(query, sentences, idfs, n=SENTENCE_MATCHES)
    for match in matches:
        print(match)


def load_files(directory):
    """
    Given a directory name, return a dictionary mapping the filename of each
    `.txt` file inside that directory to the file's contents as a string.
    """
    content = {}
    for file in os.listdir(directory):
        content[file[:-4]] = open(os.path.join(directory, file), 'r').read()
    # Dict with keys being filename minus .txt, and values are file content as string
    return content


def tokenize(document):
    """
    Given a document (represented as a string), return a list of all of the
    words in that document, in order.

    Process document by coverting all words to lowercase, and removing any
    punctuation or English stopwords.
    """
    return [word.lower() for word in nltk.word_tokenize(document) 
            if word not in string.punctuation 
            and word not in nltk.corpus.stopwords.words("english")
            and not any([c == '=' for c in word])]


def compute_idfs(documents):
    """
    Given a dictionary of `documents` that maps names of documents to a list
    of words, return a dictionary that maps words to their IDF values.

    Any word that appears in at least one of the documents should be in the
    resulting dictionary.
    """
    idfs = {}
    total_documents = len(documents)
    for document in documents.values():
        for word in document:
            idfs[word] = 0
    
    for document in documents.values():
        for key in idfs.keys():
            if key in document:
                idfs[key] += 1
    
    for key in idfs:
        idfs[key] = math.log(total_documents / idfs[key])
    
    return idfs


def top_files(query, files, idfs, n):
    """
    Given a `query` (a set of words), `files` (a dictionary mapping names of
    files to a list of their words), and `idfs` (a dictionary mapping words
    to their IDF values), return a list of the filenames of the the `n` top
    files that match the query, ranked according to tf-idf.
    """
    files_rank = dict.fromkeys(files.keys(), 0)
    for word in query:
        for file_name in files:
            term_frequency = files[file_name].count(word)
            if term_frequency > 0:
                files_rank[file_name] += term_frequency * idfs[word]
    
    # Sort files by decreasing tf-idfs values
    sorted_files = [key for (key, value) in sorted(files_rank.items(), key=lambda x: x[1], reverse=True)]
    # return n highest tf-idfs filenames
    return sorted_files[0:n]


def top_sentences(query, sentences, idfs, n):
    """
    Given a `query` (a set of words), `sentences` (a dictionary mapping
    sentences to a list of their words), and `idfs` (a dictionary mapping words
    to their IDF values), return a list of the `n` top sentences that match
    the query, ranked according to idf. If there are ties, preference should
    be given to sentences that have a higher query term density.
    """
    sentences_rank = dict.fromkeys(sentences.keys(), (0, 0))
    
    for key in sentences:
        for word in query:
            if word in sentences[key]:
                sentences_rank[key] = (sentences_rank[key][0] + idfs[word], sentences_rank[key][1] + 1)
        sentences_rank[key] = (sentences_rank[key][0], sentences_rank[key][1]/len(sentences[key]))
    
    # Sort sentences by idfs and query term density
    sorted_sentences = [key for (key, value) in sorted(sentences_rank.items(), key=lambda x: x[1], reverse=True)]
    # print(sorted_sentences[0:10])
    return sorted_sentences[0:n]


if __name__ == "__main__":
    main()
