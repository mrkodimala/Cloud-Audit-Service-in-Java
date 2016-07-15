import java.sql.*;

public class ModfifyOriginalData {
	Connection connection;
	Statement statement;
	ResultSet result;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ModfifyOriginalData m=new ModfifyOriginalData();
		m.handleModifyOriginalData(240,"data1_pdf");
	}
	public void handleModifyOriginalData(int BlockNo,String tablename){
		String originaldata=getOriginalBlock(BlockNo, tablename);
		StoreOriginalData(BlockNo, tablename, originaldata);
	}
	
	private void StoreOriginalData(int BlockNo,String tablename,String originaldata){
		makeconnection("mahi", "mahi1234");
		try{
			String query= "update "+tablename+" set block='"+originaldata+"' where id="+BlockNo;
			//System.out.println(query);
			statement.executeQuery(query);
		}catch(Exception e){
			e.printStackTrace();
			//System.out.println("Error Occured in this set original data function");
		}
	}
	
	private String getOriginalBlock(int BlockNo,String tablename){
		String finalstring="";
		makeconnection("cpymahi","mahi1234");
		try{
			String query="select block from "+tablename+" where id="+BlockNo;
			//System.out.println(query);
			result=statement.executeQuery(query);
			while(result.next()){
				finalstring=result.getString(1).toString();
				System.out.println(finalstring);
			}
			connection.close();
		}catch(Exception e){
			e.printStackTrace();
			//System.out.println("Error occuered in get original data block");
		}
		
		return finalstring;
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
