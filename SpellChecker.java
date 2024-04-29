import java.util.HashSet;
import java.util.Scanner;

public class SpellChecker {
    
    /**
     * Checks for user-inputted content to spell check, either as a file inputted in the terminal as "java SpellChecker.java < [FILE].txt" or as words as "java SpellChecker.java words words words"
     * @param args  String array of inputted through the terminal
     */
    public static void main(String[] args) {
        SpellingDictionary myDictionary = new SpellingDictionary();
        myDictionary.createDict("words.txt");

        // reading file
        if(args.length==0) {

            Scanner scanner = new Scanner(System.in);
            HashSet<String> misspelt = new HashSet<>();

            while(scanner.hasNextLine()) {
                String temp = scanner.nextLine();
                temp = temp.replaceAll("[\\p{Punct}]", "");
    
                String[] sentence = temp.split(" ");
                for(int i=0; i<sentence.length; i++) {
                    String word = sentence[i].toLowerCase();
                    //sonnet.add(sentence[i].toLowerCase());
                    if(!myDictionary.containsWord(word) && !misspelt.contains(word)) {
                        System.out.println("The word '" + word + "' is not found in the dictionary. Here are some suggestions:");
                        System.out.println("    " + myDictionary.nearMisses(word));
                        misspelt.add(word);
                    }
                }
            }
            // print nothing if spelled correctly
            // print message + suggestions if misspelt
            // do not print if a duplicate -- hashtable to check which misspelt words have already been encountered
            scanner.close();
        }

        // reading input
        else {
            //System.out.println(" Checking: " + Arrays.toString(words));
            for(int i=0; i<args.length; i++) {
                if(myDictionary.containsWord(args[i])) {
                    System.out.println(args[i] + " is spelled correctly.");
                }
                else {
                    System.out.println("Not found: " + args[i]);
                    System.out.println("    Suggestions: " + myDictionary.nearMisses(args[i]));
                }
            }
        }

    }
}
