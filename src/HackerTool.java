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

public class HackerTool {

	JFrame frame;
	private JPanel showdocuments,showdetails;
	private DefaultListModel documents;
	private JList documentlist;
	private JScrollPane docscrollpane;
	private Connection connection;
	private Statement statement;
	private ResultSet result;
	private JTextField tfBlockNo,tfOriginalData,tfChangedData;
	private JLabel panelFileName,panelBlockCount,panelFileDate,lblErrorText,panelEncryptText,panelErrorText;
	public int key;
	private JLabel lblOriginalText,lblChangedText;
	private String Block,tablename,Blockno;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HackerTool window=new HackerTool();
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
		frame = new JFrame("Hacker Tool");
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
		
		JLabel lblBlockNo=new JLabel("Block No");
		lblBlockNo.setBounds(320,40,150,20);
		lblBlockNo.setFont(new Font("Times New Roman", Font.BOLD, 15));
		frame.getContentPane().add(lblBlockNo);
		
		lblOriginalText=new JLabel("Original Block");
		lblOriginalText.setBounds(320,70,150,20);
		lblOriginalText.setFont(new Font("Times New Roman", Font.BOLD, 15));
		frame.getContentPane().add(lblOriginalText);
		
		lblChangedText=new JLabel("Change Text");
		lblChangedText.setBounds(320,100,150,20);
		lblChangedText.setFont(new Font("Times New Roman", Font.BOLD, 15));
		frame.getContentPane().add(lblChangedText);
		
		tfBlockNo=new JTextField();
		tfBlockNo.setBounds(440, 40, 50, 20);
		tfBlockNo.setFont(new Font("Times New Roman", Font.BOLD, 15));
		frame.getContentPane().add(tfBlockNo);
		
		tfOriginalData=new JTextField();
		tfOriginalData.setBounds(440, 70, 180, 20);
		tfOriginalData.setFont(new Font("Times New Roman", Font.BOLD, 15));
		tfOriginalData.disable();
		frame.getContentPane().add(tfOriginalData);
		
		tfChangedData=new JTextField();
		tfChangedData.setBounds(440, 100, 180, 20);
		tfChangedData.setFont(new Font("Times New Roman", Font.BOLD, 15));
		frame.getContentPane().add(tfChangedData);
		
		JButton btnGetBlock=new JButton("GetBlock");
		btnGetBlock.setFont(new Font("Times New Roman", Font.BOLD, 15));
		btnGetBlock.setBounds(500, 40, 100, 20);
		frame.getContentPane().add(btnGetBlock);
		btnGetBlock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String blockno=tfBlockNo.getText().toString();
				int i=0;
				if(!blockno.equals("")){
					try{
						i=Integer.parseInt(blockno);
						Blockno="";
						Blockno=Blockno+i;
						getBlock(i);
					}catch(Exception e){
						lblErrorText.setText("Invalid Block No");
					}
				}
				
			}
		});
		
		JButton btnSaveChanges=new JButton("Save Changes");
		btnSaveChanges.setFont(new Font("Times New Roman", Font.BOLD, 17));
		btnSaveChanges.setBounds(550, 130, 150, 28);
		frame.getContentPane().add(btnSaveChanges);
		btnSaveChanges.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				saveChanges();
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
		lblErrorText.setBounds(450,160,300,20);
		frame.getContentPane().add(lblErrorText);
		
	}
	
	private void saveChanges(){
		String changetext="";
		changetext=tfChangedData.getText().toString();
		if(changetext.equals("")){
			lblErrorText.setText("Enter modification Data");
		}else{
			makeconnection("mahi","mahi1234");
			try{
				changetext=ConvertStringToAscii(changetext);
				String query="update "+tablename+" set block='"+changetext+"' where id="+Blockno;
				result=statement.executeQuery(query);
				panelFileName.setText("Modifications done");
				panelFileDate.setText("");
				panelBlockCount.setText("");
				panelFileDate.setText("");
				connection.close();
			}catch(Exception e){
				e.printStackTrace();
				lblErrorText.setText("Something went wrong,Changes not made");
			}
		}
	}
	
	private String ConvertStringToAscii(String block){
		String finalstring="";
		int length=block.length();
		for(int i=0;i<length;i++){
			finalstring=finalstring+(int)(block.charAt(i))+'$';
		}
		return finalstring;
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
	
	private void getBlock(int i){
		makeconnection("mahi", "mahi1234");
		tablename=getTablename();
		if(!tablename.equals("")){
			try{
				result=statement.executeQuery("select block from "+tablename+" where id ="+i);
				while(result.next()){
					Block=result.getString(1);
					Block=ConvertAsciiBlockToString(Block);
					tfOriginalData.setText(Block);
				}
				connection.close();
			}catch(Exception e){
				lblErrorText.setText("Something wrong,Couldnt get Block");
			}
		}
	}
	private String getTablename(){
		String tablename="",docname="";
		if(documentlist.getSelectedIndex()!=-1){
			docname=docname+documentlist.getSelectedValue().toString();
			tablename=ConvertToTableName(docname);
		}else{
			lblErrorText.setText("Select a Document");
		}
		return tablename;
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
