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
    '''in: domain line as tuple of domain, count, and list of tlds
       out: string of domain, count, list of tlds delimited by tabs'''
    return '\t'.join([domainLine[0], str(domainLine[1]), str(domainLine[2])])


def runJark(inpath, outpath):
    '''purpose: analyze a jarkfile and write the results to a new file
       in:  path to input file, path to output file
       out:  void
       
       Procedure:
       1) read & parse input file.  extract urls
       2) run tldextract on each url
       3) analyze domains and tlds
       4) convert analysis results into rows (for excel-formatted output)
       5) sort rows by domain count (descending)
       6) format rows as string of tab-delimited columns
       7) write '\n'-delimited rows to file as string
    '''
    logging.info("reading jarkfile")
    urls = readJark(inpath)                                   # step 1
    logging.info("sending urls to tld extractor")
    tldResults = jk.tldUrls(urls)                             # step 2
    logging.info("got <%d> tld results" % len(tldResults))
    domains = jk.countDomainsAndAssociateTlds(tldResults)     # step 3
    logging.info("got <%d> domain counts and associated tlds" % len(domains))
    domainLines = jk.makeDomainLines(domains)                 # step 4
    logging.info("made <%d> domain lines" % len(domainLines))
    sortedDomainLines = sorted(domainLines, key = lambda line: -line[1])  # step 5
    logging.info("sorted domain lines")
    formattedLines = [formatLine(dline) for dline in sortedDomainLines]   # step 6
    logging.info("opening output file <%s>" % outpath)
    outfile = open(outpath, 'w')
    logging.info("writing results to file")
    outfile.write('\n'.join(formattedLines))                  # step 7
    logging.info("done writing")
    outfile.close()
    logging.info("file closed")


def printHelp():
    print "usage: <this program> <path to jarkfile> <where to write output file>"


def jarkify():
    if len(sys.argv) == 1:     # if there's only one arg (program name)
        printHelp()            # just print the help
        return                 # and exit
    arg = sys.argv[1]
    if arg == "-h":            # if the first arg is "-h"
        printHelp()            # just print the help (ignore any other args)
    else:
        try:                   # otherwise run the data analysis
            out = sys.argv[2]
            runJark(arg, out)
        except Exception, e:   # if any exception is encountered
            logging.error("encountered fatal error: %s" % str(e))
            raise              # log it and re-raise


if __name__ == "__main__":
    jarkify()
