import java.sql.*;

public class RefreshClass {
	public Connection connection;
	private Statement statement;
	public ResultSet result;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RefreshClass r=new RefreshClass();
		r.getDocuments();
	}
	public void getDocuments(){
		makeconnection("mahi","mahi1234");
		try{
			result=statement.executeQuery("select table_name from user_tables");
		}catch(Exception e){
			
		}
	}
	
	private void makeconnection(String username,String password)
	{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",username,password);
			statement=connection.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
