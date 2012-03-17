## Goal of this program:

 - extract data from a file to prepare it for analysis in Excel



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
   - urllib2
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
   - see logging docs for more information

 - changing the column number where it looks for urls in input csv files
   - in "jarkmain.py", look in function "readJark(filename)":
   - replace COLUMN_NUMBER in "return jk.extractBaseUrls(jarkModel, <COLUMN_NUMBER>)"
 
