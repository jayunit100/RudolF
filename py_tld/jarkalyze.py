import csv
import logging
import collections
import tldextract


mylogger = logging.getLogger("jarkalyze")

#read a full csv file and log every line read . 
def preRead(path):
    data = open(path,'r')
    reader = csv.reader(data,delimiter='\t')
    lines = list()
    i=0;
    for l in reader:
        mylogger.info("Done reading line ---> %d "% i)
        mylogger.info("length=%d " % len(l))
        lines.append(l);
        i=i+1

#####################################################################
# read, parse, and extract data-of-interest from jarkfiles
# this class serves as the model class for the application, it runs 
# tldextract for inputs, 
def parseJark(path):
    '''purpose: read a file on the filesystem, parse, 
          and return the parse result data structure
       in: path to a jarkfile, which is a tab-delimited file
       out: list of lines, where each line is a list of strings'''
    mylogger.info("opening file " + str(path))
    
    #TODO someday remove this line, its just a helper
    #to help debug large spreadsheets or data sets.
    preRead(path)

    # assume file exists and we have read permissions for it
    data = open(path, 'r')
    mylogger.info("Now reading file " + str(path))
    spamReader = csv.reader(data, delimiter='\t')

    lines = [line for line in spamReader]
    mylogger.info("found %d lines in file" % len(lines))
    data.close()
    return lines


def extractBaseUrls(jarkModel, columnno):
    '''purpose: given a list of rows with columns, extract a 
           specific column from each row
       in: 1) list of rows, where each row is a list of columns.
           2) column number (this is 1-indexed!)
       out: a list of base urls from column number <columnno> of each row.  
            skips rows that are not long enough'''
    possiblyNestedList = []
    coi = columnno - 1 # the column of interest.  remember that python lists are 0-indexed!
    for row in jarkModel:
        if len(row) > coi:
            possiblyNestedList.append(row[coi].split(" "))
            mylogger.debug("found row with %i columns" % len(row))
        else:
            mylogger.warning("found row with fewer columns (%i) than required (%i) ... skipping" % 
                             (len(row), columnno))
    # flatten the list
    return [item for sublist in possiblyNestedList for item in sublist]


#####################################################################
# tld extraction

def makeTldDict(tldResult):
    '''purpose: convert tld named-tuple to dict, as dicts are more commonly
          used, and easier to use, in python
       in:  a tld named-tuple
       out:  a dictionary with the tld tuple's attributes as keys'''
    return {
        'domain':    tldResult.domain,
        'subdomain': tldResult.subdomain,
        'tld':       tldResult.tld
    }

#TODO : Test this on large file, I think
#it should be done lazily otherwise, the list might
#run out of memory.
def tldUrls(urls):
    '''purpose: given a bunch of urls, run tldextract on each of them
       input:  iterable of urls for tld consumption
       output: list of dictionaries of tld-ified urls
       if any of the tld urls cause an exception, that url is skipped and the error is simply logged'''
    tldResponses = []
    for url in urls:
        mylogger.debug("attempting to tld <%s>" % url)
        try:
            tldResponses.append(makeTldDict(tldextract.extract(url)))
            mylogger.debug("successfully tld-ed url")
        except Exception, e:
            mylogger.error("tld url <%s> failed with message <%s>" % (url, str(e)))
    return tldResponses

#####################################################################
# result analysis 

def countDomains(tldResults):
    '''purpose: given a bunch of tld-extracted results, for each domain:
          1) count the total number of times it was seen
       in: an iterable of tldResults (dictionaries)
       out: a dictionary of (key: domain) (value: number of times domain seen)'''
    domains = [res["domain"] for res in tldResults]
    ctr = collections.Counter(domains)
    return dict(ctr)


def countDomainsAndAssociateTlds(tldResults):
    '''purpose: given a bunch of tld-extracted results, for each domain:
          1) count the total number of times it was seen
          2) provide a list of all top-level-domains that were seen with that domain
       in: an iterable of tldResults (dictionaries)
       out: a dictionary of:
          keys: domains
          values: have keys 
             1) "count": number of times domain seen
             2) "tlds": top-level domains seen with that domain (set)'''
    domains = {}
    for res in tldResults:
        dom = res["domain"]
        if not domains.has_key(dom):
            # if we haven't seen the domain yet:
            #    set up a new domain dictionary with "neutral" values:
            domains[res["domain"]] = {
                "count": 0,      # the number of times we've seen the domain
                "tlds": set([])  # the top-level domains seen with the domain: empty set
            }
        # we've seen the domain one more time
        domains[dom]["count"] += 1
        # make sure the associated tld is in the domain's set of tlds
        domains[dom]["tlds"].add(res["tld"])
    return domains


def makeDomainLines(domains):
    '''purpose: convert domain results to a row-based format
       in: output from countDomainsAndAssociateTlds
       out: a list of tuples with items:
          1) domain
          2) count
          3) unordered list of associated tlds'''
    return [(domain, ddata["count"], list(ddata["tlds"])) for (domain, ddata) in domains.items()]
