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
		// 1. Recuperar PacienteRepository
		PacienteRepository repo = (PacienteRepository) getServletContext().getAttribute("repo");
		
		// 2. Recuperar parámetros del formulario (alta.html)
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String dni = request.getParameter("dni");
     // 3. Crear objeto Paciente
        Paciente nuevoPaciente = new Paciente(nombre, apellidos, dni.toUpperCase());
     // 4. Leer contenido de alta.html (lógica similar a doGet)
        InputStream file = getServletContext().getResourceAsStream("/alta.html");
		InputStreamReader reader1 = new InputStreamReader(file);
		BufferedReader html = new BufferedReader(reader1);

		String pagina = "", linea;
		while((linea = html.readLine()) != null)
			pagina += linea;
        String mensaje;
        String mensajeHtml;
     // 5. Comprobar si el paciente ya está registrado (a través de PacienteRepository)
        if (repo.findByDni(nuevoPaciente.getDni()) != null) {
            // a) Paciente ya guardado: Mensaje de error (rojo)
            mensaje = "Error: El paciente con DNI " + dni + " ya se encuentra registrado.";
            mensajeHtml = "<span style=\"color: red;\">" + mensaje + "</span>";
        } else {
            // b) Paciente no guardado: Guardar y mensaje de éxito (verde)
            repo.addPaciente(nuevoPaciente);
            mensaje = "El paciente " + nombre + " " + apellidos + " ha sido dado de alta.";
            mensajeHtml = "<span style=\"color: green;\">" + mensaje + "</span>";
        }
	}
}
