package jcwcd.mp3;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MP3_PlayManyFiles extends HttpServlet
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
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	{
		String url = "jdbc:mysql://localhost:3306/churchinfo";
		String dbuser = "root"; String dbpass = "root";
		
		System.out.println("Date1 - "+ new Date());
		ServletOutputStream out= null; 
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			conn = DriverManager.getConnection(url, dbuser, dbpass);
			HttpSession sess = req.getSession(true);
			String username = (String)sess.getAttribute("musername");
			//String userpass = (String)sess.getAttribute("muserpass");
			System.out.println("UserName = "+username);
			
			res.setContentType("text/html");
			out = res.getOutputStream();
			
			out.println("<html><head><title>MP3.Stream</title>");
			out.println("<link rel='stylesheet' href='/images/styles.css' type='text/css'/></head>");

			out.println("<body bgcolor=#F0F0F0>");
			out.println("<table>");
			out.println("<tr><th><b>Track#</b></th><th>Track Name</th><th>Play</th></tr>");
			
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM churchinfo.mp3_mylibrary " + 
					"  ,churchinfo.mp3_fulllibrary where " + 
					"  mp3_fulllibrary.m_mp3id=mp3_mylibrary.m_mp3id "+
					"  and  mp3_mylibrary.m_id='"+username+"'");
			int t=1;
			while (rs.next())
			{
				out.println("<tr><td>"+t+"</td><td>"+rs.getString("m_mp3title")+"</td>"+"<td>"+
						"<br/><audio controls><source src='/"+rs.getString("m_mp3url")+"' type=\"audio/mpeg\">");
				out.print("</audio></td></tr>");
				t++;
			}
			out.println("</table>");
			out.println("</body></html>");
			
			
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

