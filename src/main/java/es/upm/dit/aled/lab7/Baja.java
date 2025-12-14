package es.upm.dit.aled.lab7;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet implementation class Baja
 */
@WebServlet("/baja")
public class Baja extends HttpServlet {

		@Override
	    public void init() {
	    	if(getServletContext().getAttribute("repo") == null )
	    		getServletContext().setAttribute("repo", new PacienteRepository(getServletContext()));
	    }

		/**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		    InputStream file = getServletContext().getResourceAsStream("/baja.html");
			InputStreamReader reader1 = new InputStreamReader(file);
			BufferedReader html = new BufferedReader(reader1);

			String pagina = "", linea;
			while((linea = html.readLine()) != null)
				pagina += linea;

			PacienteRepository repo = (PacienteRepository) getServletContext().getAttribute("repo");
			String options = "";
			for (Paciente p : repo.getPacientes()) {   
				options += "<option value='"+p.getDni()+"'>"+p.getNombre()+" "+p.getApellido()+"</option>\n";
			}			
			pagina = pagina.replace("<option></option>", options);

			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.println(pagina);
			out.close();
		}

		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			//Recuperar parámetros
			String dni = request.getParameter("paciente");

			//Leer la plantilla HTML
		    InputStream file = getServletContext().getResourceAsStream("/baja.html");
			InputStreamReader reader1 = new InputStreamReader(file);
			BufferedReader html = new BufferedReader(reader1);
			String pagina = "", linea;
			while((linea = html.readLine()) != null)
				pagina += linea;

			//Rellenar el mensaje
			PacienteRepository repo = (PacienteRepository) getServletContext().getAttribute("repo");
			String mensaje = "";
			if(repo.findByDni(dni) != null) {
				repo.removePaciente(dni);
				mensaje = "<h2 style='color: green; font: arial 10pt;'>El paciente con DNI "+dni+" ha sido eliminado</h2>";
			}
			else {
				mensaje = "<h2 style='color: red; font: arial 10pt;'>El paciente con DNI "+dni+" no está registrado</h2>";			
			}
			pagina = pagina.replace("<h2></h2>", mensaje);
			
			//Rellenar lista de pacientes actualizada
			String options = "";
			for (Paciente p : repo.getPacientes()) {   
				options += "<option value='"+p.getDni()+"'>"+p.getNombre()+" "+p.getApellido()+"</option>\n";
			}			
			pagina = pagina.replace("<option></option>", options);
			
			//Enviar la respuesta
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println(pagina);
			out.close();
		}
	}
