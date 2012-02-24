import jarkalyze as jk
import logging
import json
import sys

LOG_FILENAME = "jarkmainlog.txt"
logging.basicConfig(filename = LOG_FILENAME, level = logging.DEBUG, filemode = 'w')


def readJark(filename):
    '''in: path to jark file
       out: list of extracted base urls from the 23rd columns'''
    jarkModel = jk.parseJark(filename)
    return jk.extractBaseUrls(jarkModel, 23)


def runJark(inpath, outpath):
    logging.info("reading jarkfile")
    urls = readJark(inpath)
    logging.info("sending urls to tld extractor")
    tldResults = jk.tldUrls(urls)
    logging.info("got tld results")
    counts = jk.countDomains(tldResults)
    logging.info("got domain counts")
    logging.info("opening output file <%s>" % outpath)
    outfile = open(outpath, 'w')  # TODO -- use a context manager
    logging.info("writing domain counts to file")
    outfile.write(json.dumps(counts))
    logging.info("done writing")
    outfile.close()
    logging.info("file closed")


def printHelp():
    print "usage: <this program> <path to jarkfile> <where to write output file>"


def jarkify():
    if len(sys.argv) == 1:
        printHelp()
        return
    arg = sys.argv[1]
    if arg == "-h":
        printHelp()
    else:
        try:
            out = sys.argv[2]
            runJark(arg, out)
        except Exception, e:
            logging.error("encountered fatal error: %s" % str(e))
            raise e


if __name__ == "__main__":
    jarkify()
