import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.*;

public class SpellingDictionary implements SpellingOperations {
    private HashSet<String> dictionary;
    // to figure out regular expressions, use regex101.com
    
    /**
     * Constructor of dictionary, creates a dictionary hashset. Reformats to lowercase & removes punctuation.
     * @param filename  String name of file the user wants to parse through to make up the dictionary
     * @return          HashSet<String> of reformatted words from the file
     */
    public HashSet<String> createDict(String filename) {
        // create dictionary hashset
        // open the file "filename" using a Scanner
        //Read in the file line by line
        // For each line, reformat as lowercase and remove punctuation
        //add the word on that line to the hashset

        Scanner file = null;
        if (filename.length()>0) {
            try {
                file = new Scanner(new File(filename));
            } catch (FileNotFoundException e) {
                System.err.println("Cannot locate file.");
                System.exit(-1);
            }
        } else {
            file = new Scanner(System.in);
        }

        HashSet<String> dictionary = new HashSet<>();

        while(file.hasNextLine()) {
            String temp = file.nextLine();
            temp = temp.replaceAll("[\\p{Punct}]", "");

            String[] sentence = temp.split(" ");
            for(int i=0; i<sentence.length; i++) {
                dictionary.add(sentence[i].toLowerCase());
            }
            /*if(!file.hasNextLine()) {
                System.out.println(temp);
            }*/
        }

        file.close();
        this.dictionary=dictionary;

        return dictionary;
    }

      /**
   *  @param query the word to check
   *  @return true if the query word is in the dictionary.
   */
  public boolean containsWord(String query) {
    return this.dictionary.contains(query.toLowerCase());
  }
  
  /**
   *  @param query the word to check
   *  @return a list of all valid words that are one edit away from the query
   */
  public ArrayList<String> nearMisses(String query) {
    query=query.toLowerCase();

    ArrayList<String> misses = new ArrayList<>();
    for(int i=0; i<query.length(); i++) {
        // delete a character: delete one letter at a time, add that string to a list of possibilities
        String x = query.substring(0, i) + query.substring(i+1, query.length());
        if(containsWord(x) && !misses.contains(x)) {
            misses.add(x);
        }

        // if not at the end of the string, swap char i with the char ahead of it
        if(i!=query.length()-1) {
            char a = query.charAt(i);
            char b = query.charAt(i+1);
            String c;
            if(i==query.length()-2) {
                c = query.substring(0, i) + b + a;
            }
            else {
                c = query.substring(0, i) + b + a + query.substring(i+2, query.length());
            }
            if(containsWord(c) && !misses.contains(c)) {
                misses.add(c);
            }
        }

        // check for splits
        String first = query.substring(0, i);
        String last = query.substring(i,query.length());
        String split = first + " " + last;
        if(containsWord(first) && containsWord(last) && !misses.contains(split)) {
            misses.add(split);
        }

        for(char alphabet = 'a'; alphabet <='z'; alphabet++) {
            // replace one leter at a time, add that string to a list of possibilities
            String y = query.substring(0, i) + alphabet + query.substring(i+1, query.length());
            if(containsWord(y) && !misses.contains(y)) {
                misses.add(y);
            }

            // add one letter at a time, add that string to a list of possibilities
            String z = query.substring(0, i) + alphabet + query.substring(i, query.length());
            if(containsWord(z) && !misses.contains(z)) {
                misses.add(z);
            }
        }
    }

    return misses;
  }

  /**
   * Tests for each nearMisses feature.
   * @param args    unused
   */
  public static void main(String[] args) {
    SpellingDictionary myDictionary = new SpellingDictionary();
    myDictionary.createDict("words.txt");
    //System.out.println(myDictionary.dictionary);

    // missing letter
    if(!myDictionary.nearMisses("catle").contains("cattle")) {
        System.out.println(myDictionary.nearMisses("catle"));
        throw new AssertionError("Failed test 1.");
    }
    // added letter
    if(!myDictionary.nearMisses("cattlle").contains("cattle")) {
        System.out.println(myDictionary.nearMisses("cattlle"));
        throw new AssertionError("Failed test 2.");
    }
    // substituted letter
    if(!myDictionary.nearMisses("caxtle").contains("cattle")) {
        System.out.println(myDictionary.nearMisses("caxtle"));
        throw new AssertionError("Failed test 3.");
    }
    // swapped letters
    if(!myDictionary.nearMisses("cattel").contains("cattle")) {
        System.out.println(myDictionary.nearMisses("cattel"));
        throw new AssertionError("Failed test 4");
    }
    // splits
    if(!myDictionary.nearMisses("cattell").contains("cat tell")) {
        System.out.println(myDictionary.nearMisses("cattell"));
        throw new AssertionError("Failed test 5.");
    }


}

}
