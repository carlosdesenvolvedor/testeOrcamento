/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ingox.dal;
import java.sql.*;


/**
 *
 * @author Plander
 */
public class ModuloConexao {
    //metodo responsavel de estabelecer a conexão com o banco de dado
    public static Connection conector(){
        java.sql.Connection conexao = null;
        //A linha abaixo chama o drive mysql
        String driver = "com.mysql.jdbc.Driver";
        //Armazenado informações referente ao banco
        String url="jdbc:mysql://localhost/produtos1?characterEncoding=utf-8";
        String user = "dba";
        String password = "Ce134679*";
        //estabelecendo a conexão e o banco: 
        try {
             Class.forName(driver);
             conexao = DriverManager.getConnection(url,user, password);
             return conexao;
             
        } catch (Exception e) {
           // System.out.println(e);
            return null;
            
        }
    }
}
