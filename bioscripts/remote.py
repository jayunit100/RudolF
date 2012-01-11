#A PYthon example of getting remote data from ncbi.
#Request works for the whole web page, but doesn't get the sequence (gets the whole page). 
#Will edit soon. 
#TODO update for python 3.0

import sys, urllib2


#Read the result into a single string.
def readResult(fd):
    seq = "";
    while True:
        data = fd.read(1024)
        if not len(data):
            return seq;
        seq = seq + data
		
#Create a request object

accessions = ["AAF52932.1","AAB02568.1"]

#Needs some work : pubmed fastas are wrapped in html - not sure if the raw urls are accessible anymore.
#Maybe move to EBI?
for acc in accessions:
    print("Requesting " + acc + " from ncbi.nlm.nih")
    req = urllib2.Request("http://www.ncbi.nlm.nih.gov/protein/" + acc + "?report=fasta&format=text")

#Load the request into 
fd = urllib2.urlopen(req)

a = readResult(fd)

print a

