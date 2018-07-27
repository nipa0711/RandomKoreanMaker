package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import data_analysis.dataAnalysis;
import db.DBWork;

public class WordService {
	DBWork dw = new DBWork();
	private String input;
	
	public void start() throws IOException, SQLException, Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		dataAnalysis da = new dataAnalysis();
		MakeSentence ms = new MakeSentence();
		boolean flag = true;
		while (flag) {
			System.out.println("1.Partial Analysis 2.All Analysis 3.Input all data 4.Terminate");
			System.out.print("Input : ");
			input = "";
			int n = Integer.parseInt(br.readLine());
			switch (n) {
			case 1:
				dw.deleteAll("WORDSNOTALL");
				File file = new File("src/sample_part.txt");
				FileReader fr = new FileReader(file);
				BufferedReader br1 = new BufferedReader(fr);
				String line = "";
				while((line = br1.readLine()) != null)
					input += line;
				da.setString(input);
				System.out.println("+++Analyzing text data+++");
				da.analysisStr();
				System.out.println("+++Analyzing text data is DONE!+++");
				System.out.println("+++Start to input analyzed text data into DB+++");
				for (String [] temp : da.getSaveList())
					dw.insert(temp, "WORDSNOTALL");		// DB 작업
				System.out.println("+++DB input operation finished!+++");
				ms.basicSentence("WORDSNOTALL");
				break;
			case 2:
				ms.basicSentence("WORDS");
				break;
			case 3:
				// Almost 23 min.
				try {
					System.out.print("This process is too wrong. Run? [y/n] :");
					String jebal = br.readLine();
					if (jebal.trim().equals("y")) {
						File file2 = new File("src/sample.txt");
						FileReader fr2 = new FileReader(file2);
						BufferedReader br2 = new BufferedReader(fr2);
						String linee = "";
						while((linee = br2.readLine()) != null)
							input += linee;
						br2.close();
						System.out.println("***Start***");
						da.setString(input);
						da.analysisStr();
						for (String [] temp : da.getSaveList()) {
							dw.insert(temp, "WORDS");
							System.out.println("+++");
						}
						System.out.println("***End***");
					} else {
						System.exit(0);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case 4:
				System.out.println("Terminated.");
				flag = false;
				break;
			default:
				System.out.println("Wrong Input");
			}
		}
	}
}
