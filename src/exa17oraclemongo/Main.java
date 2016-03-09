/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exa17oraclemongo;

import java.sql.SQLException;

/**
 *
 * @author oracle
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        Exa17oraclemongo e = new Exa17oraclemongo();
        e.conexionMongo("tenda", "pedidos");
        //e.imprimirPedido();
        Exa17oraclemongo.getConexion();
        //e.disminuirStock();
        //e.aumentarGasto();
        //e.insertVendas();
        e.insertIntoMongo();
        e.closeConexion();
        e.desconexion();

    }

}
