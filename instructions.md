# A3: Spell Checking

The purpose of this assignment is to give you some experience using hash tables. For this project, you will implement a spell-checking program that stores its dictionary in a hash table (implemented in the Java code by class [`HashSet`](https://docs.oracle.com/javase/8/docs/api/java/util/HashSet.html)).

## Overview

The heart of this assignment is the `SpellDictionary` class.  This class will read in and store word spellings from a dictionary file (`words.txt`).  It will provide a method to check whether a given word is spelled correctly by checking whether it is in the dictionary.  It will also provide a method to suggest possible alternatives in the event of a misspelt word.  These suggestions will be formed from the set of "near misses" -- valid words that are a single edit away from the misspelled word.

We have arranged this assignment into two phases and recommend that you complete them in order.  Successful completion of phase one will earn at least a B.  Phase two comprises the remaining points on the assignment and has two subparts. 

- For phase one, you will develop a `SpellDictionary` class to create a dictionary of valid words using a HashSet.  You should also test this class in `Main` (not required, but strongly recommended).

- For phase two you will write a `SpellChecker` class that operates in two possible modes:
  - In command-line mode it will provide explicit feedback on the spelling of specific words. 
  - In file scanning mode it  takes input from a file and prints suggestions for any words that are misspelled.  

## Phase 1:  SpellDictionary Class

### Constructor
The constructor should meet the following specifications:
1. Accepts a string as input, corresponding to a filename (you can test your code using the provided file named `words.txt`) 
2. Opens the file
3. Allocates an empty `HashSet`
4. Populates the HashSet by reading words one at a time from the file

### Getter & Setter Methods
The class will not provide any getters/setters because other parts of your program shouldn't need to access it directly. You don't want to create opportunities for a user to make accidental changes to the program's reference list of words.

### Other Methods
The two operations your class should provide are listed in the interface `SpellingOperations`. Some more information about them:

* You need to provide a method `containsWord` to check whether the dictionary for a particular spelling. It should take a string (a word) and return `true` or `false` depending on whether it is found in the dictionary.

* You should also provide a `nearMisses` method that provides information about words that `containsWord` determines to be misspelled. It should identify words in the dictionary that are exactly one edit away.  To do this, construct all possible strings that are one edit away and check each against the dictionary. You will then want to return any that are real words. Make sure the list you return doesn't contain any duplicates. 

#### `nearMisses` Method

You should consider the following types of near misses:

1. **Deletions**: Delete one letter from the word. 
    - Example: **catttle** -> **cattle**
    - Number of Possibilities: *n* possibilities for a word of length *n*
2. **Insertions**: Insert one letter into the word at any point. 
    - Example: **catle** -> **cattle**
    - Number of Possibilities: 26*(*n*+1) possibilities for a word of length *n* 
3. **Substitutions**: Replace one character with another. 
    - Example: **caxtle** -> **cattle**
    - Number of Possibilities: 25**n* possibilities for a word of length *n*
4. **Transpositions**: Swap two adjacent characters. 
    - Example: **cattel** -> **cattle**
    - Number of Possibilities: *n*-1 possibilities for a word of length *n* 
5. **Splits**: Divide the word into two legal words. For this kind of near miss, the pair of words together should be recorded as a single entry, with a space between them. 
    - Example: **cattell** -> **cat tell**
    - Number of Possibilities: *n*-1 possibilities for a word of length *n*


Note: There may be more than one way to generate a particular near miss using the above rules. The list returned by `nearMisses` should still include it only once.  Hint:  Think creatively about how you might eliminate any duplicates most efficiently.

#### `Main` Method

Similar to our testing paradigm on the first assignment of the semester, you should write tests to verify that `SpellDictionary` is functioning properly.  You are encouraged to use use JUnit, but another option is to write tests that you check by hand.

Your goal should be to make at least **one test of each specific behavior** of the dictionary class.  In particular, that means trying out _each style of near-miss_ to ensure that they are included in the suggestions. Put your tests in the `Main` class.

Think about edge cases (like word endings and beginnings) and anything else that might be tricky.  You want to test your class thoroughly -- we'll also be running automated tests featuring some of our favorite misspelt words.

### Strategy Suggestions

* While debugging, you might want to have your program print out all the near-miss candidates it creates for some short (two-letter) nonsense word. Inspect this list carefully to make sure all desired candidates are present and correct, and there are no duplicates.

* Your program can ignore capitalization and punctuation. You can do this by converting all words to lower case before checking their spelling and by making sure that the ``Scanner`` skips over punctuation marks.  If you like you may implement a more sophisticated handling of capitalization -- please describe the choice in your readme in case you do.

* `SpellDictionary` should not print anything or interact directly with the user -- that will be the job of other classes.  Imagine that this is a class we might hope to use in many ways -- for the text-based checking described here, in an email program, for a window-based word processor, etc.  Printing to the `System.out` would not make sense for many of these modalities.

## Phase 2 Details: SpellChecker

The `SpellDictionary` class described above is not designed for user interaction.  That role falls to `SpellChecker`:  its primary job is to interact with the user. Ideally, there willl be two modes: one where it checks words read in as arguments, and another where it checks all words contained in a file.  Both modes of operation will be initiated via the command line.

### Words Provided as Arguments

You will run the program from the terminal by typing something like the following:

    java SpellChecker qest questt quest

The program will run and might then respond with something like:

    Not found:  qest
      Suggestions:  cest est fest gest hest jest mest nest quest rest test vest yest zest 
    Not found:  questt
      Suggestions:  quest quests 
    'quest' is spelled correctly.

In the example, we provided three words to be checked, and the program responds to each one.  Note that in this style of program invocation, the three words are **command line arguments**, and they are relayed to your program via the `String[] args` parameter of the `main` function.  When command line arguments are provided, the program prints a message about every word, whether it is correct or not, and makes suggestions for incorrect words.  This is the first mode of operation.

### Words Read from a File

The second mode of operation is invoked like this (PowerShell users, please see notes below):

    java SpellChecker < sonnet.txt

The `<` in this mode indicates that we are sending the contents of the file `sonnet.txt` to the program for spell checking (this is called [input redirection](https://www.science.smith.edu/~nhowe/teaching/csc210/Tutorials/redir.php).  You will know that this mode has been invoked because `args.length` in `main` will be 0 (unlike the previous mode).  Detecting this, your program then will read the file contents using a `Scanner` taking input from `System.in`.

In this mode of operation, the program should read individual words from the input file and check their spelling.  If the word is spelled correctly, the program should **silently** move on to the next word -- it shouldn't print a message. On the other hand, if a word is misspelt, your program should print a message and offer suggestions.  This message should happen *only* the first time a particular misspelling is encountered, even if the same misspelling appears several times. Think critically about which data structures might help you make this happen efficiently.

You have some freedom about how to arrange the code into methods within this class.  Probably you will want separate methods for each mode of operation; you may also refine things further.  It is ok to use only static methods in this class.

Once you have your program working, you will want to test it out. Here's a sonnet in Shakespeare's original spelling that would make a good candidate:

<pre style="left-margin:0.5in; font-style:italic">Shall I compare thee to a Summers day?
Thou art more louely and more temperate:
Rough windes do shake the darling buds of Maie,
And Sommers lease hath all too short a date:
Sometime too hot the eye of heauen shines,
And often is his gold complexion dimm'd,
And euery faire from faire some-time declines,
By chance, or natures changing course vntrim'd
But thy eternal Sommer shall not fade,
Nor loose possession of that faire thou ow'st,
Nor shall death brag thou wandr'st in his shade,
When in eternall lines to time thou grow'st,
So long as men can breath or eyes can see,
So long liues this, and this giues life to thee.
</pre>

### Notes on Design
This is a good opportunity to practice what you have learned about class design. Specifically, try to put methods in logical places **based upon the roles of the classes**:
- Think of ``SpellDictionary`` as a class that might be used by other programs -- a word processor, for example, or an email program -- so try to keep it general, while still offering useful and powerful methods. 
- Your ``SpellCheck`` program is one specific application. If you design ``SpellDictionary`` well, then you won't have to write too much code for ``SpellCheck``... but you don't want ``SpellDictionary`` catering too much to ``SpellCheck``'s specific implementation!

Note for Powershell Users: The `<` does not work in PowerShell. Instead, you will want to use the syntax:

      Get-Content sonnet.txt | & java SpellChecker

### Acknowledgment

_The concept for this assignment is due to [David Eck](http://math.hws.edu/eck/)_