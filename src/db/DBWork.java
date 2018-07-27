package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.regex.Pattern;

import dbconfig.oracle.OracleConfig;

public class DBWork {
	static Connection conn = null;
	static PreparedStatement pstmt = null;
	static ResultSet rs = null;

	public void con() {
		try {
			Class.forName(OracleConfig.DRIVER);
			conn = DriverManager.getConnection(OracleConfig.URL, OracleConfig.USER, OracleConfig.PASSWORD);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void discon() {
		try {
			if (rs != null)
				rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// 약 23분
	public synchronized void insert(String[] list, String tableName) {
		int length = list.length;
		int index = 1;
		con();
		while (index < length) {
			if (list[index].length() > 0 && list[index].trim() != "" && list[index - 1].trim() != "") {
				if (list[index] != null && list[index - 1] != null) {
					if (Pattern.matches("^[A-Z]*$", list[index].trim())) {
						if (!list[index].trim().substring(0, 1).equals("B")
								&& !list[index].trim().substring(0, 1).equals("C")) {
							String sql = "insert into " + tableName + "(\"" + list[index].trim() + "\") values (?)";
							try {
								Thread.sleep(50);
								pstmt = conn.prepareStatement(sql);
								pstmt.setString(1, list[index - 1]);
								pstmt.executeUpdate();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			index++;
		}
		discon();
	}

	public ArrayList<String> select(String tag, String tableName) {
		con();
		ArrayList<String> result = new ArrayList<>();
		String sql = "select distinct " + tag + " from " + tableName + " where " + tag + " is not null";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		discon();
		return result;
	}

	public void deleteAll(String tableName) {
		con();
		String sql = "delete from " + tableName;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			System.out.println("delete table " + tableName + " OK !!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		discon();
	}
}
