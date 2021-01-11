/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Cristian
 */
public class Conexion {
    public static Connection conectar() {
		Connection con = null;
 
		try {
			con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ec_dinardap_ri","postgres","postgres");
			if (con != null) {
				System.out.println("Conexion Satisfactoria");
			}
 
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return con;
	}
}
