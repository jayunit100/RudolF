#A PYthon example of getting remote data.
#TODO update for python 3.0

import sys, urllib2


#Read the result into a single string.
def readResult(fd):
	seq="";
	while 1:
    		data = fd.read(1024)
    		if not len(data):
        		return seq;
    		seq=seq+data
		
#Create a request object

accessions=["AAF52932.1","AAB02568.1"];

#Needs some work : pubmed fastas are wrapped in html - not sure if the raw urls are accessible anymore.
#Maybe move to EBI?
for acc in accessions:
        print("Requesting " + acc + " from ncbi.nlm.nih");
	req = urllib2.Request("http://www.ncbi.nlm.nih.gov/protein/"+acc+"?report=fasta&format=text")

#Load the request into 
fd = urllib2.urlopen(req)

a=readResult(fd);

print a;

