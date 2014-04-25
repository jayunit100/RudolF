#Amino Acid Predictor:
 
 Start a REPL then load in the namespace and functions. 
 
##The two main functions are:

#1.  
                                           (com-pare "A" "B" "C" "D")  
  Enter the three arguments as paths to plain text files with peptide sequences 

  Inputs:

   * `"A"` : Path to protein used to train the classifier.  
   * `"B"` : Path to protein with gaps to test.  
   * `"C"` : Path to the same protein as argument `"B"` without any gaps. Used to score the accuracy of the classifier

  Output:

   * `"D"` : Name of output file. Outputs the result in the current directory.
  
  To execute:                     
       
                             (com-pare "order.txt" "orderX.txt" "order.txt" "out.txt") 

                                 
                                     

  Generate `"order.txt" "orderX.txt" "order.txt"` using the procedure below. 
    
  Notice the files `"A"` and `"C"` are the same. This means the file used to train the classifier is the 
  same as the one used to score it. Since this data is artificially generated, the model should find 
  patterns in the data, and it does.  To generate this data in the REPL type:

                            (order-protein 100 ["LRKDC" "YDEST" "ESGPI"] "order.txt")       

  The order-protein function shuffles the three 5-letter chunks, `"LRKDC" "YDEST" "ESGPI"`, concatenates them,
  shuffles them again, concatenates them to the previous and so on 100 times. This yields a string of
  1500 characters written out as plain text to the file `order.txt`, in the current directory.
  To generate `orderX.txt` simply replace any 4 Gs with X in `order.txt` using a find/replace in any text editor.
    
  To understand why the classifier works so well on the order.txt data it is necessary to understand 
  the classification process: When com-pare takes in a training peptide sequence, `"A"`, it breaks it up into
  5 letter chunks, e.g., `LRKDC`, also called  neighborhoods. It then partitions all of the chunks into disjoint
  classes based on the amino acid in the center, yielding 21 classes. For example `LRKDC` belongs to the `K-class`. 
  In general, `XX?XX` belongs to the `?-class`, where `X` is any amino acid. 
  Each `?-class` is a set of training examples that trains the `?-model` to guess `?` from the four surrounding
  amino acids. For example, an `A-class` could be `{KLASG MNART}` where `KLASG` and `MNART` are examples for `A`.
  The features for each example `AB?CD`, where `A,B,C,D` are any amino acids and NOT amino acid abbreviations,
  are: `AD, BC, A, D, B, C, AB and CD`. Since any of `A, B, C and D` can have 21 values, they are
  `(21^2)*4 + 21*4 = 1848` possible features for each example. The hash-map, protein-neighborhood, associates
  each of the possible features with a unique index using the following language for the features:

                       (AD, BC, A, D, B, C, AB, CD)  -->  (AD02, BC01, 1A, D2, 1B, C1, 1AB, CD1) (1)

  The features of the hash-map are in the form on the RHS of `(1)` and two element combinations are computed 
  using the cartesian product in `clojure.contrib.combinatorics`. The hash-map, protein-neighborhood
  has 1848 key value pairs in the form: 

                                        {AL01 33, 2D 66, 1LL 1330, R2 1847, ...} 

  where the pairs are unordered.The a sparse feature vector in `R^1848` is constructed from each example. 
  The operations are defined  so only non zero values need to appear. A feature vector could be:

                                   {66 1, 44 1, 1010 1, 33 1, 3939 1, 0 1, 99 1, 2333 1} 

  They are always 8 non-zero entries in each feature vectorsince 8 possible features can be extracted for each example. 
  Notice the values are always 1, atypical of SVMs.

  This section requires a basic understanding of [Support Vector Machines] (http://en.wikipedia.org/wiki/Support_vector_machine). 
  The `?-model` is trained by projecting onto (via the inner product) feature vectors derived from the `?-class`, and away from 
  feature vectors derived from the 20 other classes. Clearly they're a lot more negative examples than positive examples for each class. 
  From the training data, `"A"`, com-pare builds a model for each of the 21 amino acids and tests each model on `"B"`.
  Thus multiple amino acids may be proposed for each gap, `X`. com-pare returns a sequence, in file `"D"`, with N+1 
  elements. N is the number of X's in `"B"`. The first element is always the overall score, correct/total, 
  how many X's were guessed correctly based on file `"C"`. The rest of the ordered elements correspond to the Xs in file `"B"`  
  the ith element of the sequence in `"D"` corresponds to the ith X in file `"B"`, indexed from left to right. Create an example
  output `"D"` by typing: 

                                   (com-pare "order.txt" "orderX.txt" "order.txt" "out.txt") 

  in the REPL. `out.txt` reads:

                                       ([:score 1] [\G "TG"] [\G "TG"] [\G "TG"] [\G "TG"])

  For each element after the score in the form `[a b]`, `a` corresponds to the actual value of the gap and `b` is an 
  ordered sting, ordered from RIGHT TO LEFT of guesses for the gap  In the case of `[\G "TG"]`, `\G` means `G` is the
  actual value and `"TG"` means `G` is the most likely guess and `T` is the second likely guess. 
      

#2. 
                                                       (show-plots "A" "B")

  * `"A"`: The amino acid whose learning statistics will display
  * `"B"`: The learning statistics graphs for a given amino acid using Incanter

  try:  

                                                    (show-plots "A" "Anole.txt") 


 and compare to: 

                                                    (show-plots "G" "order.txt")


 after making ordered data via: 

                                        (order-protein 100 ["LRKDC" "YDEST" "ESGPI"] "order.txt")  

  
 The vector of models, model-matrix, (a vector of vectors) transforms the binary classification problem into 
 a 21 classifier problem. Notice the training of the models is intrinsically parallel.

 A number of functions are provided for manipulating protein text data in the supporting functions section. 

 Overall, This project does exactly what it's designed to, but real protein sequences are not uniformly
 structured locally in five piece chunks. Nonetheless, it introduces a general way to define features and
 many of the ideas and code are reusable to solve other problems.



email @ sesanker0@gmail.com
