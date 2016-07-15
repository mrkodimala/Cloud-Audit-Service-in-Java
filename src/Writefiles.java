import java.io.*;
import java.sql.*;

public class Writefiles {
	Connection connection;
	Statement stmt;
	ResultSet rs;
	OutputStream out;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Writefiles w=new Writefiles();
		w.handleWriteFilesClass("grayscale.jpg");
	}
	public void handleWriteFilesClass(String filename){
		String finalname;
		try{
			String s=System.getProperty("user.home");
			out=new FileOutputStream(s+"\\Downloads\\"+filename);
			finalname=ConvertToTableName(filename);
			makeConnection();
			getDataFromDatabase(finalname);
			WriteDataToFile();
			connection.close();
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private String ConvertToTableName(String tablename){
		char tblname[]=tablename.toCharArray();
		int length=tblname.length;
		for(int i=0;i<length;i++)
		{
			
			if(tblname[i]=='.'){
				tblname[i]='_';
			}
		}
		String finalname=new String(tblname);
		return finalname;
	}
	
	private void makeConnection(){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","mahi","mahi1234");
			stmt=connection.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getDataFromDatabase(String tablename){
		String query="select * from "+tablename;
		try {
			rs=stmt.executeQuery(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String convertData(String s){
		String finalstring="";
		int strlen=s.length();
		String temp="";
		for(int i=0;i<strlen;i++)
		{
			if(s.charAt(i)!='$'){
				temp=temp+s.charAt(i);
			}else{
				finalstring=finalstring+(char)Integer.parseInt(temp);
				temp="";
			}
		}
		return finalstring;
	}
	
	public void WriteDataToFile()
	{
		int count=0;
		String s="";
		try {
			while(rs.next()){
				s=convertData(rs.getString(2));
				int n=s.length();
				for(int i=0;i<n;i++)
					out.write(s.charAt(i));
				count++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
