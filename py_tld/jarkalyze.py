import csv
import urllib2
import json
import logging
import unittest


mylogger = logging.getLogger("jarkalyze")


#####################################################################

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

def makeTldUrl(baseUrl):
    '''input : a single base Url
       output : a Url for Tld consumption'''
    # TODO: check whether "baseUrl" needs to be encoded/escaped
    fullUrl = "http://tldextract.appspot.com/api/extract?url=" + baseUrl   
    return fullUrl


def getWebPage(url):
    '''input: a url
       output: the contents found at that url'''
    mylogger.debug("trying to grab url <%s>" % url)
    openedurl = urllib2.urlopen(url)
    mylogger.debug("response object: " + str(dir(openedurl)))
    text = openedurl.read()
    return text


def parseTldResponse(string):
    return json.loads(string)


def sendTldUrls(baseUrls):
    '''input:  iterable of urls for tld consumption
       output: list of dictionaries of tld-ified urls'''
    tldUrls = [makeTldUrl(baseUrl) for baseUrl in baseUrls]
    tldResponses = [parseTldResponse(getWebPage(tldUrl)) for tldUrl in tldUrls]
    return tldResponses

#####################################################################
# tests

class JarkfileTest(unittest.TestCase):
    def setUp(self):
        pass
    def testReadjarkfile(self):
        jm = parseJark('samplecsv.txt')
        self.assertEqual(len(jm), 3, "number of lines in jarkfile")
    def testExtractBaseUrls(self):
        pass
    
class TldTest(unittest.TestCase):
    def setUp(self):
        pass
    def testMakeTldUrl(self):
        myurl = "http://whatever.com"
        tld = "http://tldextract.appspot.com/api/extract?url="
        self.assertEqual(tld + myurl, makeTldUrl(myurl), "tld url maker")
    def testGetWebPage(self):
        content = getWebPage("http://www.google.com")
        self.assertTrue(len(content) > 5000, "page content")
    def testParseTldResponse(self):
        response = '{"domain": "abc", "subdomain": "", "tld": ""}'
        parsed = parseTldResponse(response)
        self.assertTrue(parsed.has_key("domain"))
        self.assertTrue(parsed.has_key("subdomain"))
        self.assertTrue(parsed.has_key("tld"))
    def testSendTldUrls(self):
        pass
    def testBadTldUrl(self):
        pass

def runTestSuite():
    suite1 = unittest.TestLoader().loadTestsFromTestCase(JarkfileTest)
    suite2 = unittest.TestLoader().loadTestsFromTestCase(TldTest)
    
    mySuite = unittest.TestSuite([suite1, suite2])
    unittest.TextTestRunner(verbosity=2).run(mySuite)
