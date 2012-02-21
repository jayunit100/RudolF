import csv
import urllib2
import json


#####################################################################

def parseJark(path):
    '''in: path to a jarkfile
       out: jarkmodel'''
    data = open(path, 'r') # enter the file name for file
    spamReader = csv.reader(data, delimiter='\t') # take col
    return [line for line in spamReader]


def extractBaseUrls(jarkModel, columnno):
    '''in: jarkmodel
       out: a list of base urls from column number <columnno> of each row.  skips rows that are not long enough'''
    possiblyNestedList = [row[columnno - 1].split(" ") for row in jarkModel if len(row) > columnno]
    return reduce(lambda x,y: x + y, possiblyNestedList) # flatten the list


def readJark():
    '''this is a junk function; it should be placed in a separate file'''
    return extractBaseUrls(parseJark('samplecsv.txt'), 23)

#####################################################################

def makeTldUrl(baseUrl):
    '''input : a single base Url
       output : a Url for Tld consumption'''
    # TODO: check whether "baseUrl" needs to be encoded/escaped
    fullUrl = "http://tldextract.appspot.com/api/extract?url=" + baseUrl   
    return fullUrl


def getWebPage(url):
    '''input: a url
       output: the contents found at that url'''
    return urllib2.urlopen(url).read()


def parseTldResponse(string):
    return json.loads(string)


def sendTldUrls(baseUrls):
    '''input:  iterable of urls for tld consumption
       output: list of dictionaries of tld-ified urls'''
    tldUrls = [makeTldUrl(baseUrl) for baseUrl in baseUrls]
    tldResponses = [parseTldResponse(getWebPage(tldUrl)) for tldUrl in tldUrls]
    return tldResponses

#####################################################################


if __name__ == "__main__":
    # TODO: put this junk somewhere else
    urls = j.readJark()
    j.sendTldUrls(urls)

