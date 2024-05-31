package com.DocConnect.docCONNECT.vmm;

import java.sql.*;

public class DBLoader {

    public static ResultSet executeSQL(String sql) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Driver Loading Done");                      //change schema name here
        Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/docconnect", "root", "system");
        System.out.println("connection done");
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        System.out.println("Statement Done");
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println("Statement Created");

        return rs;
    }

    public static ResultSet executeSql(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
