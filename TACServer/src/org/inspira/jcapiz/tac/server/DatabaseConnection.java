package org.inspira.jcapiz.tac.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseConnection {

	private Connection connection;
	private String url;
	private String user;
	private String password;

	public String getUrl() {
		return url;
	}

	public DatabaseConnection() {
		String host = "";
		this.user = "";//"amstrong";
		this.password = "";//"!\"#$%wszae123QWERT";
		try{
			Scanner scan = new Scanner(new FileInputStream(new File("credenciales.txt")));
			host = scan.nextLine();
			this.user = scan.nextLine();
			this.password = scan.nextLine();
			scan.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		url = "jdbc:mysql://"+host+"/Demanda_Unidades_Aprendizaje?useUnicode=true&characterEncoding=utf-8";
		connection = null;
	}

	public Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			connection = null;
			url = e.toString();
		} catch (InstantiationException e) {
			url = e.toString();
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			url = e.toString();
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			url = e.toString();
			e.printStackTrace();
		}
		return connection;
	}

	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}