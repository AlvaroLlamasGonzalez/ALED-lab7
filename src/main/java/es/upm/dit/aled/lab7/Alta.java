package es.upm.dit.aled.lab7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;



/**
 * Servlet implementation class Alta
 */
@WebServlet("/alta")
public class Alta extends HttpServlet {

    @Override
    public void init() {
    	if(getServletContext().getAttribute("repo") == null )
    		getServletContext().setAttribute("repo", new PacienteRepository(getServletContext()));
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    InputStream file = getServletContext().getResourceAsStream("/alta.html");
		InputStreamReader reader1 = new InputStreamReader(file);
		BufferedReader html = new BufferedReader(reader1);

		String pagina = "", linea;
		while((linea = html.readLine()) != null)
			pagina += linea;

		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		out.println(pagina);
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//TODO
	}
}
