import unittest
import jarkalyze as j
import logging
import urllib2


LOG_FILENAME = "testLogging.txt"
logging.basicConfig(filename = LOG_FILENAME, level = logging.DEBUG, filemode = 'w')



class JarkfileTest(unittest.TestCase):
    
    def setUp(self):
        pass
    
    def testReadjarkfile(self):
        jm = j.parseJark('samplecsv.txt')
        self.assertEqual(len(jm), 3, "number of lines in jarkfile")
        
    def testExtractBaseUrls(self):
        pass
   
    
class TldTest(unittest.TestCase):
    
    def setUp(self):
        pass
    
    def testMakeTldUrl(self):
        myurl = "http://whatever.com"
        tld = "http://tldextract.appspot.com/api/extract?url="
        self.assertEqual(tld + myurl, j.makeTldUrl(myurl), "tld url maker")
        
    def testGetWebPage(self):
        content = j.getWebPage("http://www.google.com")
        self.assertTrue(len(content) > 5000, "page content")
        
    def testGetWebPageBadServerResponse(self):
        badUrl = "http://tldextract.appspot.com/api/extract?url=sttw://www.yurrxyrvvrvw.xom/ovws/yyyxsy%97soo%97to%97vrv%97uor%97ouurxv/-/979587/5575774/-/yvt759/-/ootro"
        self.assertRaises(urllib2.HTTPError, j.getWebPage, badUrl)
        
    def testGetWebPageBadUrl(self):
        self.assertRaises(ValueError, j.getWebPage, "abc")
        
    def testParseTldResponse(self):
        response = '{"domain": "abc", "subdomain": "", "tld": ""}'
        parsed = j.parseTldResponse(response)
        self.assertTrue(parsed.has_key("domain"))
        self.assertTrue(parsed.has_key("subdomain"))
        self.assertTrue(parsed.has_key("tld"))
        
    def testSendTldUrls(self):
        urls = ["http://tldextract.appspot.com/api/extract?url=sttw://svyttovtrmvs.owsourxv.xom/ovws/oytroo-worow/stmo98/yotyyyx_557998.stmo",
                "http://tldextract.appspot.com/api/extract?url=sttw://www.yurrxy.uwvoo.vwu/ovwsovttvrs/rrrow-755577.stmo",
                "http://tldextract.appspot.com/api/extract?url=sttw://www.yurrxyrvvrvw.xom/ovws/yyyxsy%97soo%97to%97vrv%97uor%97ouurxv/-/979587/5575774/-/yvt759/-/ootro"]
        extracted = j.sendTldUrls(urls)
        expected = 2
        actual = len(extracted)
        self.assertEqual(expected, actual, "number of successful responses: expected %i, got %i" % (expected, actual))
        for e in extracted:
            self.assertTrue(e.has_key("domain"))
            self.assertEqual(3, len(e), "size of dictionary response")

    def testBadTldUrl(self):
        pass


def runTestSuite():
    suite1 = unittest.TestLoader().loadTestsFromTestCase(JarkfileTest)
    suite2 = unittest.TestLoader().loadTestsFromTestCase(TldTest)
    
    mySuite = unittest.TestSuite([suite1, suite2])
    unittest.TextTestRunner(verbosity=2).run(mySuite)


if __name__ == "__main__":
    logging.info("starting unit tests of jarkalyze")
    runTestSuite()
    logging.info("finished running jarkalyze unittests")
