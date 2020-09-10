import csv
import sys

from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier

TEST_SIZE = 0.4


def main():

    # Check command-line arguments
    if len(sys.argv) != 2:
        sys.exit("Usage: python shopping.py data")

    # Load data from spreadsheet and split into train and test sets
    evidence, labels = load_data(sys.argv[1])
    X_train, X_test, y_train, y_test = train_test_split(
        evidence, labels, test_size=TEST_SIZE
    )

    # Train model and make predictions
    model = train_model(X_train, y_train)
    predictions = model.predict(X_test)
    sensitivity, specificity = evaluate(y_test, predictions)

    # Print results
    print(f"Correct: {(y_test == predictions).sum()}")
    print(f"Incorrect: {(y_test != predictions).sum()}")
    print(f"True Positive Rate: {100 * sensitivity:.2f}%")
    print(f"True Negative Rate: {100 * specificity:.2f}%")


def month_to_index(month):
    if month == "Jan":
        return 0
    if month == "Feb":
        return 1
    if month == "Mar":
        return 2
    if month == "Apr":
        return 3
    if month == "May":
        return 4
    if month == "June":
        return 5
    if month == "Jul":
        return 6
    if month == "Aug":
        return 7
    if month == "Sep":
        return 8
    if month == "Oct":
        return 9
    if month == "Nov":
        return 10
    if month == "Dec":
        return 11


def load_data(filename):
    """
    Load shopping data from a CSV file `filename` and convert into a list of
    evidence lists and a list of labels. Return a tuple (evidence, labels).

    evidence should be a list of lists, where each list contains the
    following values, in order:
        - Administrative, an integer
        - Administrative_Duration, a floating point number
        - Informational, an integer
        - Informational_Duration, a floating point number
        - ProductRelated, an integer
        - ProductRelated_Duration, a floating point number
        - BounceRates, a floating point number
        - ExitRates, a floating point number
        - PageValues, a floating point number
        - SpecialDay, a floating point number
        - Month, an index from 0 (January) to 11 (December)
        - OperatingSystems, an integer
        - Browser, an integer
        - Region, an integer
        - TrafficType, an integer
        - VisitorType, an integer 0 (not returning) or 1 (returning)
        - Weekend, an integer 0 (if false) or 1 (if true)

    labels should be the corresponding list of labels, where each label
    is 1 if Revenue is true, and 0 otherwise.
    """
    evidence = []
    labels = []
    with open(filename) as csvfile:
        file_rows = csv.reader(csvfile)
        next(file_rows)
        for row in file_rows:
            values = []

            # - Administrative, an integer
            values.append(int(row.pop(0)))
            # - Administrative_Duration, a floating point number
            values.append(float(row.pop(0)))
            # - Informational, an integer
            values.append(int(row.pop(0)))
            # - Informational_Duration, a floating point number
            values.append(float(row.pop(0)))
            # - ProductRelated, an integer
            values.append(int(row.pop(0)))
            # - ProductRelated_Duration, a floating point number
            values.append(float(row.pop(0)))
            # - BounceRates, a floating point number
            values.append(float(row.pop(0)))
            # - ExitRates, a floating point number
            values.append(float(row.pop(0)))
            # - PageValues, a floating point number
            values.append(float(row.pop(0)))
            # - SpecialDay, a floating point number
            values.append(float(row.pop(0)))
            # - Month, an index from 0 (January) to 11 (December)
            values.append(month_to_index(row.pop(0)))
            # - OperatingSystems, an integer
            values.append(int(row.pop(0)))
            # - Browser, an integer
            values.append(int(row.pop(0)))
            # - Region, an integer
            values.append(int(row.pop(0)))
            # - TrafficType, an integer
            values.append(int(row.pop(0)))
            # - VisitorType, an integer 0 (not returning) or 1 (returning)
            visitor_type = row.pop(0)
            if visitor_type == "Returning_Visitor":
                values.append(1)
            else:
                values.append(0)
            # - Weekend, an integer 0 (if false) or 1 (if true)label = row.pop(0)
            weekend = row.pop(0)
            if weekend == "TRUE":
                values.append(1)
            else:
                values.append(0)

            evidence.append(values)

            label = row.pop(0)
            if label == "TRUE":
                labels.append(1)
            else:
                labels.append(0)

    return evidence, labels


def train_model(evidence, labels):
    """
    Given a list of evidence lists and a list of labels, return a
    fitted k-nearest neighbor model (k=1) trained on the data.
    """
    model = KNeighborsClassifier(n_neighbors=1)
    model.fit(evidence, labels)

    return model


def evaluate(labels, predictions):
    """
    Given a list of actual labels and a list of predicted labels,
    return a tuple (sensitivity, specificty).

    Assume each label is either a 1 (positive) or 0 (negative).

    `sensitivity` should be a floating-point value from 0 to 1
    representing the "true positive rate": the proportion of
    actual positive labels that were accurately identified.

    `specificity` should be a floating-point value from 0 to 1
    representing the "true negative rate": the proportion of
    actual negative labels that were accurately identified.
    """
    correct_positive = 0
    correct_negative = 0
    total_positive = 0
    total_negative = 0

    for i in range(len(labels)):
        if labels[i] == 1:
            total_positive += 1
            if predictions[i] == 1:
                correct_positive += 1
        else:
            total_negative += 1
            if predictions[i] == 0:
                correct_negative += 1

    sensitivity = correct_positive / total_positive
    specificity = correct_negative / total_negative

    return sensitivity, specificity

if __name__ == "__main__":
    main()
