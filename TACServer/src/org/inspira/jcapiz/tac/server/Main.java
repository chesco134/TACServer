package org.inspira.jcapiz.tac.server;

public class Main {

	public static void main(String[] args){
		Server server = new Server();
		server.start();
		System.out.println("Running");
		try{
			server.join();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		System.out.println("Finished");
	}
}
