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

public class MP3_MyLibrary extends HttpServlet
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
		ResultSet rs = null;  int x = 1;
		try{
			HttpSession sess = req.getSession(true);
			String username = (String)sess.getAttribute("musername");
			//String userpass = (String)sess.getAttribute("muserpass");
			System.out.println("UserName = "+username);

			out = res.getOutputStream();
			res.setContentType("text/html");

			conn = DriverManager.getConnection(url, dbuser, dbpass);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM churchinfo.mp3_mylibrary " + 
					"  ,churchinfo.mp3_fulllibrary where " + 
					"  mp3_fulllibrary.m_mp3id=mp3_mylibrary.m_mp3id "+
					"  and  mp3_mylibrary.m_id='"+username+"'");

			out.println("<html><head><title>MP3.Library</title>");
			out.println("<link rel='stylesheet' href='/images/styles.css' type='text/css'/></head>");
			
			out.println("<body bgcolor=#F2F2F2>");
			out.println("<table width='300' style='customers'>");
			out.println("<tr><th>Track</th><th>Name</th><th>Play</th><th>Share</th></tr>");
			while(rs.next())
			{
				out.println("<tr><td> ["+ (x++) +"] </td>");
				out.println("<td> "+rs.getString("m_mp3title") +" </td>");
				out.println("<td> <a href='/mp3/mp3playfile?playFile="+rs.getString("m_mp3id")+"'>"+
						"<img src='/images/small-play-button.png' width=32 height=32/></a></td>");
				out.println("<td> <img src='/images/share-2-icon.png' width=32 height=32/></td></tr>");
				System.out.println("Redirecting to MyLibrary!");
			}

			out.println("</table>");
			out.println("<b><a href='/mp3/mp3playmanyfiles'>Play Files</a><br/><br/>");
			out.println("<b><form action='/mp3/addtolibrary' method='post'>");
			out.println("	<input name='f_mp3id' type='text' size='16'/><br/>");
			out.println("   <input type='submit' text='Submit'/>");
			out.println("</form>");
			System.out.println("Table connected!");
			out.println("</body></head>");
			
		}catch(Exception err1){
			try{out.println(err1.toString());}catch(Exception err2){}
			System.out.println(err1.toString());
		}finally{
			try{out.close();}catch(Exception err1){}
			try{rs.close();}catch(Exception err1){}
			try{stmt.close();}catch(Exception err1){}
			try{conn.close();}catch(Exception err1){}
		}
		
		
	}
			

}
