package demo03;

import java.io.IOException;
import java.util.Properties;

public class MyPropertise extends Properties{
	private static MyPropertise instance = null;
	private MyPropertise(){
		try {
			this.load(MyPropertise.class.getClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static MyPropertise getInstance(){
		if (instance == null)
			instance = new MyPropertise();
		return instance;
	}
}