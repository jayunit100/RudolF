#!/usr/bin/env python

#This code adopted from Paulo Nuin's bioinformatics repo and blog : https://github.com/nuin/beginning-python-for-bioinformatics/

'''script that counts the number of bases in a DNA sequence
showing the string.count() method'''

input='''
AGAGCGCGCGCGCGGCCCGCGCGGGCGGGCGCGACCACATGAGACCTATACC
AGAGATTACACACCCCACACCACATTTTATAGAGAGATTCACATTTAGGTCA
CAAATTGGAGCGCGCG
'''
#Example 1 : print found matches, standard ... 

#counting
total_a = input.count('A')
total_c = input.count('C')
total_g = input.count('G')
total_t = input.count('T')

#printing results
print str(total_a) + ' As found'
print str(total_c) + ' Cs found'
print str(total_g) + ' Gs found'
print str(total_t) + ' Ts found'

#Example 2 (rudolf style) :)...  printing matches, using lambdas...

nucleotides=['A','G','C','T']
counts = map( (lambda x: input.count(x)),nucleotides)
nuc_counts=zip(nucleotides,counts)

print(nuc_counts)
