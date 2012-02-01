
import csv
import re
import urllib2
from string import whitespace
from collections import Counter


def source_urls():
    '''input : a csv file
       output :  returns a list of all urls'''
    data = open('./redmaopython.csv','r') # enter the file name for file
    spamReader = csv.reader(data , delimiter='\t') # take col
    myUrls = make_t_url_list(spamReader, 23) #
    normalizeAllUrls(myUrls)
    
def write(path, text):    
    f = open(path, 'w')
    f.write(text) 
    f.close()
   
def normalizeAllUrls(myUrls):
    '''input : A list of space/tab separated urls
    output : A map of normalized urls
    myUrl is a string of many urls http://www.a.com http://    www.b.com ....'''
    c = Counter()
    for urlList in myUrls:
        for url in urlList.split(" "):
            write("JarkData.txt", normalizeURL(url))
            c[n] += 1
    write("JarkDataCounter.txt", normalizeURL(url))

def normalizeURL(url):
    '''input : a single URL
    output : a normalized URL'''
    call = "http://tldextract.appspot.com/api/extract?url=" + url    
    newUrl = urllib2.urlopen(call).read() 
    return newUrl

def make_t_url_list(t_reader,n):
    '''input : A reader (presumabley of a large, multi column csv)
    output : a list of urls extracted from column n of csv.'''
    tUrls = []
    for row in t_reader:
        if len(row) > n:
            tUrls.append(row[n-1]) ## not general needs fixing
    return tUrls

if __name__ == "__main__":
    source_urls()