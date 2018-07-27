package spellChk;

import java.io.IOException;

public class test {

	public static void main(String[] args) throws IOException {
		SpellChk sc = new SpellChk();
		String str = "네는 셋을 들여다보다";
		String[] arr = sc.SpellCheck(str);
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}
	}

}
