<link rel="stylesheet" type="text/css" href="stylesheets/mdextra.css" />

---
title: markdown example for rudolf blog
---

[How-to source for creating markdown pages](http://xlson.com/2010/11/09/getting-started-with-github-pages.html)

Some issues:

 - automagic rename to "example.html": don't know how this works
 - can files under a personal page interfere with project pages if they have the same name?
 - haven't found the full documentation for how this works
 - don't understand what the first three lines do.  are they required?  can they be augmented?


But still, it's great to be able to easily:

 1. create numbered lists
 2. make things **bold**
 3. do *italics*
 4. insert [links](https://github.com/)


## What else?
===============

---------------

I don't know, how about this quote:

> Speak softly and carry a big stick.

Would you like to see some code?

    (defn derive
      [f h]
      (fn [x]
        (/ (- (f (+ x h))
              (f x))
           h)))