import jarkalyze as jk
import logging

LOG_FILENAME = "jarkmainlog.txt"
logging.basicConfig(filename = LOG_FILENAME, level = logging.DEBUG, filemode = 'w')


def readJark(filename):
    '''in: path to jark file
       out: list of extracted base urls from the 23rd columns'''
    jarkModel = jk.parseJark(filename)
    return jk.extractBaseUrls(jarkModel, 23)


def runJark():
    logging.info("reading jarkfile")
    urls = readJark("samplecsv.txt")
    logging.info("sending urls to tld extractor")
    jk.sendTldUrls(urls)
    logging.info("finished")


if __name__ == "__main__":
    runJark()
