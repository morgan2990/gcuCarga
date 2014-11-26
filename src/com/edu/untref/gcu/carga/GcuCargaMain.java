package com.edu.untref.gcu.carga;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class GcuCargaMain {
	static final String DB_URL = "jdbc:mysql://localhost/STUDENTS";

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	// Database credentials
	static final String USER = "username";
	static final String PASS = "password";

	public static void main(String[] args) throws IOException,
			URISyntaxException {
		List<String> list = Files.readAllLines(Paths.get(".", "Oliveros informe de alumnos y materias.csv"),
				Charset.defaultCharset());
		String[] stringAux = list.remove(0).split(";");
		String[] idsMateria = Arrays.copyOfRange(stringAux, 5,
				stringAux.length);
		Iterator<String> i = list.iterator();
		int idAlumno = 1;
		int cantAlumnos = 0;
		int idSituacionAlumno = 1;
		while (i.hasNext()) {
			String s = i.next();
			try{
			String[] informacionAlumno = s.split(";");
			if (informacionAlumno[3].equals("2007")) {
				String planEstudio = "1";
				String queryAlumno = "INSERT INTO alumno (id, legajo, apellido, nombre, planEstudio_id, anioIngreso) VALUES("
						+ idAlumno
						+ ", "
						+ informacionAlumno[0]
						+ ", "
						+ informacionAlumno[1]
						+ ", "
						+ informacionAlumno[2]
						+ ", "
						+ planEstudio + ", " + informacionAlumno[4] + ");";
				// executeQuery(queryAlumno);
				String []notasAlumno = Arrays.copyOfRange(informacionAlumno, 5,
						informacionAlumno.length);
				System.out.println(queryAlumno);
				for (int j = 0; j < idsMateria.length; j++) {
					String querySituacionAlumno = "INSERT INTO Test.situacion_alumno (id, alumno_id, materia_id, estado_situacion, fecha_situacion) VALUES("
							+ idSituacionAlumno
							+ " ,"
							+ idAlumno
							+ " ,"
							+ idsMateria[j]
							+ " ,"
							+ calcularEstadoMateria(notasAlumno[j])
							+ " ,"
							+ new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
									.format(new Date())+");";
					idSituacionAlumno = idSituacionAlumno + 1;
					// executeQuery(querySituacionAlumno);
					System.out.println(querySituacionAlumno);
				}
			}
			cantAlumnos = cantAlumnos+1;
			}
			catch (ArrayIndexOutOfBoundsException e){
				
			}
			i.remove();
		}
		System.out.println(cantAlumnos);
	}

	public static void executeQuery(String query) {

		Connection conn = null;
		Statement stmt = null;
		// STEP 2: Register JDBC driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");

			// STEP 4: Execute a query
			System.out.println("Inserting records into the table...");
			stmt = conn.createStatement();

			stmt.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static String calcularEstadoMateria(String estado) {
		String[] estadosAprobados = { "4", "5", "6", "7", "8", "9", "10",
				"A.P.E." };
		String[] estadosCursando = { "C" };
		String[] estadosRegular = { "A" };
		String[] estadosDesaprobado = { "S/A" };
		if (Arrays.asList(estadosAprobados).contains(estado)) {
			return EstadoMateria.APROBADO.name();
		} else if (Arrays.asList(estadosCursando).contains(estado)) {
			return EstadoMateria.CURSANDO.name();
		} else if (Arrays.asList(estadosRegular).contains(estado)) {
			return EstadoMateria.REGULAR.name();
		} else if (Arrays.asList(estadosDesaprobado).contains(estado)) {
			return EstadoMateria.ABANDONADO.name();
		}
		return "";

	}
}
