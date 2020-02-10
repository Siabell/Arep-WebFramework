package edu.escuelaing.arep;

import java.lang.reflect.Method;

import edu.escuelaing.arep.annotations.Web;

public class WebService {

	public static void main(String[] args) {
		String className = "edu.escuelaing.arep.webservice.WebServiceHello";
		
		try {
			Class c = Class.forName(className);
			Method[] methods = c.getMethods();
			for (Method m : methods) {
				if(m.isAnnotationPresent(Web.class)) {
					System.out.println("Ejecutando Metodo " + m.getName());
					System.out.println("En clase " + c.getName());
					try {
						System.out.println(m.invoke(null));
					} catch (Exception ex) {
						System.err.println(ex.getMessage());
					}
				}
			}
		}catch (ClassNotFoundException ex) {			
			System.err.println(ex.getMessage());
		}
	}

}
