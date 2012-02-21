import csv
import urllib2


def parseJark(path):
    '''in: path to a jarkfile
       out: jarkmodel'''
    data = open(infile,'r') # enter the file name for file
    spamReader = csv.reader(data , delimiter='\t') # take col
    return spamReader


def extractBaseUrls(jarkModel, columnno):
    '''in: jarkmodel
       out: a list of base urls from column number <columnno> of each row.  skips rows that are not long enough'''
    return [row[columnno - 1] for row in jarkModel if len(row) > columnno]


def makeFullUrl(baseUrl):
    '''input : a single base Url
    output : a full Url'''
    # TODO: check whether "baseUrl" needs to be encoded/escaped
    fullUrl = "http://tldextract.appspot.com/api/extract?url=" + baseUrl   
    return fullUrl


def readWebPage(url):
    '''input: a url
    output: the contents at that url'''
    return urllib2.urlopen(url).read()