Requirements for py_tld:

 - run on python2.7
 - fully commented 

 - input:  csv text files (< 2GB ) 
   - multiple urls in 23rd column 
     - need to be able to change to 26 column sometimes
     - need to see how I can just replace 23 with 26 in jarkmain.py in order to do so
 
 
   - URLS consist of subdomains, domains, top level domains
     - example: in "http://www.google.com/?abc=def":
     - 'www' is the subdomain
     - 'google' is the domain
     - 'com' is the tld
   

 - output: list of ordered URLs as defined by tldextract by count
   - format in .txt file that is excel-readable
   - need count of domains
   - the counts need be summed by file not by record/row
   * ideally would have the top level domains (e.g., .gov.uk) in a corresponding column 
     - so that the domains would be actionable and ready to open/call
     - example:
     
              google   [com, uk, de]
              yahoo    [com, gov, edu]
         
       instead of

              google    4
              yahoo     22




    



 
   

   