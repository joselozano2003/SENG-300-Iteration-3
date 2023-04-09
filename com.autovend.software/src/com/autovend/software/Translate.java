package com.autovend.software;


// Class containing static translate methods.
// All of these methods take as arguments strings in english, and return
// translated strings in the specified language.
//
// Support for more languages can easily be added in the future by adding
// more methods to this class.
public class Translate {
	
	/*
	 * Translates text to language specified by lang_code.
	 * Could make a dictionary here, but I don't think it would simplify
	 * things much more than they already are. To expand
	 * 
	 * text: text to be translated.
	 * lang_code: string code representing language to translate to.
	 * 
	 * SUPPORTED LANGUAGE CODES (to be expanded):
	 * 
	 * English - "ENG"
	 * L language - "LLL" (translates all strings to 'L')
	 * 
	 */
	public static String translate(String text, String lang_code) {
		
		if (lang_code == "ENG") return toEnglish(text);
		else if (lang_code == "LLL") return toL(text);
		
		else return null;
		
	}

	/*
	 * Translates passed string (english text) to english.
	 */
	public static String toEnglish(String text) {
		return text;		// Since text is assumed to be in English, return as-is
	}
	
	
	/*
	 * Translates passed string (english text) to L language.
	 */
	public static String toL(String text) {
		return "L";
	}
	
	
	// Main - only used for testing.
	public static void main(String args[]) {
		System.out.println(toL("Hello world"));
	}
	
}

