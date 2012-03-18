import jarkalyze as jk
import logging
import sys


LOG_FILENAME = "jarkmainlog.txt"
logging.basicConfig(filename = LOG_FILENAME, level = logging.INFO, filemode = 'w')


def readJark(filename):
    '''in: path to jark file
       out: list of extracted base urls from the 23rd columns'''
    jarkModel = jk.parseJark(filename)
    return jk.extractBaseUrls(jarkModel, 23)


def formatLine(domainLine):
    # format:  domain, count, list of tlds
    return '\t'.join([domainLine[0], str(domainLine[1]), str(domainLine[2])])


def runJark(inpath, outpath):
    logging.info("reading jarkfile")
    urls = readJark(inpath)
    logging.info("sending urls to tld extractor")
    tldResults = jk.tldUrls(urls)
    logging.info("got <%d> tld results" % len(tldResults))
    domains = jk.countDomainsAndAssociateTlds(tldResults)
    logging.info("got <%d> domain counts and associated tlds" % len(domains))
    domainLines = jk.makeDomainLines(domains)
    logging.info("made <%d> domain lines" % len(domainLines))
    sortedDomainLines = sorted(domainLines, key = lambda line: -line[1])
    logging.info("sorted domain lines")
    formattedLines = [formatLine(dline) for dline in sortedDomainLines]
    logging.info("opening output file <%s>" % outpath)
    outfile = open(outpath, 'w')
    logging.info("writing results to file")
    outfile.write('\n'.join(formattedLines))
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
            raise


if __name__ == "__main__":
    jarkify()
