package t1;

import java.io.UnsupportedEncodingException;

public class Test {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		DFA dfa = new DFA();
		System.out.println(dfa.searchKeyword("中国"));
	}

}
