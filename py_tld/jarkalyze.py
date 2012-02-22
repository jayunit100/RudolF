import csv
import urllib2
import json
import logging
import collections


mylogger = logging.getLogger("jarkalyze")


#####################################################################
# jarkfiles

def parseJark(path):
    '''in: path to a jarkfile
       out: jarkmodel'''
    mylogger.debug("opening jarkfile " + str(path))
    data = open(path, 'r')
    mylogger.debug("reading jarkfile " + str(path))
    spamReader = csv.reader(data, delimiter='\t') # take col
    lines = [line for line in spamReader]
    mylogger.debug("found %d lines in jarkfile" % len(lines))
    return lines


def extractBaseUrls(jarkModel, columnno):
    '''in: jarkmodel
       out: a list of base urls from column number <columnno> of each row.  
            skips rows that are not long enough'''
    possiblyNestedList = [row[columnno - 1].split(" ") for row in jarkModel if len(row) > columnno]
    return reduce(lambda x,y: x + y, possiblyNestedList) # flatten the list


#####################################################################
# url-munging and web access

def makeTldUrl(baseUrl):
    '''input : a single base Url
       output : a Url for Tld consumption'''
    # TODO: check whether "baseUrl" needs to be encoded/escaped
    fullUrl = "http://tldextract.appspot.com/api/extract?url=" + baseUrl
    mylogger.debug("built tld url " + fullUrl) 
    return fullUrl


def getWebPage(url):
    '''input: a url
       output: the contents found at that url
       or an exception if the server responds with an error'''
    mylogger.debug("trying to grab url <%s>" % url)
    openedurl = urllib2.urlopen(url)
    text = openedurl.read()
    mylogger.debug("successfully grabbed url and read response")
    return text


def parseTldResponse(string):
    return json.loads(string)


def sendTldUrls(baseUrls):
    '''input:  iterable of urls for tld consumption
       output: list of dictionaries of tld-ified urls
       if any of the tld urls cause an exception, that url is skipped and the error is simply logged'''
    tldUrls = [makeTldUrl(baseUrl) for baseUrl in baseUrls]
    tldResponses = []
    for tldUrl in tldUrls:
        try:
            tldResponses.append(parseTldResponse(getWebPage(tldUrl)))
        except urllib2.HTTPError, e:
            mylogger.error("tld url <%s> failed with message <%s>" % (tldUrl, e.msg))
    return tldResponses

#####################################################################
# result analysis 

def countDomains(tldResults):
    '''in: an iterable of tldResults
       out: a dictionary of (key: domain) (value: number of times domain seen)'''
    domains = [res["domain"] for res in tldResults]
    ctr = collections.Counter(domains)
    return dict(ctr)
