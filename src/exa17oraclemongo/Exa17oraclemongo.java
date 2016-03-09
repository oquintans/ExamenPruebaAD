package exa17oraclemongo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;

public class Exa17oraclemongo {

    public static Connection conexion = null;
    ResultSet rs;
    MongoClient cliente;
    MongoDatabase db;
    MongoCollection col;

    public static Connection getConexion() throws SQLException {
        String usuario = "hr";
        String password = "hr";
        String host = "localhost.localdomain"; // tambien puede ser una ip como "192.168.1.14"
        String puerto = "1521";
        String sid = "orcl";
        String ulrjdbc = "jdbc:oracle:thin:" + usuario + "/" + password + "@" + host + ":" + puerto + ":" + sid;

        conexion = DriverManager.getConnection(ulrjdbc);
        return conexion;
    }

    public static void closeConexion() throws SQLException {
        conexion.close();
        System.out.println("Conexion a oracle finalizada");
    }

    public void conexionMongo(String database, String coleccion) {
        cliente = new MongoClient("localhost", 27017);
        System.out.println("Conexion a mongo realizada.");

        db = cliente.getDatabase(database);
        System.out.println("Conectado a BD ");

        col = db.getCollection(coleccion);
        System.out.println("Conectado a colleccion scores.");
    }

    public void desconexion() {
        cliente.close();
        System.out.println("Conexion a mongo finalizada");

    }

    public void imprimirPedido() {
        BasicDBObject query1 = new BasicDBObject();
        FindIterable cursor = col.find(query1);
        MongoCursor<Document> it = cursor.iterator();
        System.out.println("Consulta en proceso");

        while (it.hasNext()) {
            Document docu = it.next();
            Pedido17 p = new Pedido17((String) docu.get("codcli"), (String) docu.get("codpro"), (Double) docu.get("cantidade"), (String) docu.get("codcli"));
            System.out.println(p.toString());
            //   System.out.println(docu);
        }
        System.out.println("Consulta finalizada");

        it.close();
    }

    public void disminuirStock() {
        BasicDBObject query1 = new BasicDBObject();
        FindIterable cursor = col.find(query1);
        MongoCursor<Document> it = cursor.iterator();
        System.out.println("Consulta en proceso");

        while (it.hasNext()) {
            Document docu = it.next();
            String cod = docu.getString("codpro");
            Double cant = docu.getDouble("cantidade");
            try {
                PreparedStatement pS = conexion.prepareStatement("update produtos set stock=stock-? where codigop=?");
                pS.setDouble(1, cant);
                pS.setString(2, cod);
                rs = pS.executeQuery();
            } catch (SQLException ex) {
                Logger.getLogger(Exa17oraclemongo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Consulta finalizada");
        it.close();
    }

    public void aumentarGasto() {
        ResultSet rs1;
        BasicDBObject query1 = new BasicDBObject();
        FindIterable cursor = col.find(query1);
        MongoCursor<Document> it = cursor.iterator();
        System.out.println("Consulta en proceso");

        while (it.hasNext()) {
            Document docu = it.next();

            String codpro = docu.getString("codpro");
            String codcli = docu.getString("codcli");
            Double cant = docu.getDouble("cantidade");
            try {
                PreparedStatement pS1 = conexion.prepareStatement("select prezo from produtos where codigop=?");
                pS1.setString(1, codpro);
                rs1 = pS1.executeQuery();
                while (rs1.next()) {
                    Double prezo = rs1.getDouble("prezo");
                    Double total = prezo * cant;
                    PreparedStatement pS = conexion.prepareStatement("update clientes set gasto=gasto+? where codigoc=?");
                    pS.setDouble(1, total);
                    pS.setString(2, codcli);
                    rs = pS.executeQuery();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Exa17oraclemongo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Consulta finalizada");
        it.close();
    }

    public void insertVendas() {
        ResultSet rs1;
        BasicDBObject query1 = new BasicDBObject();
        FindIterable cursor = col.find(query1);
        MongoCursor<Document> it = cursor.iterator();
        System.out.println("Consulta en proceso");

        while (it.hasNext()) {
            Document docu = it.next();

            String codpro = docu.getString("codpro");
            String codcli = docu.getString("codcli");
            Double cant = docu.getDouble("cantidade");
            String data = docu.getString("data");
            try {
                PreparedStatement pS1 = conexion.prepareStatement("select prezo from produtos where codigop=?");
                pS1.setString(1, codpro);
                rs1 = pS1.executeQuery();
                while (rs1.next()) {
                    Double prezo = rs1.getDouble("prezo");
                    Double total = prezo * cant;
                    PreparedStatement pS = conexion.prepareStatement("insert into vendas values(?,?,?,?)");
                    pS.setString(1, codcli);
                    pS.setString(2, codpro);
                    pS.setString(3, data);
                    pS.setDouble(4, total);
                    rs = pS.executeQuery();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Exa17oraclemongo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Consulta finalizada");
        it.close();
    }

    public void insertIntoMongo() {

        ResultSet rs1;
        BasicDBObject query1 = new BasicDBObject();
        FindIterable cursor = col.find(query1);
        MongoCursor<Document> it = cursor.iterator();
        System.out.println("Consulta en proceso");

        while (it.hasNext()) {
            Document docu = it.next();

            String codpro = docu.getString("codpro");
            String codcli = docu.getString("codcli");
            Double cant = docu.getDouble("cantidade");
            String data = docu.getString("data");
            try {
                PreparedStatement pS1 = conexion.prepareStatement("select prezo from produtos where codigop=?");
                pS1.setString(1, codpro);
                rs1 = pS1.executeQuery();
                while (rs1.next()) {
                    Double prezo = rs1.getDouble("prezo");
                    Double total = prezo * cant;

                    MongoClient cliente2 = new MongoClient("localhost", 27017);
                    System.out.println("Conexion a mongo realizada.");

                    db = cliente2.getDatabase("tenda");
                    System.out.println("Conectado a BD tenda");

                    MongoCollection col2 = db.getCollection("vendas");
                    System.out.println("Conectado a colleccion vendas.");

                    col2.insertOne(
                            new Document()
                            .append("codigoc", codcli)
                            .append("codigop", codpro)
                            .append("data", data)
                            .append("total", total)
                    );
                    System.out.println("Inserccion realizada");
                    cliente2.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Exa17oraclemongo.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
