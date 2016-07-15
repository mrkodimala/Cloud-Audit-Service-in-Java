import java.io.*;
import java.sql.*;

public class Readfiles {
	Connection connection;
	Statement stmt;
	ResultSet rs;
	int key;
	public static void main(String[] args) throws IOException {
		
		Readfiles r=new Readfiles();
		r.handleReadClass("F:\\Material\\cloud audit.pdf",4,5);
	}
	
	
	public void handleReadClass(String filepath,int metadatalength,int key){
		this.key=key;
		try{
			InputStream in=new FileInputStream(filepath);
			String tablename=filepathconvert(filepath);
			CreateTable(tablename);
			DivideBlocks(in, tablename,metadatalength);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private String filepathconvert(String filepath){
		StringBuffer s=new StringBuffer(filepath);
		s=s.reverse();
		String finalstring="";
		int i=0;
		while(s.charAt(i)!='\\'){
			finalstring=finalstring+s.charAt(i);
			i++;
		}
		s=new StringBuffer(finalstring);
		s=s.reverse();
		int len=s.length();
		i=0;
		finalstring="";
		while(i<len){
			int k=(int)s.charAt(i);
			if((k>=65&&k<=90)||(k>=97&&k<=122)||(k>=48&&k<=57)){
				finalstring=finalstring+(char)(k);
			}
			if((char)(k)=='.')
			{
				finalstring=finalstring+'_';
			}
			i++;
		}
		s=new StringBuffer(finalstring);
		while(s.charAt(0)>=48&&s.charAt(0)<=57){
			s=new StringBuffer(s.substring(1));
		}
		finalstring=s.toString();
		return finalstring;
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
	
	private void CreateTable(String tablename) throws SQLException{
		
		String query="create table "+tablename+"(id number(10),block nvarchar2(150),metadata varchar2(30),metalength number(4))";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","mahi","mahi1234");
			stmt=connection.createStatement();
			stmt.executeQuery(query);
			System.out.println("table created successfully");
			Thread.sleep(100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("table already existing");
			try {
				stmt.executeQuery("drop table "+tablename);
				stmt.executeQuery(query);
				System.out.println("table created successfully");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			connection.close();
		}
	}
	
	private void InsertIntoTable(int id,String block,String meta,String tablename,int metalength){
		String query="insert into "+tablename+" values"+"("+id+",'"+block+"','"+meta+"','"+metalength+"')";
		try {
			/*Class.forName("oracle.jdbc.driver.OracleDriver");
			connection=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","mahi","mahi1234");
			stmt=connection.createStatement();*/
			stmt.executeQuery(query);
			//System.out.println("values inserted successfully");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//System.out.println(query);
			e.printStackTrace();
		}
	}
	
	private void DivideBlocks(InputStream in,String tablename,int metalength){
		try {
			int length=in.available();
			String block="";
			int blockno=1;
			int noofchars=0;
			String meta;
			makeconnection();
			for(int i=0;i<length;i++){
				int n=in.read();
				block=block+(n)+'$';
				noofchars++;
				if(noofchars==20){
					noofchars=0;
					meta=CalculateMetaData(block,metalength);
					InsertIntoTable(blockno,block,meta,tablename,metalength);
					block="";
					blockno++;
				}
			}
			System.out.println("file stored to database successfully with "+(blockno-1)+"value");
			connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String CalculateMetaData(String block,int metalength){
		String meta="";
		int length=block.length();
		int count=0;
		int metlength=0;
		String number="";
		for(int i=0;(i<length)&&(metlength<metalength);i++){
			if((char)block.charAt(i)=='$'){
				count++;
				if(count==3){
					i++;
					while((char)(block.charAt(i))!='$'){
						number=number+(char)(block.charAt(i));
						i++;
					}
					i--;
					count=0;
					metlength++;
					int metnumber=Integer.parseInt(number);
					number="";
					int encrypt=metnumber^key;
					meta=meta+encrypt+"$";
				}
			}
		}
		return meta;
	}
	
	private void makeconnection()
	{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","mahi","mahi1234");
			stmt=connection.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


//	public void handleReadClass(String filepathstr, int metalength, int key2) {
		// TODO Auto-generated method stub
		
	//}
}
