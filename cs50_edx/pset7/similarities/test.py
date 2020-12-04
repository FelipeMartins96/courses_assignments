a = "foobarbaz"
b = "barbaz"
n = 2


list_substrings_a = []
list_substrings_b = []

for i in range(len(a)-(n-1)):
    list_substrings_a.append(a[i:i+n])

for i in range(len(b)-(n-1)):
    list_substrings_b.append(b[i:i+n])

print(list_substrings_a)
print(list_substrings_b)

set_substrings_a = set(list_substrings_a)
set_substrings_b = set(list_substrings_b)

print(set_substrings_a)
print(set_substrings_b)

print(list(set_substrings_a & set_substrings_b)) #[]