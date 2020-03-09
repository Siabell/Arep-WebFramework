package edu.escuelaing.arep;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import java.net.*;

public class HttpServer {
	
	static final String DEFAULT_FILE = "index.html";
	static final File WEB_ROOT = new File(System.getProperty("user.dir") + "/src/main/resources");
	static final String FILE_NOT_FOUND = "404.html";
    static final String METHOD_NOT_SUPPORTED = "notSupported.html";

	
	/**
	 * Maneja cual es el recurso que quiero obtener el cliente y da una respuesta
	 * @param header
	 * @param out
	 * @param outputLine
	 */
	public static void requestOption (String[] header, PrintWriter out, BufferedOutputStream outputLine) {
		if (header[0].equals("GET")) {
			 System.out.println("es un get");
			 String fileReq = header[1];
			 if (header[1].equals("/")) {
				 System.out.println("por defecto");
				 File file = new File(WEB_ROOT,DEFAULT_FILE);
				 sendResponse(out,file,"text/html",outputLine,"200 ok");
			 }else {
	 				File file = new File(WEB_ROOT,fileReq);
	 				if (!file.exists()) {
	 					System.out.println("no existe el archivo "+fileReq);
	 					File fileNotFound = new File(WEB_ROOT,FILE_NOT_FOUND);
		 				sendResponse(out,fileNotFound,"text/html",outputLine,"404 NOT_FOUND");
	 				} else {
	 					System.out.println("si existe el archuivo "+fileReq);
	 					String contentMimeType = defineContentType(fileReq);
	 					sendResponse(out,file,contentMimeType,outputLine,"200 ok");
	 				}
	 			}
   	} else {
			 //metodo no permitido
			 File file = new File(WEB_ROOT,METHOD_NOT_SUPPORTED);
			 sendResponse(out,file,"text/html",outputLine,"405 METHOD_NOT_ALLOWED");
		 }
	}
	
	
	
	/**
     * Envia la respuesta al cliente
     * @param out
     * @param file
     * @param contentType
     * @param outputLine
     * @param answer
     */
    public static void sendResponse (PrintWriter out,File file, String contentType,BufferedOutputStream outputLine, String answer ) {
	
    	out.print("HTTP/1.1 "+ answer+"\r\n");
		out.print("Server: Java HTTP Server  : 1.0 \r\n");
		out.print("Content-type: " + contentType+"\r\n");
		out.print("\r\n"); // blank line between headers and content,
		out.flush(); // flush character output stream buffer
		// file
		if (file!= null) {
			try {
				System.out.println("si va--------------");
				String[] type = contentType.split("/");
				System.out.println(contentType);
				if (type[0].equals("image")  ) {
					BufferedImage image = ImageIO.read(file);
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			        ImageIO.write(image, type[1], byteArrayOutputStream);
			        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
			        outputLine.write(byteArrayOutputStream.toByteArray());
			        outputLine.flush();
				}else {
					System.out.println("texto");
					outputLine.write(fileDataByte(file), 0, (int) file.length());
					outputLine.flush();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		

    }
 
	
    
    /**
     * Convierte un archivo a bytes
     * @param file
     * @return un arreglo de bytes con la informacion del archivo
     * @throws IOException
     */
    private static byte[] fileDataByte (File file) throws IOException {
		FileInputStream fileIn = null;
		byte[] fileData = new byte[(int) file.length()];
		
		try {
			fileIn = new FileInputStream(file);
			fileIn.read(fileData);
		} finally {
			if (fileIn != null) 
				fileIn.close();
		}
		
		return fileData;
	}
    
    /**
     * Define cual es el tipo de contenido
     * @param fileReq
     * @return tipo de contenido
     */
    private static String defineContentType(String fileReq ) {
    	String answer = null;
    	if (fileReq.endsWith(".htm")  ) {
    		answer = "text/html";
    	} else if (fileReq.endsWith(".html")  ) {
    		answer = "text/html";
    	} else if (fileReq.endsWith(".jpg") ) {
    		answer = "image/jpg";
    	} else if (fileReq.endsWith(".png") ) {
    		answer = "image/png";
    	}
    	else {
    		answer = "text/html";
    	}
    	return answer;
    }
    
    
}
