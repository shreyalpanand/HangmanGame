import java.util.*;

public class HangmanManager {

	private int totalGuesses;
	private Set<String> wordsRemain;
	private SortedSet<Character> guessMade;
	private String initialPattern;

	// Constructs the HangmanManager class
	public HangmanManager(Collection<String> dictionary, int length, int max) {
		if(length < 1 || max < 0) {
			throw new IllegalArgumentException();
		} else {
			wordsRemain = new TreeSet<String>();
			for(String next : dictionary) {
				if(next.length() == length) {
					wordsRemain.add(next);
				}
			}
			initialPattern = "";
			for(int i = 0; i < length; i++) {
				initialPattern += "- ";
			}
			guessMade = new TreeSet<Character>();
			totalGuesses = max;
		}
	}

	// This method gives access to the current set of words
	public Set<String> words() {
		return wordsRemain;
	}

	// This method tells how many guesses are left
	public int guessesLeft() {
		return totalGuesses;
	}

	// This method tells the letters that are already guessed by the user
	public SortedSet<Character> guesses() {
		return guessMade;
	}

	// This method returns the current pattern
	// It throws an IllegalStateException if the set of words is empty
	public String pattern() {
		if(wordsRemain.size() == 0) {
			throw new IllegalStateException();
		}
		return initialPattern;
	}

	// This method records all the updates that are made by the player
	// It throws an IllegalStateException if the number of guesses is less than 0
	// or if the the set of words is empty
	// It throws an IllegalArgumentException if the guess has already been made before
	// It returns the number of times the guessed letter appears in the pattern
	public int record (char guess) {
		if(guessesLeft() < 1 || wordsRemain.size() == 0) {
			throw new IllegalStateException();
		} else if(wordsRemain.size() != 0 && guessMade.contains(guess)) {
			throw new IllegalArgumentException();
		} else {
			Map<String, Set<String>> guessWord = new TreeMap<String, Set<String>>();
			getPattern(guessWord, guess);
			return getPatternGroup(guessWord, guess);
		}
	}

	// This method forms the pattern by considering the guess made by player
	// and then forms the value of Set<String> for each pattern(key)
	private void getPattern(Map<String, Set<String>> guessWord, char guess) {
		for(String word : wordsRemain) {
			String pattern = "";
			for(int i = 0; i < word.length(); i++) {
				if(word.charAt(i) != guess) {
					pattern = pattern + initialPattern.charAt(i * 2) + " ";
				} else {
					pattern = pattern + guess + " ";
				}
			}
			Set<String> samePattern = new TreeSet<String>();
			if(guessWord.containsKey(pattern)) {
				samePattern = guessWord.get(pattern);
			}
			pattern.trim();
			samePattern.add(word);
			guessWord.put(pattern, samePattern);
		}

	}

	// This method gets the pattern that has the Set of maximum size
	// This pattern decreases of the number of guesses left if the guess is wrong
	// This method also counts the number of times the letter is repeated in the pattern
	private int getPatternGroup(Map<String, Set<String>> guessWord, char guess) {
		int maxCount = 0;
		String newPattern = "";
		for(String pattern : guessWord.keySet()) {
			if(guessWord.get(pattern).size() > maxCount) {
				maxCount = guessWord.get(pattern).size();
				newPattern = pattern;

				wordsRemain = guessWord.get(newPattern);
			}
		}
		int countLetter = 0;
		for(int i = 0; i < newPattern.length(); i++) {
			if(newPattern.charAt(i) == guess) {
				countLetter++;
			}
		}
		if(countLetter == 0) {
			totalGuesses --;
		}
		initialPattern = newPattern;
		guessMade.add(guess);
		return countLetter;
	}
}
