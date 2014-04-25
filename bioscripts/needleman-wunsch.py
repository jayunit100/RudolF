#!/usr/bin/env python
from sys import *
seq1='GAGACCGCCATGGCGACCCTGGAAAAGCTGATGAAGGCCCT'
seq2='AGACCCAATGCGACCCTGAAAAAGCTGATGAAGGCCTTTTT'

print'''
# Both sequences are similar to the human protein huntingtin. 
# Spurious expanded trinucleotide (CGA) repeats in this protein 
# cause it to aggregate in neurons leading to Huntington's disease. 

# The Needleman-Wunsch algorithm preforms a global alignment 
# off two sequences (of length n and m) 
# For a given similarity matrix s
# (containig the penalties for character match-mismatch) 
# and a LINEAR gap penalty the algorithm is guaranteed 
# to find the alignment with highest score (in O(nm) time).   
# The algorithm is outlined through comments to the source.
'''
stderr.write('Calculating')
rows=len(seq1)+1
cols=len(seq2)+1
try:
    #use fast numerical arrays if we can
    from numpy import *
    a=zeros((rows,cols),float)
except ImportError:
    #use a list if we have to
    a=[]
    for i in range(rows):
	a+=[[0.]*cols]

#################################################
##              Needleman-Wunsch               ##
#################################################

match=1.
mismatch=-1.
gap=-1.
s={
'AA':     match,'AG':mismatch,'AC':mismatch,'AT':mismatch,\
'GA':mismatch,'GG':     match,'GC':mismatch,'GT':mismatch,\
'CA':mismatch,'CG':mismatch,'CC':     match,'CT':mismatch,\
'TA':mismatch,'TG':mismatch,'TC':mismatch,'TT':     match,\
}
for i in range(rows):
    a[i][0] = 0
for j in range(cols):
    a[0][j] = 0
for i in range(1,rows):
    for j in range(1,cols):
        # Dynamic programing -- aka. divide and conquer:
        # Since gap penalties are linear in gap size
        # the score of an alignmet of length l only depends on the   
        # the l-th characters in the alignment (match - mismatch - gap)
        # and the score of the one shorter (l-1) alignment,
        # i.e. we can calculate how to extend an arbritary alignment
        # soley based on the previous score value.  
	choice1 = a[i-1][j-1] + s[(seq1[i-1] + seq2[j-1])]
	choice2 = a[i-1][j] + gap
	choice3 = a[i][j-1] + gap
	a[i][j] = max(choice1, choice2, choice3)

				
aseq1 = ''
aseq2 = ''
#We reconstruct the alignment into aseq1 and aseq2, 
i = len(seq1)
j = len(seq2)
while i>0 and j>0:
    if i%10==0:
        stderr.write('.')

    #by preforming a traceback of how the matrix was filled out above,
    #i.e. we find a shortest path from a[n,m] to a[0,0]
    score = a[i][j]
    score_diag = a[i-1][j-1]
    score_up = a[i][j-1]
    score_left = a[i-1][j]
    if score == score_diag + s[seq1[i-1] + seq2[j-1]]:
        aseq1 = seq1[i-1] + aseq1
        aseq2 = seq2[j-1] + aseq2
        i -= 1
        j -= 1
    elif score == score_left + gap:
        aseq1 = seq1[i-1] + aseq1
        aseq2 = '_' + aseq2
        i -= 1
    elif score == score_up + gap:
        aseq1 = '_' + aseq1
        aseq2 = seq2[j-1] + aseq2
        j -= 1
    else:
        #should never get here..
        print 'ERROR'
        i=0
        j=0
        aseq1='ERROR';aseq2='ERROR';seq1='ERROR';seq2='ERROR'
while i>0:
    #If we hit j==0 before i==0 we keep going in i.
    aseq1 = seq1[i-1] + aseq1
    aseq2 = '_' + aseq2
    i -= 1		

while j>0:
    #If we hit i==0 before i==0 we keep going in j. 
    aseq1 = '_' + aseq1
    aseq2 = seq2[j-1] + aseq2
    j -= 1

#################################################
#################################################
##              Full backtrack                 ##
#################################################

#To reconstruct all alinghments is somewhat tedious..
def make_graph():
#the simpilest way is to make a graph of the possible constructions of the values in a 
    graph={}
    for i in range(1,cols)[::-1]:
        graph[(i,0)] = [(i-1,0)]
        graph[(0,i)] = [(0,i-1)]
        for j in range(1,cols)[::-1]:
            graph[(i,j)]=[]
            score = a[i][j]
            score_diag = a[i-1][j-1]
            score_up = a[i][j-1]
            score_left = a[i-1][j]
            if score == score_diag + s[seq1[i-1] + seq2[j-1]]:
		graph[(i,j)] += [(i-1,j-1)]
            if score == score_left + gap:
		graph[(i,j)] += [(i-1,j)]
            if score == score_up + gap:
		graph[(i,j)] += [(i,j-1)]
    return graph

def find_all_paths(graph, start, end, path=[]):
#and then to recursivly find all paths 
#from bottom right to top left..
    path = path + [start]
#    print start
    if start == end:
        return [path]
    if not graph.has_key(start):
        return []
    paths = []
    for node in graph[start]:
        if node not in path:
            newpaths = find_all_paths(graph, node, end, path)
            for newpath in newpaths:
                paths.append(newpath)
    return paths

graph=make_graph()
tracks=find_all_paths(graph,(cols-1,rows-1),(0,0))
baseqs1=[]
baseqs2=[]
for track in tracks:
#using these we can reconstruct all optimal alig.-s 
    baseq1 = ''
    baseq2 = ''
    last_step=(cols-1,rows-1)
    for step in track:
        i,j=last_step
        if i==step[0]:
            baseq1 = '_' + baseq1
            baseq2 = seq2[j-1] + baseq2
        elif j==step[1]:
            baseq1 = seq1[i-1] + baseq1
            baseq2 = '_' + baseq2
        else:
            baseq1 = seq1[i-1] + baseq1
            baseq2 = seq2[j-1] + baseq2

        last_step=step
    baseqs1+=[baseq1]
    baseqs2+=[baseq2]
#################################################

print ''
print '# Using:  match='+repr(match)+'; mismatch='+repr(mismatch)+'; gap='+repr(gap)      	
print seq1
print seq2
print '# We get e.g.:'
print aseq1
print aseq2
print ''
gaps=0
mms=0
ms=0
for i in range(len(aseq1)):
    if aseq1[i]==aseq2[i]:
        aseq1=aseq1[:i]+'='+aseq1[i+1:]
        aseq2=aseq2[:i]+'='+aseq2[i+1:]
        ms+=1
    else:
        if aseq1[i]=='_' or aseq2[i]=='_':
            gaps+=1
        else:
            mms+=1

print aseq1
print aseq2
print ''
print ms,' matches; ',mms,' mismatches; ',gaps,' gaps.' 
print '# With a score of'
print a[rows-2][cols-2],'/',min(len(seq1),len(seq2))

print 'Optimal alig. is ',len(tracks),' times degenrate:'
print ''
for i in range(len(tracks)):
    print i+1,'.'
    print baseqs1[i]
    print baseqs2[i]
