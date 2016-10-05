/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inspira.jcapiz.tac.server;

import java.util.List;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author jcapiz
 */
public class AccionesTablaAlumnoCursaMateria {
    public static final String BIO = "INGENIERIA BIÓNICA";
    public static final String MEC = "INGENIERIA MECATRÓNICA";
    public static final String TEL = "INGENIERIA TELEMÁTICA";
    public static final String ISISA = "ISISA";
    
    private static final DatabaseConnection db;
    
    static{
    	 db = new DatabaseConnection();
    }
    
    public static void actualizaRecurse(String boleta, String ua, boolean esRecurse){
        try{
            Connection con = db.getConnection();
            CallableStatement cstmnt = con.prepareCall("{call actualizaRecurse(?,?,?)}");
            cstmnt.setString(1, boleta);
            cstmnt.setString(2, ua);
            cstmnt.setBoolean(3, esRecurse);
            cstmnt.executeUpdate();
            con.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        db.closeConnection();
    }
    
    public static void insertaAlumnoCursaUnidadDeAprendizaje(String boleta, String ua, boolean esRecurse){
        try{
            Connection con = db.getConnection();
            CallableStatement cstmnt =
                    con.prepareCall("{call insertaAlumno_Cursa_Unidad_Aprendizaje(?,?,?)}");
            cstmnt.setString(1, boleta);
            cstmnt.setString(2, ua);
            cstmnt.setBoolean(3, esRecurse);
            cstmnt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        db.closeConnection();
    }
    
    public static void eliminaAlumnoCursaUnidadDeAprendizaje(String boleta, String ua){
        try{
            Connection con = db.getConnection();
            CallableStatement cstmnt = 
                    con.prepareCall("{call eliminaAlumno_Cursa_Unidad_Aprendizaje(?,?)}");
            cstmnt.setString(1, boleta);
            cstmnt.setString(2, ua);
            cstmnt.executeUpdate();
            con.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        db.closeConnection();
    }
    
    public static boolean esRecurse(String boleta, String ua){
        boolean esRecurse = false;
        try{
            Connection con = db.getConnection();
            PreparedStatement pstmnt = con.prepareStatement("select isRecurse "
                    + "from Alumno_Cursa_Unidad_Aprendizaje where boleta "
                    + "like ? and idUnidad_Aprendizaje like ?");
            pstmnt.setString(1, boleta);
            pstmnt.setString(2, ua);
            ResultSet rs = pstmnt.executeQuery();
            while(rs.next()){
                esRecurse = rs.getInt(1) != 0;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        db.closeConnection();
        return esRecurse;
    }
    
    public static boolean cursaUnidadAprendizaje(String boleta, String ua){
        boolean cursaUA = false;
        try{
            Connection con = db.getConnection();
            PreparedStatement pstmnt = con.prepareStatement("select count(*) "
                    + "from Alumno_Cursa_Unidad_Aprendizaje where boleta like "
                    + "? and idUnidad_Aprendizaje like ?");
            pstmnt.setString(1, boleta);
            pstmnt.setString(2, ua);
            ResultSet rs = pstmnt.executeQuery();
            if(rs.next())
                cursaUA = rs.getInt(1) != 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        db.closeConnection();
        return cursaUA;
    }
    
    public static boolean existeBoleta(String boleta){
        boolean existeBoleta = false;
        try{
            Connection con = db.getConnection();
            PreparedStatement pstmnt = con.prepareStatement("select count(*)"
                    + " from Alumno_Periodo_Academico where boleta like ?");
            pstmnt.setString(1, boleta);
            ResultSet rs = pstmnt.executeQuery();
            if(rs.next())
                existeBoleta = rs.getInt(1) != 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        db.closeConnection();
        return existeBoleta;
    }
    
    public static String carrera (String boleta){
        String carrera = null;
        try{
            Connection con = db.getConnection();
            PreparedStatement pstmt = con.prepareStatement("select idPrograma_Academico from Alumno where boleta like ?");
            pstmt.setString(1, boleta);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                carrera = rs.getString(1);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        db.closeConnection();
        return carrera;
    }
    
    public static String obtenerNombre(String boleta){
        String nombres = "Hola ISISO :3";
        try{
            Connection con = db.getConnection();
            PreparedStatement pstmnt = con.prepareStatement("select nombre from "
                    + "Nombres_Alumno where boleta like ? order by folio asc");
            pstmnt.setString(1, boleta);
            ResultSet rs = pstmnt.executeQuery();
            StringBuilder sb = new StringBuilder();
            while(rs.next())
                sb.append(rs.getString(1).concat(" "));
            String arr[] = sb.toString().trim().split(" ");
            sb.delete(0, sb.length());
            for(int i=0; i<arr.length; i++)
                sb.append(arr[i].concat(" "));
            nombres = sb.toString().trim();
            con.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        db.closeConnection();
        return nombres;
    }
    
    public static String[] obtenerMaterias(String boleta){
    	String[] materias = null;
    	try{
    		Connection con = db.getConnection();
    		CallableStatement cstmnt = con.prepareCall("{call getMaterias(?)}");
    		cstmnt.setString(1, boleta);
    		cstmnt.executeUpdate();
    		ResultSet rs = cstmnt.getResultSet();
    		List<String> lm = new ArrayList<>();
    		while(rs.next())
    			lm.add(rs.getString(1));
    		materias = lm.toArray(new String[]{});
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	db.closeConnection();
    	return materias;
    }
    
    public static String[] obtenerMateriasMarcadas(String boleta){
    	String[] materias = null;
    	try{
    		Connection con = db.getConnection();
    		CallableStatement cstmnt = con.prepareCall("{call getMaterias_Marcadas(?)}");
    		cstmnt.setString(1, boleta);
    		cstmnt.executeUpdate();
    		ResultSet rs = cstmnt.getResultSet();
    		Parser row;
    		List<Parser> rows = new ArrayList<>();
    		while(rs.next()){
    			row = new Parser();
    			row.addInt("idAlumno_Cursa_Unidad_Aprendizaje", rs.getInt("idAlumno_Cursa_Unidad_Aprendizaje"));
    			row.addString("idUnidad_Aprendizaje", rs.getString("idUnidad_Aprendizaje"));
    			row.addString("boleta", rs.getString("boleta"));
    			row.addString("idPeriodo_Academico", rs.getString("idPeriodo_Academico"));
    			row.addBoolean("es_recurse", rs.getInt("isRecurse") > 0);
    			rows.add(row);
    		}
    		materias = new String[rows.size()];
    		int i=0;
    		for(Parser cRow : rows)
    			materias[i++] = cRow.getSerializedMessage();
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	return materias;
    }
    
}
