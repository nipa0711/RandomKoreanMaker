package data_analysis;

import java.util.ArrayList;
import java.util.List;

import org.snu.ids.kkma.ma.MExpression;
import org.snu.ids.kkma.ma.MorphemeAnalyzer;
import org.snu.ids.kkma.ma.Sentence;

public class dataAnalysis {
	private String string;
	private ArrayList <String []> saveList = new ArrayList <>();

	public ArrayList<String[]> getSaveList() {
		return saveList;
	}

	public void setSaveList(ArrayList<String[]> saveList) {
		this.saveList = saveList;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public void analysisStr() {
		try {
			// init MorphemeAnalyzer
			MorphemeAnalyzer ma = new MorphemeAnalyzer();
			
			// create logger, null then System.out is set as a default logger
			ma.createLogger(null);

			// analyze morpheme without any post processing 
			List<MExpression> ret = ma.analyze(string);

			// refine spacing
			ret = ma.postProcess(ret);

			// leave the best analyzed result
			ret = ma.leaveJustBest(ret);

			// divide result to sentences
			List<Sentence> stl = ma.divideToSentences(ret);
			
			for( int i = 0; i < stl.size(); i++ ) {
				Sentence st = (Sentence) stl.get(i);
				for( int j = 0; j < st.size(); j++ ) {
					int a = st.get(j).toString().indexOf("[") + 1;
					int b = st.get(j).toString().indexOf("]");
					String temp = st.get(j).toString().substring(a, b);
					String [] tempList = temp.split("/");
					for (int k = 0; k < tempList.length; k++) {
						tempList[k] = tempList[k].replaceAll("[^ㄱ-ㅎ|가-힣|a-z|A-Z]","");
					}
					saveList.add(tempList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
