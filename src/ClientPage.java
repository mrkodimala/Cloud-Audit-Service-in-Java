import java.awt.EventQueue;
import java.sql.*;
import java.awt.FlowLayout;
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
import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.UIManager;

public class ClientPage {

	JFrame frame;
	private JTextField Filepath;
	private JTextField MetaDataLength;
	private String Filepathstr;
	private JLabel lblProcessing,lblErrormessage;
	private JPanel showdocuments,showdetails;
	private DefaultListModel documents;
	private JList documentlist;
	private JScrollPane docscrollpane;
	private Connection connection;
	private Statement statement;
	private ResultSet result;
	private JLabel panelFileName,panelBlockCount,panelFileDate;
	private int key;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientPage window = new ClientPage();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the application.
	 */
	public ClientPage() {
		initialize();
		key=5;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Client Page");
		frame.getContentPane().setFont(new Font("Times New Roman", Font.BOLD, 18));
		frame.getContentPane().setBackground(new Color(176, 196, 222));
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(20, 20, 730, 700);
		
		JLabel lblChooseFile = new JLabel("Choose File");
		lblChooseFile.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblChooseFile.setBounds(45, 62, 100, 22);
		frame.getContentPane().add(lblChooseFile);
		
		Filepath = new JTextField("");
		Filepath.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		Filepath.setBounds(155, 61, 288, 23);
		Filepath.disable();
		frame.getContentPane().add(Filepath);
		Filepath.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBackground(UIManager.getColor("Button.light"));
		btnBrowse.setFont(new Font("Times New Roman", Font.BOLD, 17));
		btnBrowse.setBounds(468, 59, 100, 28);
		frame.getContentPane().add(btnBrowse);
		btnBrowse.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				 JFileChooser fileChooser = new JFileChooser();
			        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			        fileChooser.setAcceptAllFileFilterUsed(true);
			        int rVal = fileChooser.showOpenDialog(null);
			        if (rVal == JFileChooser.APPROVE_OPTION) {
			        	Filepathstr=fileChooser.getSelectedFile().toString();
			          Filepath.setText(Filepathstr);
			        }
			}
		});
		
		JLabel lblMetaDataLength = new JLabel("Meta Data Length");
		lblMetaDataLength.setFont(new Font("Times New Roman", Font.BOLD, 17));
		lblMetaDataLength.setBounds(45, 122, 140, 28);
		frame.getContentPane().add(lblMetaDataLength);
		
		JLabel lblmaxchars = new JLabel("Max No 4");
		lblmaxchars.setFont(new Font("Times New Roman", Font.BOLD, 13));
		lblmaxchars.setBounds(160, 140, 140, 28);
		lblmaxchars.setForeground(new Color(0,0,0));
		frame.getContentPane().add(lblmaxchars);
		
		lblProcessing = new JLabel("");
		lblProcessing.setFont(new Font("Times New Roman", Font.BOLD, 19));
		lblProcessing.setForeground(new Color(0,200,0));
		lblProcessing.setBounds(460, 122, 400, 28);
		frame.getContentPane().add(lblProcessing);
		
		MetaDataLength = new JTextField("");
		MetaDataLength.setFont(new Font("Times New Roman", Font.BOLD, 17));
		MetaDataLength.setBounds(188, 122, 45, 26);
		frame.getContentPane().add(MetaDataLength);
		MetaDataLength.setColumns(10);
		
		lblErrormessage = new JLabel("");
		lblErrormessage.setFont(new Font("Times New Roman", Font.BOLD, 19));
		lblErrormessage.setForeground(new Color(250,20,20));
		lblErrormessage.setBounds(230, 160, 400, 28);
		frame.getContentPane().add(lblErrormessage);
		
		JButton btnUpload = new JButton("Upload");
		btnUpload.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnUpload.setBounds(317, 122, 126, 28);
		frame.getContentPane().add(btnUpload);
		btnUpload.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				UploadButton();
			}
		});
		
		JLabel doclistlbl=new JLabel("Files in your cloud");
		doclistlbl.setFont(new Font("Times New Roman", Font.BOLD, 17));
		doclistlbl.setForeground(new Color(0,0,0));
		doclistlbl.setBounds(45,233,200,20);
		frame.getContentPane().add(doclistlbl);
		
		showdocuments=new JPanel();
		showdocuments.setLayout(null);
		showdocuments.setBounds(45, 270, 260, 250);
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
		btnrefresh.setBounds(200, 230, 100, 28);
		frame.getContentPane().add(btnrefresh);
		btnrefresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				refreshdocuments();
				frame.repaint();
				lblProcessing.setText("");
			}
		});
		
		
		JButton btngetdetails=new JButton("Get Details");
		btngetdetails.setFont(new Font("Times New Roman", Font.BOLD, 17));
		btngetdetails.setBounds(310, 270, 140, 28);
		frame.getContentPane().add(btngetdetails);
		btngetdetails.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				getdetails();
				lblProcessing.setText("");
			}
		});
		
		JButton btnClear=new JButton("Clear");
		btnClear.setFont(new Font("Times New Roman", Font.BOLD, 17));
		btnClear.setBounds(590,480,100,28);
		frame.getContentPane().add(btnClear);
		btnClear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				panelFileDate.setText("");
				panelBlockCount.setText("");
				panelFileName.setText("");
				lblProcessing.setText("");
			}
		});
		
		JButton btnDownload=new JButton("Download");
		btnDownload.setFont(new Font("Times New Roman", Font.BOLD, 17));
		btnDownload.setBounds(570,520,120,28);
		frame.getContentPane().add(btnDownload);
		btnDownload.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				DownloadFile();
			}
		});
		
		
		JButton btnDelete=new JButton("Delete");
		btnDelete.setFont(new Font("Times New Roman", Font.BOLD, 17));
		btnDelete.setBounds(460,520,100,28);
		frame.getContentPane().add(btnDelete);
		btnDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				DeleteTable();
				lblProcessing.setText("");
			}
		});
		
		showdetails=new JPanel();
		showdetails.setLayout(null);
		showdetails.setBounds(460, 270, 230, 200);
		showdetails.setBackground(new Color(255,255,255));
		frame.getContentPane().add(showdetails);
		
		JLabel lbldetailsabtfile=new JLabel("Details of File");
		lbldetailsabtfile.setFont(new Font("Times New Roman", Font.BOLD, 17));
		lbldetailsabtfile.setBounds(470,250,150,20);
		frame.getContentPane().add(lbldetailsabtfile);
		
		panelFileName=new JLabel();
		panelFileName.setFont(new Font("Times New Roman", Font.BOLD, 17));
		panelFileName.setBounds(5, 10, 200, 20);
		showdetails.add(panelFileName);
		
		panelBlockCount=new JLabel();
		panelBlockCount.setFont(new Font("Times New Roman", Font.BOLD, 17));
		panelBlockCount.setBounds(5, 35, 200, 20);
		showdetails.add(panelBlockCount);
		
		panelFileDate=new JLabel();
		panelFileDate.setFont(new Font("Times New Roman", Font.BOLD, 17));
		panelFileDate.setBounds(5, 60, 250, 20);
		showdetails.add(panelFileDate);
		
		JButton btnGotoTpaPage=new JButton("Goto TPA Page");
		btnGotoTpaPage.setFont(new Font("Times New Roman", Font.BOLD, 17));
		btnGotoTpaPage.setBounds(45,580, 200, 28);
		frame.getContentPane().add(btnGotoTpaPage);
		btnGotoTpaPage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//frame.disable();
				TPAPage t=new TPAPage();
				t.startTPAPage(5);
			}
		});
		
	}
	
	private void DownloadFile(){
		String docname="";
		if(documentlist.getSelectedIndex()!=-1){
			docname=docname+documentlist.getSelectedValue().toString();
			Writefiles w=new Writefiles();
			w.handleWriteFilesClass(docname);
			
			panelFileName.setText("Name : "+docname);
			panelBlockCount.setText("File downloaded ");
			panelFileDate.setText("             successfully");
		}
	}
	
	private void DeleteTable(){
		String docname="";
		if(documentlist.getSelectedIndex()!=-1){
			docname=docname+documentlist.getSelectedValue().toString();
			String tablename=ConvertToTableName(docname);
			makeconnection("mahi","mahi1234");
			try{
				result=statement.executeQuery("drop table "+tablename);
			}catch(Exception e){
				e.printStackTrace();
			}
			panelFileName.setText("Name : "+docname);
			panelBlockCount.setText("File Deleted successfully");
			panelFileDate.setText("");
			refreshdocuments();
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
	
	public void refreshdocuments()
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
	
	private void UploadButton(){
		String filepath,metadata;
		filepath=Filepath.getText().toString();
		metadata=MetaDataLength.getText().toString();
		if(filepath.equals("")||metadata.equals("")){
			if(filepath.equals("")&&metadata.equals("")){
				lblErrormessage.setText("Filepath and Metadata Both are empty");
			}else if(filepath.equals("")){
				lblErrormessage.setText("Filepath is empty");
			}else{
				lblErrormessage.setText("MetaData Field is empty");
			}
		}else{
			int metalength=0;
			try{
				metalength=Integer.parseInt(metadata);
				lblErrormessage.setText("");
			}catch(Exception e){
				lblErrormessage.setText("enter the number ");
			}
			if(metalength<=0||metalength>4){
				lblErrormessage.setText("enter the number in the range");
			}else{
				lblProcessing.setText("File Uploaded Successfully");
				frame.repaint();
				Readfiles r=new Readfiles();
				r.handleReadClass(Filepathstr, metalength, 5);
				refreshdocuments();
				CopyStorage c=new CopyStorage();
				c.Start(Filepathstr);
			}
		}
	}
}
