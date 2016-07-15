import java.awt.EventQueue;
import java.sql.*;
import java.awt.Font;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TPAPage {

	JFrame frame;
	private JPanel showdocuments,showdetails;
	private DefaultListModel documents;
	private JList documentlist;
	private JScrollPane docscrollpane;
	private Connection connection;
	private Statement statement;
	private ResultSet result;
	private JTextField tfmetalength,tfBlockNumber;
	private JLabel panelFileName,panelBlockCount,panelFileDate,lblErrorText,panelEncryptText,panelErrorText;
	public int key;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TPAPage window = new TPAPage();
					window.startTPAPage(5);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the application.
	 */
	public void startTPAPage(int key) {
		this.key=key;
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Third Party Auditor Page");
		frame.getContentPane().setFont(new Font("Times New Roman", Font.BOLD, 18));
		frame.getContentPane().setBackground(new Color(176, 196, 222));
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		frame.setBounds(400, 200, 800, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JLabel doclistlbl=new JLabel("Select a File from cloud");
		doclistlbl.setFont(new Font("Times New Roman", Font.BOLD, 17));
		doclistlbl.setForeground(new Color(0,0,0));
		doclistlbl.setBounds(45,20,200,20);
		frame.getContentPane().add(doclistlbl);
		
		showdocuments=new JPanel();
		showdocuments.setLayout(null);
		showdocuments.setBounds(45, 50, 260, 250);
		showdocuments.setBackground(new Color(255,255,255));
		frame.getContentPane().add(showdocuments);
		
		documents=new DefaultListModel<String>();
		refreshdocuments();
		
		documentlist=new JList(documents);
		documentlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		documentlist.setSelectedIndex(0);
		documentlist.setVisibleRowCount(5);
		
		docscrollpane=new JScrollPane(documentlist);
		docscrollpane.setBounds(0, 0, 260, 250);
		showdocuments.add(docscrollpane);
		
		JButton btnrefresh=new JButton("Refresh");
		btnrefresh.setFont(new Font("Times New Roman", Font.BOLD, 17));
		btnrefresh.setBounds(45, 320, 100, 28);
		frame.getContentPane().add(btnrefresh);
		btnrefresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				refreshdocuments();
				frame.repaint();
			}
		});
		
		JLabel lblMetaLength=new JLabel("Meta Data Length");
		lblMetaLength.setBounds(350,40,150,30);
		lblMetaLength.setFont(new Font("Times New Roman", Font.BOLD, 17));
		frame.getContentPane().add(lblMetaLength);
		
		JLabel lblBlockNo=new JLabel("      Block Number");
		lblBlockNo.setBounds(350,80,150,30);
		lblBlockNo.setFont(new Font("Times New Roman", Font.BOLD, 17));
		frame.getContentPane().add(lblBlockNo);
		
		tfmetalength=new JTextField();
		tfmetalength.setBounds(500, 40, 50, 30);
		tfmetalength.setFont(new Font("Times New Roman", Font.BOLD, 17));
		frame.getContentPane().add(tfmetalength);
		
		tfBlockNumber=new JTextField();
		tfBlockNumber.setBounds(500, 80, 100, 30);
		tfBlockNumber.setFont(new Font("Times New Roman", Font.BOLD, 17));
		frame.getContentPane().add(tfBlockNumber);
		
		JButton btnChallange=new JButton("Challange");
		btnChallange.setFont(new Font("Times New Roman", Font.BOLD, 17));
		btnChallange.setBounds(550, 120, 150, 28);
		frame.getContentPane().add(btnChallange);
		btnChallange.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				ChallangeButton();
			}
		});

		JButton btngetdetails=new JButton("Get Details");
		btngetdetails.setFont(new Font("Times New Roman", Font.BOLD, 17));
		btngetdetails.setBounds(160, 320, 140, 28);
		frame.getContentPane().add(btngetdetails);
		btngetdetails.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				getdetails();
			}
		});
		
		showdetails=new JPanel();
		showdetails.setLayout(null);
		showdetails.setBounds(360, 210, 400, 200);
		showdetails.setBackground(new Color(255,255,255));
		frame.getContentPane().add(showdetails);
		
		JLabel lbldetailsabtfile=new JLabel("Details of File");
		lbldetailsabtfile.setFont(new Font("Times New Roman", Font.BOLD, 17));
		lbldetailsabtfile.setBounds(370,190,150,20);
		frame.getContentPane().add(lbldetailsabtfile);
		
		panelFileName=new JLabel();
		panelFileName.setFont(new Font("Times New Roman", Font.BOLD, 17));
		panelFileName.setBounds(5, 10, 400, 20);
		showdetails.add(panelFileName);
		
		panelBlockCount=new JLabel();
		panelBlockCount.setFont(new Font("Times New Roman", Font.BOLD, 17));
		panelBlockCount.setBounds(5, 35, 400, 20);
		showdetails.add(panelBlockCount);
		
		panelFileDate=new JLabel();
		panelFileDate.setFont(new Font("Times New Roman", Font.BOLD, 17));
		panelFileDate.setBounds(5, 60, 400, 20);
		showdetails.add(panelFileDate);
		
		panelEncryptText=new JLabel();
		panelEncryptText.setFont(new Font("Times New Roman", Font.BOLD, 17));
		panelEncryptText.setBounds(5, 85, 400, 20);
		showdetails.add(panelEncryptText);
		
		panelErrorText=new JLabel();
		panelErrorText.setFont(new Font("Times New Roman", Font.BOLD, 15));
		panelErrorText.setBounds(5, 130, 400, 20);
		panelErrorText.setForeground(new Color(255,0,0));
		showdetails.add(panelErrorText);
		
		JButton btnClear=new JButton("Clear");
		btnClear.setFont(new Font("Times New Roman", Font.BOLD, 17));
		btnClear.setBounds(450, 420, 140, 28);
		frame.getContentPane().add(btnClear);
		btnClear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				panelFileDate.setText("");
				panelBlockCount.setText("");
				panelFileName.setText("");
			}
		});
		
		JButton btnClose=new JButton("Close");
		btnClose.setFont(new Font("Times New Roman", Font.BOLD, 17));
		btnClose.setBackground(new Color(255,0,0));
		btnClose.setForeground(new Color(255,255,255));
		btnClose.setBounds(600, 420, 140, 28);
		frame.getContentPane().add(btnClose);
		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				frame.dispose();
			}
		});
		
		lblErrorText=new JLabel("");
		lblErrorText.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblErrorText.setForeground(new Color(255,0,0));
		lblErrorText.setBounds(450,150,300,20);
		frame.getContentPane().add(lblErrorText);
		
		JButton btnRecoverData=new JButton("Recover Original Data");
		btnRecoverData.setFont(new Font("Times New Roman", Font.BOLD, 17));
		btnRecoverData.setBounds(550, 160, 210, 28);
		frame.getContentPane().add(btnRecoverData);
		btnRecoverData.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				RecoverDataButton();
			}
		});
		
		
	}
	
	private void RecoverDataButton(){
		String blockno=tfBlockNumber.getText().toString();
		String docname="";
		if(blockno.equals("")){
			lblErrorText.setText("Block No is empty");
		}else{
			if(documentlist.getSelectedIndex()!=-1){
				docname=docname+documentlist.getSelectedValue().toString();
				String tablename=ConvertToTableName(docname);
				String count=getBlockCount(tablename);
				int tfBlockNo=Integer.parseInt(blockno);
				int BlockNo=Integer.parseInt(count);
				if(tfBlockNo>BlockNo||tfBlockNo<1){
					lblErrorText.setText("Invalid Block Number, Range(1 to"+BlockNo+")");
				}else{
					ModfifyOriginalData m=new ModfifyOriginalData();
					m.handleModifyOriginalData(tfBlockNo, tablename);
					panelFileName.setText("Original Data Recovered Successfully");
					panelBlockCount.setText("");
					panelEncryptText.setText("");
					panelErrorText.setText("");
					panelFileDate.setText("");
				}
			}else{
				lblErrorText.setText("Document not Selected");
			}
		}
	}
	
	
	private void getdetails(){
		String docname="";
		if(documentlist.getSelectedIndex()!=-1){
			docname=docname+documentlist.getSelectedValue().toString();
			String tablename=ConvertToTableName(docname);
			String count=getBlockCount(tablename);
			String date=getFileModifiedDate(tablename);
			panelFileName.setText("Name : "+docname);
			panelBlockCount.setText("No of Blocks:"+count);
			panelFileDate.setText("Date:"+date);
		}
	}
	
	private void ChallangeButton(){
		String metadata=tfmetalength.getText().toString();
		String blockno=tfBlockNumber.getText().toString();
		int metalength=1,Blockno=1,flag=0;
		if(metadata.equals("")||blockno.equals("")){
			if(metadata.equals("")&&blockno.equals("")){
				lblErrorText.setText("Metadata and Blockno field are empty");
			}else if(blockno.equals("")){
				lblErrorText.setText("Blockno field is empty");
			}else{
				lblErrorText.setText("MetaData Field is empty");
			}
		}else{
			try{
				metalength=Integer.parseInt(metadata);
				Blockno=Integer.parseInt(blockno);
			}catch(Exception e){
				flag=1;
			}
			if(flag==1){
				lblErrorText.setText("Metalength or Blockno is not in correct format");
			}else{
				Challange(metalength,Blockno);
			}
		}
	}
	private void Challange(int tfMetaLength,int tfBlockno){
		//System.out.println("Challange called with "+tfMetaLength+"\t"+tfBlockno);
		String docname="";
		String tablename;
		if(documentlist.getSelectedIndex()!=-1){
			//System.out.println("Contro reached inside if");
			docname=docname+documentlist.getSelectedValue().toString();
			tablename=ConvertToTableName(docname);
			String strCount=getBlockCount(tablename);
			int metalength=getMetaLength(tablename);
			int BlockCount=Integer.parseInt(strCount);
			//System.out.println(docname+"\t"+tablename+"\t"+strCount+"\t"+metalength+"\t"+BlockCount+"\t"+tfBlockno);
			if(metalength!=tfMetaLength){
				lblErrorText.setText("Invalid metadata");
			}else{
				if(tfBlockno>BlockCount||tfBlockno<=0){
					lblErrorText.setText("Enter the Blockno in range 1 to"+BlockCount);
				}else{
					lblErrorText.setText("");
					//System.out.println("test challange reached");
					TestChallange(tfBlockno,tablename,tfMetaLength);
					//System.out.println("test challange executed");
				}
			}
		}else{
			lblErrorText.setText("Document is not selected");
		}
	}
	
	private void TestChallange(int Blockno,String tablename,int metalength){
		makeconnection("mahi","mahi1234");
		String block="",metadata="";
		try{
			result=statement.executeQuery("select block,metadata from "+tablename+" where id="+Blockno);
			while(result.next()){
				block=result.getString(1).toString();
				metadata=result.getString(2).toString();
			}
			String strBlock=ConvertAsciiBlockToString(block);
			String strmetadata=ConvertToStringMeta(metadata);
			String encryptmetadat=ConvertEncryptMeta(metadata);
			int flag=CheckIntegrity(strBlock,strmetadata,metalength);
			panelFileName.setText("Block: "+strBlock);
			panelFileDate.setText("MetaData: "+strmetadata);
			panelBlockCount.setText("Encrypted MetaData: "+encryptmetadat);
			panelErrorText.setText("Spaces are not visible in Metadata and Encrypted Data");
			if(flag==1){
				panelEncryptText.setText("Success,No modifications");
			}else{
				panelEncryptText.setText("Fail,Modification Done");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private String ConvertEncryptMeta(String meta){
		String finalstring="";
		String t="";
		int temp;
		int strlength=meta.length();
		for(int i=0;i<strlength;i++){
			if(meta.charAt(i)!='$'){
				t=t+meta.charAt(i);
			}else{
				temp=Integer.parseInt(t);
				t="";
				finalstring=finalstring+(char)(temp);
			}
		}
		return finalstring;
	}
	
	private int CheckIntegrity(String block,String meta,int metalength){
		int i=3;
		int j=0;
		int k=2;
		while(j<metalength){
			if(block.charAt(i)==meta.charAt(j)){
				j++;
				i=i+3;
			}else{
				return 0;
			}
		}
		return 1;
	}
	
	private String ConvertAsciiBlockToString(String Block){
		String finalstr="";
		String t="";
		int temp;
		int strlength=Block.length();
		for(int i=0;i<strlength;i++){
			if(Block.charAt(i)!='$'){
				t=t+Block.charAt(i);
			}else{
				temp=Integer.parseInt(t);
				finalstr=finalstr+(char)(temp);
				t="";
			}
		}
		return finalstr;
	}
	
	private String ConvertToStringMeta(String Metadata){
		String finalstr="";
		String t="";
		int temp;
		int strlength=Metadata.length();
		for(int i=0;i<strlength;i++){
			if(Metadata.charAt(i)!='$'){
				t=t+Metadata.charAt(i);
			}else{
				temp=Integer.parseInt(t);
				temp=temp^key;
				finalstr=finalstr+(char)(temp);
				t="";
			}
		}
		return finalstr;
	}
	
	private int getMetaLength(String tablename){
		int metalength=0;
		makeconnection("mahi", "mahi1234");
		try {
			result=statement.executeQuery("select metalength from "+tablename+" where id=1");
			while(result.next()){
				metalength=result.getInt(1);
				//System.out.println(metalength);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return metalength;
	}
	
	private void refreshdocuments()
	{
		RefreshClass r=new RefreshClass();
		r.getDocuments();
		documents.removeAllElements();
		try {
			while(r.result.next()){
				String s=r.result.getString(1).toString();
				StringBuffer tblname=new StringBuffer(s);
				String finalstring="";
				int length=tblname.length();
				for(int i=0;i<length;i++)
				{
					
					if(tblname.charAt(i)=='_'){
						finalstring=finalstring+".";
					}else{
						finalstring=finalstring+tblname.charAt(i);
					}
				}
				documents.addElement(finalstring);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Error occured in refreshdocuments function");
		}
		frame.repaint();
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
	
	private String getFileModifiedDate(String tablename){
		String date="";
		makeconnection("mahi","mahi1234");
		try{
			tablename=tablename.toUpperCase();
			result=statement.executeQuery("select created from user_objects where object_name='"+tablename+"'");
			while(result.next()){
				date=result.getString(1).toString();
			}
			connection.close();
		}catch(Exception e){
			//e.printStackTrace();
			System.out.println("error occured in filemodified date function");
		}
		return date;
	}
	
	private String getBlockCount(String tablename){
		String count="";
		makeconnection("mahi","mahi1234");
		try{
			result=statement.executeQuery("select count(*) from "+tablename);
			while(result.next()){
				count=result.getString(1).toString();
			}
			connection.close();
		}catch(Exception e){
			//e.printStackTrace();
			System.out.println("error occured in block count function");
		}
		return count;
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
	//public void startTPAPage(int key2) {
		// TODO Auto-generated method stub
		
	//}
	
}
