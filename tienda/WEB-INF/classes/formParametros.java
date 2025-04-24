import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class formParametros extends HttpServlet 
{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(	"<HEAD></HEAD>" + 
						"<BODY>\n" +
						"<H1>" + "Leyendo Texto" + "</H1>\n" +
						"<UL>\n" + 
						" <LI><B>Texto1</B>: "
						+ request.getParameter("txt1") + "\n" + 
						" <LI><B>Texto 2</B>: "
						+ request.getParameter("txt2") + "\n" + 
						"</UL>\n" +
						"</BODY></HTML>");
	}
}
