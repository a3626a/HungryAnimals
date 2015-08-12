package oortcloud.hungryanimals.configuration;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

public class StringParser {
	
	/**
	 * 
	 * @param input
	 * @param spliter
	 * @return splitByLevel(input, '(', ')', ',')
	 */
	public static String[] splitByLevel(String input) {
		return splitByLevel(input, '(', ')', ',');
	}
	
	/**
	 * 
	 * @param input
	 * @param spliter
	 * @return splitByLevel(input, '(', ')', spliter)
	 */
	public static String[] splitByLevel(String input, char spliter) {
		return splitByLevel(input, '(', ')', spliter);
	}
	
	/**
	 * 
	 * @param input
	 * @param left
	 * @param right
	 * @param spliter
	 * @return for input "(...)", this won't be split. "(..),(..)" will be split with the outer ',' 
	 */
	public static String[] splitByLevel(String input, char left, char right, char spliter) {
		ArrayList<String> temp = new ArrayList<String>();
		int level = 0;
		int prevIndex = 0;
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == left) {
				level++;
			}
			if (input.charAt(i) == right) {
				level--;
			}
			if (level==0&&input.charAt(i) == spliter) {
				temp.add(input.substring(prevIndex, i));
				prevIndex=i+1;
			}
		}
		temp.add(input.substring(prevIndex));
		String[] ret = new String[temp.size()];
		ret = temp.toArray(ret);
		return ret;
	}
	
	public static String reduceLevel(String input, char left, char right) {
		int level = 0;
		
		if (input.startsWith(new String(new char[]{left})) && input.endsWith(new String(new char[]{right}))) {
			for (int i = 1; i < input.length()-1; i++) {
				if (input.charAt(i) == left) {
					level++;
				}
				if (input.charAt(i) == right) {
					level--;
				}
				if (level<0) {
					return input;
				}
			}
		}
		
		if (level > 0) {
			return input;
		}
		
		if (input.charAt(0) == left && input.charAt(input.length()-1) == right) {
			return input.substring(1, input.length()-1);
		}
		
		return input;
		
	}
	
	public static String reduceLevel(String input) {
		return reduceLevel(input, '(', ')');
	}

}
