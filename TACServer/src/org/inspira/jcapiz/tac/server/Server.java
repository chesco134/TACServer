package org.inspira.jcapiz.tac.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
	/** We may need to add a task limiter **/
	@Override
	public void run(){
		ServerSocket server;
		Socket socket;
		DataInputStream entrada;
		DataOutputStream salida;
		IOHandler ioHandler;
		try{
			server = new ServerSocket(23543);
			while(true){
				socket = server.accept();
				entrada = new DataInputStream(socket.getInputStream());
				salida = new DataOutputStream(socket.getOutputStream());
				ioHandler = new IOHandler(entrada, salida);
				new ServerTask(ioHandler).start();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
