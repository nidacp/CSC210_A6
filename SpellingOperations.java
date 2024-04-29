import java.util.ArrayList;

/**
 *  Operations that should be supported by a spelling dictionary
 */
public interface SpellingOperations {
  /**
   *  @param query the word to check
   *  @return true if the query word is in the dictionary.
   */
  public boolean containsWord(String query);
  
  /**
   *  @param query the word to check
   *  @return a list of all valid words that are one edit away from the query
   */
  public ArrayList<String> nearMisses(String query);
}