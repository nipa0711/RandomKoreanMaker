package spellChk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class SpellChk {

	public String[] SpellCheck(String str) throws IOException {

		int wordCnt = str.split(" ").length;
		String[] originalWord = str.split(" ");

		URL url = new URL("http://speller.cs.pusan.ac.kr/PnuWebSpeller/lib/check.asp");
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("text1", str);

		StringBuilder postData = new StringBuilder();
		for (Map.Entry<String, Object> param : params.entrySet()) {
			if (postData.length() != 0)
				postData.append('&');
			postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			postData.append('=');
			postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);

		Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

		StringBuilder sb = new StringBuilder();
		for (int c; (c = in.read()) >= 0;)
			sb.append((char) c);
		String response = sb.toString();

		// System.out.println(response);
		String result[] = new String[wordCnt];
		String temp = "<input type='hidden' id='correctionTableSize' value='";
		int len = temp.length();
		int idx = response.indexOf("<input type='hidden' id='correctionTableSize' value='");
		String error = response.substring(idx + len, idx + len + 10);
		error = error.replaceAll("[^0-9]", "");
		int errCnt = Integer.parseInt(error);

		for (int i = 0; i < errCnt; i++) {
			temp = "<TD id='tdReplaceWord_" + i + "' class='tdReplace' >";
			len = temp.length();
			idx = response.indexOf("<TD id='tdReplaceWord_" + i + "' class='tdReplace' >");

			result[i] = response.substring(idx + len, idx + len + 10);

			String[] word = result[i].split("<br/>");
			result[i] = word[0];
			result[i] = result[i].replaceAll("[^ㄱ-ㅎ|가-힣|' ']", "");
			int cnt = 0;

			for (int j = 0; j < wordCnt; j++) {
				if (result[i].replaceAll(" ", "").equals(originalWord[j])) {
					originalWord[cnt] = result[i];
				} else if (Math.abs(originalWord[j].hashCode() - result[i].hashCode()) < 10000) {
					originalWord[cnt] = result[i];
				}
				cnt++;
			}
		}
		return originalWord;
	}
}
