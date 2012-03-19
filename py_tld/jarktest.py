import unittest
import jarkalyze as j
import logging
import urllib2
import tldextract
import sys


LOG_FILENAME = "jarktestlog.txt"
logging.basicConfig(filename = LOG_FILENAME, level = logging.DEBUG, filemode = 'w')


#TODO Add an optional unit test for a large csv file - does this work on CSV
#files over over 10,000 lines?  100,000 ? 
class JarkfileTest(unittest.TestCase):
    
    def setUp(self):
        pass
    
    def testReadjarkfile(self):
        jm = j.parseJark('sample2.csv')
        self.assertEqual(len(jm), 3, "number of lines in jarkfile")
        
    def testExtractBaseUrls(self):
        inData = [
            [1,2,3,"abc def ghi"], 
            ["hello"], 
            ["col1", "col2", "column 3", "colum4 and there are 7 things here", "5col", "last"]
        ]
        extracted = j.extractBaseUrls(inData, 4)
        self.assertEqual(len(extracted), 10, 
                         "number of extracted things (wanted %i, got %i)" % (10, len(extracted)))
   
    
class TldTest(unittest.TestCase):
    
    def setUp(self):
        pass
    
    def testExtractUrl(self):
        url = "http://tldextract.appspot.com/api/extract?url=sttw://svyttovtrmvs.owsourxv.xom/ovws/oytroo-worow/stmo98/yotyyyx_557998.stmo"
        etd = tldextract.extract(url)
        logging.info("tld result: " + str(etd))
        self.assertEqual(etd.tld, "appspot.com", "extracted domain")
        self.assertEqual(etd.domain, "tldextract", "extracted subdomain")
        
    def testMakeTldDict(self):
        url = "http://tldextract.appspot.com/api/extract?url=sttw://svyttovtrmvs.owsourxv.xom/ovws/oytroo-worow/stmo98/yotyyyx_557998.stmo"
        etd = tldextract.extract(url)
        logging.info("tld result: " + str(etd))
        tdict = j.makeTldDict(etd)
        self.assertEqual(tdict["tld"], "appspot.com")
        self.assertTrue(tdict.has_key("subdomain"))
        self.assertTrue(tdict.has_key("domain"))
        self.assertEqual(3, len(tdict), "size of dictionary response") 
        
    def testTldUrls(self):
        urls = ["http://tldextract.appspot.com/api/extract?url=sttw://svyttovtrmvs.owsourxv.xom/ovws/oytroo-worow/stmo98/yotyyyx_557998.stmo",
                "http://tldextract.appspot.com/api/extract?url=sttw://www.yurrxy.uwvoo.vwu/ovwsovttvrs/rrrow-755577.stmo",
                "http://tldextract.appspot.com/api/extract?url=sttw://www.yurrxyrvvrvw.xom/ovws/yyyxsy%97soo%97to%97vrv%97uor%97ouurxv/-/979587/5575774/-/yvt759/-/ootro"]
        extracted = j.tldUrls(urls)
        expected = 3
        actual = len(extracted)
        self.assertEqual(expected, actual, "number of successful responses: expected %i, got %i" % (expected, actual))
        for e in extracted:
            self.assertTrue(e.has_key("domain"))
            self.assertTrue(e.has_key("subdomain"))
            self.assertTrue(e.has_key("tld"))
            self.assertEqual(3, len(e), "size of dictionary response")

    def testBadTldUrl(self):
        self.skipTest("not implemented")


class AnalysisTest(unittest.TestCase):
    
    def setUp(self):
        pass
    
    def testCountDomains(self):
        tlds = [{"domain": "whatever"}, {"domain":"tld"}, {"domain":"whatever"}, {"domain":"whatever"}]
        domainCounts = j.countDomains(tlds)
        self.assertEqual(2, len(domainCounts), "number of distinct domains")
        self.assertEqual(3, domainCounts["whatever"], "number of whatever domains")
    
    def testCountDomainsAndAssociateTlds(self):
        tlds = [{"domain":"whatever", "tld": ".com"}, 
                {"domain":"tld",      "tld": ".com"}, 
                {"domain":"whatever", "tld": ".com"}, 
                {"domain":"whatever", "tld": ".gov"}]
        domains = j.countDomainsAndAssociateTlds(tlds)
        self.assertEqual(2, len(domains), "number of distinct domains (got %d)" % len(domains))
        self.assertEqual(3, domains["whatever"]['count'], "times domain 'whatever' seen")
        self.assertEqual(set([".com", ".gov"]), domains["whatever"]['tlds'])
        
    def testBadCountDomainsInput(self):
        tlds = [{"badkey": "junkvalue"}]
        try:
            dCs = j.countDomains(tlds)
            self.assertTrue(False, "bad tld result")
        except KeyError as e:
            self.assertTrue(True, "good tld result")
        


def runTestSuite():
    testClasses = [JarkfileTest, TldTest, AnalysisTest]
    suites = [unittest.TestLoader().loadTestsFromTestCase(c) for c in testClasses]
    
    mySuite = unittest.TestSuite(suites)
    unittest.TextTestRunner(verbosity=2).run(mySuite)


if __name__ == "__main__":
    if len(sys.argv) > 1:
        logging.error("passed too many command-line args: " + str(sys.argv))
        raise ValueError("no command line args accepted -- please don't pass any")
    logging.info("starting unit tests of jarkalyze")
    runTestSuite()
    logging.info("finished running jarkalyze unittests")
