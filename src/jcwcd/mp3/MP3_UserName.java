package jcwcd.mp3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MP3_UserName extends HttpServlet
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
		Statement stmt = null;
		ResultSet rs = null;
		String username = null; String passwd = null;
		try{
			HttpSession sess = req.getSession(true);
			String m_user = req.getParameter("f_user"); 
			String m_passwd = req.getParameter("f_passwd"); 
			
			if (sess.getAttribute("musername")!=null)
			{
				username = (String)sess.getAttribute("musername");
				passwd = (String)sess.getAttribute("muserpass");
				System.out.println("User = "+username+"  / passwd = "+passwd);
			}
  
			out = res.getOutputStream();
			res.setContentType("text/html");
			
			System.out.println("Connecting database...");

			conn = DriverManager.getConnection(url, dbuser, dbpass);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM mp3_users where m_id='"+
					m_user+"' and m_passwd='"+m_passwd+"'");
			if(rs.next())
			{
				System.out.println("Redirecting to MyLibrary!");
				sess.setAttribute("musername", m_user);
				sess.setAttribute("muserpass", m_passwd);
				res.sendRedirect("/mp3/mp3mylibrary");
			}else{
				out.println("<h4>Bad UserName/Passwd </h4>");
			}
			System.out.println("Database connected!");
			
			
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
