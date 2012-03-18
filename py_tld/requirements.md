Requirements for py_tld:


 - run on python2.7


 - fully commented 


 - input:  csv text files (< 2GB ) 
   - multiple urls in 23rd column 
     - need to be able to change to 26 column sometimes
       - need comments showing how to replace 23 with 26
 
   - URLS consist of subdomains, domains, top-level domains
     - example: in "http://www.google.com/?abc=def":
     - 'www' is the subdomain
     - 'google' is the domain
     - 'com' is the tld or top-level domain
   

 - output: list of tld-extracted URLs as defined by the python tldextract module
   - columns:  
     1. domain
     2. number of times domain seen
     3. list of distinct corresponding top-level domains (e.g., .gov.uk)
   - ordered by count (descending)
   - format in .txt file that is excel-readable
   - sum counts by file not by record/row
     
Example of output:
     
       domain     count     associated TLD's
      -----------------------------------------------
       google      22       [com, uk, de]
       yahoo       18       [com, gov, edu]
   