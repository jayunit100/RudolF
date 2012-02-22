import jarkalyze as jk
import logging

LOG_FILENAME = "logging.txt"
logging.basicConfig(filename = LOG_FILENAME, level = logging.DEBUG, filemode = 'w')


def readJark(filename):
    '''this is a junk function; it should be placed in a separate file'''
    #edit this variable to run on a real file
    jarkModel = jk.parseJark(filename)
    return jk.extractBaseUrls(jarkModel, 23)


def runJark():
    logging.debug("reading jarkfile")
    urls = readJark("samplecsv.txt")
    logging.debug("sending urls to tld extractor")
    jk.sendTldUrls(urls)


if __name__ == "__main__":
    runJark()
