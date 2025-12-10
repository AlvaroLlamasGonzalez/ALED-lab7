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
	import java.util.Collection;

	@WebServlet("/baja")
	public class Baja extends HttpServlet {

	    // Método auxiliar para leer el HTML (similar al de Alta)
	    private String readHtmlContent(String path) throws IOException {
	        InputStream file = getServletContext().getResourceAsStream(path);
	        InputStreamReader reader1 = new InputStreamReader(file);
	        BufferedReader html = new BufferedReader(reader1);

	        String pagina = "", linea;
	        while((linea = html.readLine()) != null)
	            pagina += linea + "\n"; 

	        html.close();
	        return pagina;
	    }
	    
	    /**
	     * Renderiza la página de baja, llenando el desplegable y mostrando el mensaje si existe.
	     */
	    private void renderBajaPage(HttpServletRequest request, HttpServletResponse response, 
	                                String mensaje, boolean exito) throws IOException {

	        PacienteRepository repo = (PacienteRepository) getServletContext().getAttribute("repo");
	        
	        // 1. Leer baja.html
	        String pagina = readHtmlContent("/baja.html");

	        // 2. Construir las opciones del desplegable
	        StringBuilder opcionesHtml = new StringBuilder();
	        Collection<Paciente> pacientes = repo.getPacientes();

	        if (pacientes.isEmpty()) {
	            opcionesHtml.append("<option value=\"\">No hay pacientes registrados</option>");
	        } else {
	             // Opciones para el desplegable (DNI como value, Nombre + Apellido como texto)
	             opcionesHtml.append("<option value=\"\">-- Seleccione paciente --</option>"); 
	             for (Paciente p : pacientes) { 
	                 opcionesHtml.append("<option value=\"").append(p.getDni()).append("\">");
	                 opcionesHtml.append(p.getNombre()).append(" ").append(p.getApellido());
	                 opcionesHtml.append("</option>");
	             }
	        }

	        // Reemplazar el placeholder <option></option> en baja.html
	        // Nota: asumiendo que el HTML de BAJA solo tiene un <option></option> para reemplazar.
	        pagina = pagina.replace("<option></option>", opcionesHtml.toString());

	        // 3. Insertar el mensaje de resultado (en <h2></h2>)
	        if (mensaje != null) {
	            String color = exito ? "green" : "red"; 
	            String mensajeHtml = "<span style=\"color: " + color + ";\">" + mensaje + "</span>";
	            pagina = pagina.replace("<h2></h2>", "<h2>" + mensajeHtml + "</h2>");
	        }

	        // 4. Devolver la respuesta
	        response.setContentType("text/html");
	        response.getWriter().println(pagina);
	        response.getWriter().close();
	    }

		/**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        // Muestra la página de baja, llenando el desplegable
	        renderBajaPage(request, response, null, false);
		}

		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        
	        // a) recuperar el valor seleccionado (DNI)
	        String dniAEliminar = request.getParameter("paciente");
	        
	        String mensaje = null;
	        boolean exito = false;

	        if (dniAEliminar != null && !dniAEliminar.isEmpty()) {
	            
	            PacienteRepository repo = (PacienteRepository) getServletContext().getAttribute("repo");

	            // b) Comprobar si dicho paciente está almacenado (para obtener su nombre antes de borrar)
	            Paciente pacienteAborrar = repo.findByDni(dniAEliminar);
	            
	            if (pacienteAborrar != null) {
	                // c) Lo elimine si estaba almacenado
	                repo.removePaciente(dniAEliminar); 
	                mensaje = "El paciente con DNI **" + dniAEliminar + "** ha sido eliminado";
	                exito = true;
	            } else {
	                // Paciente no encontrado (ej. intento de refresco tras eliminación)
	                mensaje = "Error: El paciente con DNI **" + dniAEliminar + "** no se encuentra registrado o ya fue eliminado.";
	                exito = false;
	            }
	        } else {
	             mensaje = "Error: Debe seleccionar un paciente para dar de baja.";
	             exito = false;
	        }

	        // d) Muestre de nuevo la página de baja con los datos actualizados y un mensaje.
	        renderBajaPage(request, response, mensaje, exito);
		}
	}