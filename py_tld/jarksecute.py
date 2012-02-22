import jarkalyze as jk
import logging

LOG_FILENAME = "logging.txt"
logging.basicConfig(filename = LOG_FILENAME, level = logging.DEBUG, filemode = 'w')


def readJark():
    '''this is a junk function; it should be placed in a separate file'''
    #edit this variable to run on a real file
    testpath = 'samplecsv.txt'
    jarkModel = jk.parseJark(testpath)
    return jk.extractBaseUrls(jarkModel, 23)


if __name__ == "__main__":
    # TODO: put this junk somewhere else
    logging.debug("reading jarkfile")
    urls = readJark()
    logging.debug("sending urls to tld extractor")
    jk.sendTldUrls(urls)
else:
    raise Exception("this file should only be run as a top-level script")
