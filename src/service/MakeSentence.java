package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import db.DBWork;
import spellChk.SpellChk;

public class MakeSentence {
	private DBWork dw = new DBWork();
	private SpellChk sc = new SpellChk();
	private Random rand = new Random();
	
	public void basicSentence(String tableName) throws IOException {
		ArrayList <String> IC = dw.select("IC", tableName);
		ArrayList <String> MDT = dw.select("MDT", tableName);
		ArrayList <String> NNG = dw.select("NNG", tableName);
		ArrayList <String> JKS = dw.select("JKS", tableName);
		ArrayList <String> JKO = dw.select("JKO", tableName);
		ArrayList <String> MAG = dw.select("MAG", tableName);
		ArrayList <String> VV = dw.select("VV", tableName);
		int icLength = IC.size();		// 감탄사
		int mdtLength = MDT.size();		// 일반 관형사
		int nngLength = NNG.size();		// 고유 명사
		int jksLength = JKS.size();		// 주격 조사
		int jkoLength = JKO.size();		// 목적격 조사
		int magLength = MAG.size();		// 일반 부사
		int vvLength = VV.size();		// 동사
		
		for (int i = 0; i < 10; i++) {
			String output = "";
			if (icLength > 0)
				output += IC.get(rand.nextInt(icLength)) + "! ";
			if (magLength > 0)
				output += MAG.get(rand.nextInt(magLength)) + " ";
			if (mdtLength > 0)
				output += MDT.get(rand.nextInt(mdtLength)) + " ";
			if (nngLength > 0 && jksLength > 0)
				output += checkRule(NNG.get(rand.nextInt(nngLength)), JKS.get(rand.nextInt(jksLength)));
			else {
				i--;
				continue;
			}
			if (nngLength > 0 && jkoLength > 0)
				output += checkRule(NNG.get(rand.nextInt(nngLength)), JKO.get(rand.nextInt(jkoLength)));
			else {
				i--;
				continue;
			}
			if (vvLength > 0)
				output += VV.get(rand.nextInt(vvLength)) + "다.";
			else {
				i--;
				continue;
			}
			System.out.println((i+1) + " : " + output);
		}
	}
	
	public void basicSentenceWithSpell(String tableName) throws IOException {
		ArrayList <String> NNG = dw.select("NNG", tableName);
		ArrayList <String> JKS = dw.select("JKS", tableName);
		ArrayList <String> JKO = dw.select("JKO", tableName);
		ArrayList <String> VV = dw.select("VV", tableName);
		int nngLength = NNG.size();		// 고유명사
		int jksLength = JKS.size();		// 주격조사
		int jkoLength = JKO.size();
		int vvLength = VV.size();		// 형용사
		for (int i = 0; i < 10; i++) {
			String temp = NNG.get(rand.nextInt(nngLength)) 
						+ JKS.get(rand.nextInt(jksLength))
						+ " " + NNG.get(rand.nextInt(nngLength))
						+ JKO.get(rand.nextInt(jkoLength))
						+ " " + VV.get(rand.nextInt(vvLength)) + "다";
			System.out.println(temp);
			String [] tempS = sc.SpellCheck(temp);
			String output = "";
			for (int j = 0; j < tempS.length; j++) {
//				if (tempS[j] == null)
//					output += tempO[j] + " ";
//				else {
//					if(tempS[j].equals(temp)) {
//						output = temp;
//						break;
//					} else {
//						output += tempS[j] + " ";
//					}
//				}
				output += tempS[j] + " ";
			}
			System.out.println(output);
			System.out.println("===============");
		}
	}
	
	public String checkRule(String NNG, String JK) {
		String tempjk = JK;
		String seletedValue = null;
		if (tempjk.equals("서"))
			tempjk = "은";
		else if (tempjk.equals("더러"))
			tempjk = "을";
		char lastName = NNG.charAt(NNG.length() - 1);
        
        // 한글의 제일 처음과 끝의 범위밖일 경우는 오류
        if (lastName < 0xAC00 || lastName > 0xD7A3) {
            return NNG;
        }
                
        // 은/는 or 이/가 부분을 확인
        if (tempjk.equals("은") || tempjk.equals("는")) {
        	seletedValue = (lastName - 0xAC00) % 28 > 0 ? "은" : "는";
        } else if (tempjk.equals("이") || tempjk.equals("가")) {
        	seletedValue = (lastName - 0xAC00) % 28 > 0 ? "이" : "가";
        } else if (tempjk.equals("을") || tempjk.equals("를")) {
        	seletedValue = (lastName - 0xAC00) % 28 > 0 ? "을" : "를";
        }
        return NNG+seletedValue+" ";
		
	}
}
