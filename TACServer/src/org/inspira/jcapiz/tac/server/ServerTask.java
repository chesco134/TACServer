package org.inspira.jcapiz.tac.server;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class ServerTask extends Thread{
	
	private IOHandler ioHandler;
	
	public ServerTask(IOHandler ioHandler){
		this.ioHandler = ioHandler;
	}
	
	@Override
	public void run(){
		Parser request = null;
		Parser response = null;
		String formattedMessage = null;
		boolean status = false;
		byte[] mainContent = null;
		try{
			mainContent = ioHandler.handleIncommingMessage();
			formattedMessage = URLDecoder.decode(new String(mainContent), "UTF8");
			request = new Parser(formattedMessage);
			response = new Parser();
			switch(request.getAction()){
			case 1:
				AccionesTablaAlumnoCursaMateria.insertaAlumnoCursaUnidadDeAprendizaje(request.getString("boleta"), request.getString("unidad_aprendizaje"), request.getBoolean("es_recurse"));
				status = true;
				break;
			case 2:
				AccionesTablaAlumnoCursaMateria.actualizaRecurse(request.getString("boleta"), request.getString("unidad_aprendizaje"), request.getBoolean("es_recurse"));
				status = true;
				break;
			case 3:
				AccionesTablaAlumnoCursaMateria.eliminaAlumnoCursaUnidadDeAprendizaje(request.getString("boleta"), request.getString("unidad_aprendizaje"));
				status = true;
				break;
			case 4:
				response.addBoolean("es_recurse", AccionesTablaAlumnoCursaMateria.esRecurse(request.getString("boleta"), request.getString("unidad_aprendizaje")));
				status = true;
				break;
			case 5:
				response.addBoolean("cursa_unidad_aprendizaje", AccionesTablaAlumnoCursaMateria.cursaUnidadAprendizaje(request.getString("boleta"), request.getString("unidad_aprendizaje")));
				status = true;
				break;
			case 6:
				response.addBoolean("existe_boleta", AccionesTablaAlumnoCursaMateria.existeBoleta(request.getString("boleta")));
				status = true;
				break;
			case 7:
				response.addString("carrera", AccionesTablaAlumnoCursaMateria.carrera(request.getString("boleta")));
				status = true;
				break;
			case 8:
				response.addString("nombre", AccionesTablaAlumnoCursaMateria.obtenerNombre(request.getString("boleta")));
				status = true;
				break;
			case 9:
				response.addStringArray("materias", AccionesTablaAlumnoCursaMateria.obtenerMaterias(request.getString("boleta")));
				status = true;
				break;
			case 10:
				response.addStringArray("materias", AccionesTablaAlumnoCursaMateria.obtenerMateriasMarcadas(request.getString("boleta")));
				status = true;
				break;
			}
			response.addBoolean("status", status);
			ioHandler.sendMessage(URLEncoder.encode(response.getSerializedMessage(), "UTF8").getBytes());
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				ioHandler.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}
