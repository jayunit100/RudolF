import jarkalyze as jk
import logging
import json

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
    tldResults = jk.sendTldUrls(urls)
    logging.info("got tld results")
    counts = jk.countDomains(tldResults)
    logging.info("got domain counts")
    outpath = "results.txt"
    logging.info("opening output file <%s>" % outpath)
    outfile = open(outpath, 'w')  # TODO -- use a context manager
    logging.info("writing domain counts to file")
    outfile.write(json.dumps(counts))
    logging.info("done writing")
    outfile.close()
    logging.info("file closed")


if __name__ == "__main__":
    runJark()
