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

public class MP3_PlayFile extends HttpServlet
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
		ResultSet rs = null;  String playFile="mp3dg4y9wepo";
		try{
			conn = DriverManager.getConnection(url, dbuser, dbpass);
			stmt = conn.createStatement();
			
			if (req.getParameter("playFile")!=null) {playFile = req.getParameter("playFile");}
			rs = stmt.executeQuery("SELECT * FROM churchinfo.mp3_fulllibrary "+
						"where m_mp3id='"+playFile+"'");
			
			HttpSession sess = req.getSession(true);
			String username = (String)sess.getAttribute("musername");
			//String userpass = (String)sess.getAttribute("muserpass");
			System.out.println("UserName = "+username);
			
			res.setContentType("text/html");
			out = res.getOutputStream();
			
			out.println("<html><head><title>MP3.Stream</title></head>");
			out.println("<link rel='stylesheet' href='/images/styles.css' type='text/css'/></head>");

			out.println("<body bgcolor=#F0F0F0>");

			if(rs.next())
			{
				out.println("<table style='customers'>");
				out.println("<tr><th>Kings Revival Church</th></tr>");
				out.println("<tr><td>"+rs.getString("m_mp3title")+"</td></tr>");
				out.println("<tr><td><img src='/"+rs.getString("m_mp3image")+"' width=250 height=250> </td></tr>");
				out.println("<tr><td>"+"<audio controls><source src='/"+rs.getString("m_mp3url")+"' type=\"audio/mpeg\">");
				out.println("</audio></td></tr>");
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

