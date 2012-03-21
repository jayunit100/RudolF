
 - test jarkalyze.tldUrls on a large file, I think it should be done lazily otherwise, the list might run out of memory

 - add an optional unit test for a large csv file - does this work on CSV files over over 10,000 lines?  100,000 ? 
 
 - jarkmain.readJark is untested - add unit test that verifies the column number input is valid
 
 - someday remove the call to jarkalyze.preRead (in function jarkalyze.parseJark) -- it's just a helper to help debug large spreadsheets or data sets
    