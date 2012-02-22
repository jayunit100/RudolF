import unittest
import jarkalyze as j
import logging


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
    def testParseTldResponse(self):
        response = '{"domain": "abc", "subdomain": "", "tld": ""}'
        parsed = j.parseTldResponse(response)
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


if __name__ == "__main__":
    logging.debug("starting unit tests of jarkalyze")
    runTestSuite()
    logging.debug("finished running jarkalyze unittests")
