package jcwcd.mp3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MP3_AddToLibrary extends HttpServlet
{	/**
	 * The Default SerialID
	 */
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config)
	{
		try{super.init(config);
	    	Class.forName("com.mysql.jdbc.Driver");
	    }catch(Exception er){}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	{
		String url = "jdbc:mysql://localhost:3306/churchinfo";
		String dbuser = "root"; String dbpass = "root";
		
		System.out.println("Date1 - "+ new Date());
		ServletOutputStream out= null; 
		Connection conn = null;
		Statement stmt = null;  String username = "";
		ResultSet rs = null;  String playFile="mp3dg4y9wepo";
		try{
			HttpSession sess = req.getSession(true);
			username = (String)sess.getAttribute("musername");
			
			conn = DriverManager.getConnection(url, dbuser, dbpass);
			stmt = conn.createStatement();
			String mp3id = (String)req.getParameter("f_mp3id"); 
		
			res.setContentType("text/html");
			out = res.getOutputStream();
			
			out.println("<html><head><title>MP3.Stream</title></head>");
			out.println("<link rel='stylesheet' href='/images/styles.css' type='text/css'/></head>");
			out.println("<body bgcolor=#F0F0F0>");
			
			out.println("<table style='customers'>");
			out.println("<tr><th>Thank You For Subscribing</th></tr>");
			
			
			if (mp3id!=null){
				rs = stmt.executeQuery("SELECT * FROM churchinfo.mp3_fulllibrary "+
						"where m_mp3id='"+mp3id+"'");
				if(rs.next())
				{ ///--- Valid MP3 file to be added...
					PreparedStatement pstmt = conn.prepareStatement("replace into churchinfo.mp3_mylibrary "+
							"(m_id, m_mp3id, m_buydate) values(?,?,?)");
					pstmt.setString(1, username);
					pstmt.setString(2, mp3id);
					pstmt.setDate(3, new java.sql.Date((new java.util.Date()).getTime()));
					pstmt.executeUpdate();
					pstmt.close();
					out.println("<tr><td>Added To Library [OK]</td></tr>");
				}
			}
			out.println("<tr><td>Refrfesh</td></tr>");
			out.println("</table>");
			//String userpass = (String)sess.getAttribute("muserpass");
			System.out.println("UserName = "+username);
		}catch(Exception err){
			System.out.println("Error - "+err.toString());
		}finally{
			try{rs.close();}catch(Exception er1){}
			try{stmt.close();}catch(Exception er1){}
			try{conn.close();}catch(Exception er1){}
			try{out.close();}catch(Exception er1){}
		}
	}
}