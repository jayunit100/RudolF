Goal of this program:

 - extract data from a file to prepare it for analysis in MySQL


Implementation:

 - extract URL domains from a column of a file (in this case, column 23)
 - create a list of url data from the column of the file
 - separate the top level from sub domain using Kurkowski's top level domain extractor
 - list the separated domains or results in order of frequency in a text file
 - prepare data in format suitable for MySQL
 
 
Dependencies:

 - python2.7 
 - tldextract (to install, do 'pip install tldextract' or 'pypm install extract')
