package demo03;

import java.sql.SQLException;

public class BankDAO {
	
	public int update(String cardno, float money) throws SQLException{
		String sql = "update account set balance = balance + ? where accountid = ? ";
		DBHelper db = new DBHelper();
		return db.update(sql, money, cardno);
	}
}
