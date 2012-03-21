## Goal of this program:

 - extract domain and top-level domain data from a file to prepare it for analysis in Excel
 - see [the tldextract documentation](https://github.com/john-kurkowski/tldextract)
     for more information on what a tld is
 
     
     
## Todos

 - see file `todos.md`
 


## Implementation:

 - extract URL domains from a column of a file (in this case, column 23)
 - create a list of url data from the column of the file
 - separate the top level from sub domain using Kurkowski's top level domain extractor
 - list the separated domains or results in order of frequency in a text file
 - prepare data in format suitable for Excel
 

 
## Dependencies:

 - python runtime:
   - python2.7 

 - "standard" libraries (should come bundled with python2.6 and above):
   - json
   - logging
   - collections

 - "non-standard" libraries -- can be downloaded from python package index:
   - tldextract 

   
   
## Before starting:

 - run the unit tests

        prompt> python jarktest.py

 - make sure they ALL pass



## Using the program:

 - from the command line:

        prompt> python jarkmain.py <path to jarkfile> <where to write output file>

 - example:
   
        prompt> python jarkmain.py sample1.csv out1.txt
        
 - logging is written to "jarkmainlog.txt" in the current working directory
 
        
 
## Modifying the program:

 - changing the logging level
   - look for this line in "jarkmain.py":

     logging.basicConfig(filename = LOG_FILENAME, level = logging.INFO, filemode = 'w')

   - change "level" to something lower (logging.DEBUG) or higher (logging.WARNING)
   - see [logging docs](http://docs.python.org/library/logging.html) for more information

 - changing the column number where it looks for urls in input csv files
   - in "jarkmain.py", look in function "readJark(filename)":
   - replace COLUMN_NUMBER in "return jk.extractBaseUrls(jarkModel, <COLUMN_NUMBER>)"
   
   
## Known issues

 - tldextract seems to have a problem where it fails the first time it is used, but
    succeeds subsequently.
    After setting up this project, run the unit tests.  If they fail, try them again,
    noting whether they fail in the same way.

## 3/19/2012 RUNNING ON REAL DATA 
   Running on real data sets is expected to cause issues (memory, file formats w/ lots of columns or large columns,etc..) 
   In particular, its not clear wether some special logic is required to read a large CSV in python.  
   There is a debugging statement which preReads the file, and logs to "logging.txt" that will 
   output what line has been read. If a file is bad, you can trace these logs to see 
   where the output stops, and the bad line will be around that area.  


 
