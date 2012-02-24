import csv
import urllib2
import json
import logging
import collections
import tldextract


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
# tld extraction

def makeTldDict(tldResult):
    '''in:  a tld named tuple
       out:  a dictionary with the tld tuple's attributes as keys'''
    return {
        'domain': tldResult.domain,
        'subdomain': tldResult.subdomain,
        'tld': tldResult.tld
    }


def tldUrls(urls):
    '''input:  iterable of urls for tld consumption
       output: list of dictionaries of tld-ified urls
       if any of the tld urls cause an exception, that url is skipped and the error is simply logged'''
    tldResponses = []
    for url in urls:
        mylogger.debug("attempting to tld <%s>" % url)
        try:
            tldResponses.append(makeTldDict(tldextract.extract(url)))
            mylogger.debug("successfully tld-ed url")
        except Exception, e:
            mylogger.error("tld url <%s> failed with message <%s>" % (url, str(e)))
    return tldResponses

#####################################################################
# result analysis 

def countDomains(tldResults):
    '''in: an iterable of tldResults
       out: a dictionary of (key: domain) (value: number of times domain seen)'''
    domains = [res["domain"] for res in tldResults]
    ctr = collections.Counter(domains)
    return dict(ctr)
