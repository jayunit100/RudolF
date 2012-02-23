need a few things clarified before we can refactor:


Q1) a) what is `make_t_url_list` supposed to do?  
    b) why is it only extracting column n?  
    c) why is there a comment `not general needs fixing`?

A1) a) it (maybe) is intended to filter only on col 23 from a specific csv file and create a list of the data therein 
    b) not 100% sure but we are only interested in that column of the file
    c) that (maybe) is a comment about this script meaning that for general use we dont need to specify column 23



Q2) is `normalizeURL` supposed to build a URL or go download a web page?

A2) it is supposed to loop through the myUrls list and normalize each url by calling the tld extractor by kurkowski so that we get a normalized list of top level domains from the file



Q3) why is there a counter in `normalizeAllUrls`?

A3) counter is there to store and COunt relative frequency of each top level domain from file



Q4) `23` in `source_urls` is a magic number that we need to get rid of -- what does it mean?

A4) that (may) refer to the column number which indicates where the data is that we want to work with; the rest of the data in the file is extraneous

Q5) We found that some of the URL inputs are invalid (i.e. they crash the tldextract server on appengine).  a) What are the 
valid / invalid characters in a URL ?  b) Is it okay to have % signs ? -'s ? In any case, we need to refactor the test file
to include only sane URLs .  And eventually, we will need to update the validateURL method to check for such constraints.. 

A5)  a) A valid URL is a type of URI (Uniform Resource Identifier is a compact string of characters
   for identifying an abstract or physical resource).  Although many URL schemes are named after protocols, this does not
   imply that the only way to access the URL's resource is via the named protocol.  Gateways, proxies, caches, and name resolution services
   might be used to access some resources, independent of the protocol of their origin, and the resolution of some URL may require the use
   of more than one protocol (e.g., both DNS and HTTP are typically used to access an "http" URL's resource when it can't be found in a local
   cache).   

For more info see: http://www.ietf.org/rfc/rfc2396.txt. 

     b) In general URIs as defined by RFC 3986 may contain any of the following characters: A-Z, a-z, 0-9, -, ., _, ~, :, /, ?, #, [, ], @, !, $, &, ', (, ), *, +, ,, ; and =. Any other character needs to be encoded with the percent-encoding (%hh). Each part of the URI has further restrictions about what characters need to be represented by an percent-encoded word.

For more info see: http://stackoverflow.com/questions/1547899/which-characters-make-a-url-invalid  

