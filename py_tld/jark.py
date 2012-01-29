import sys
sys.path.append("/home/colbert/Desktop/Jark") # add path to file here
import t_specific
import csv
import re
import urllib.request
from string import whitespace
from collections import Counter

#input : a csv file
#output :  returns a list of all urls
def source_urls():
    data = open('./file.csv','r') # enter the file name for file
    spamReader=csv.reader(data , delimiter='\t') # take col
    myUrls = list()
    myUrls = make_t_url_list(spamReader,23) #
    normalizeAllUrls(myUrls)
    

#input : A list of space/tab separated urls
#output : A map of normalized urls
def normalizeAllUrls(myUrls):
    #myUrl is a string of many urls http://www.a.com http://    www.b.com ....
    c = Counter()
    for urlList in myUrls:
        for url in urlList.split(" "):
            n = normalizeURL(url);
            c[n] += 1
    return c;

#input : a single URL
#output : a normalize URL 
def normalizeURL(url):
    call = "http://tldextract.appspot.com/api/extract?url=" + url    
    normalizeUrl = urllib.request.urlopen(call).read()
    print (normalizeUrl);
    return normalizeUrl

#input : A reader (presumabley of a large, multi column csv)
#output : a list of urls extracted from column n of csv.
def make_t_url_list(t_reader,n):
    tUrls = list()
    for row in t_reader:
        if len(row) > n:
            tUrls.append(row[n-1]) ## not general needs fixing
    return tUrls

if __name__ == "__main__":
    source_urls()
