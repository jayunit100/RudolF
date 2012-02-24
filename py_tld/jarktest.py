import unittest
import jarkalyze as j
import logging
import urllib2


LOG_FILENAME = "jarktestlog.txt"
logging.basicConfig(filename = LOG_FILENAME, level = logging.DEBUG, filemode = 'w')



class JarkfileTest(unittest.TestCase):
    
    def setUp(self):
        pass
    
    def testReadjarkfile(self):
        jm = j.parseJark('samplecsv.txt')
        self.assertEqual(len(jm), 3, "number of lines in jarkfile")
        
    def testExtractBaseUrls(self):
        inData = [[1,2,3,"abc def ghi"], [], []]
        extracted = j.extractBaseUrls(inData, 4)
        self.assertEqual(len(extracted), 3, "number of extracted things (wanted %i, got %i)" % (3, len(extracted)))
   
    
class TldTest(unittest.TestCase):
    
    def setUp(self):
        pass
        
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


def runTestSuite():
    testClasses = [JarkfileTest, TldTest, AnalysisTest]
    suites = [unittest.TestLoader().loadTestsFromTestCase(c) for c in testClasses]
    
    mySuite = unittest.TestSuite(suites)
    unittest.TextTestRunner(verbosity=2).run(mySuite)


if __name__ == "__main__":
    logging.info("starting unit tests of jarkalyze")
    runTestSuite()
    logging.info("finished running jarkalyze unittests")
