/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import principal.Conectar;
import principal.GerarNumero;

/**
 *
 * @author hugov
 */
public class VendasSql {
    static Conectar cc = new Conectar();
    static Connection cn = cc.conexao();
    static PreparedStatement ps;
    
    
    public static int registrar(Vendas uc) {
        int rsu = 0;
        String sql = Vendas.REGISTRAR;
        try {
            ps = cn.prepareStatement(sql);
            ps.setString(1, uc.getPrimaryKey());
            ps.setString(2, uc.getTotal());
            ps.setString(3, uc.getData());
            ps.setString(4, uc.getIdcli());
            ps.setString(5, uc.getTxtCampoObs());
           /* ps.setString(4, uc.getLinha1());
            ps.setString(5, uc.getLinha2());
            ps.setString(6, uc.getLinha3());
            ps.setString(7, uc.getLinha4());
            ps.setString(8, uc.getLinha5());
            ps.setString(9, uc.getLinha6());
            ps.setString(10, uc.getLinha7());
            ps.setString(11, uc.getLinha8());
            ps.setString(12, uc.getLinha9());
            ps.setString(13, uc.getLinha10());
            ps.setString(14, uc.getLinha11());
            ps.setString(15, uc.getLinha12());
            ps.setString(16, uc.getLinha13());
            ps.setString(17, uc.getLinha14());
            ps.setString(18, uc.getLinha15());
            ps.setString(19, uc.getLinha16());
            ps.setString(20, uc.getLinha17());
            ps.setString(21, uc.getLinha18());
            ps.setString(22, uc.getLinha19());
            ps.setString(23, uc.getLinha20());*/
            
         
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println(sql);
        return rsu;
    }
   /* public static int registrarlinha(Vendas uc) {
        int rsu = 0;
        String sql = Vendas.REGISTRARLINHA;
        try {
           
            ps = cn.prepareStatement(sql);
            ps.setString(1, uc.getCodigo());
            ps.setString(2, uc.getTipoDoProduto());
            ps.setString(3, uc.getDescricao());
            ps.setString(4, uc.getValorUnit());
            ps.setString(5, uc.getQuantidade());
            ps.setString(6, uc.getTotalProduto());

            
         
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println(sql);
        return rsu;
    }*/


    public static int eliminar(String id) {
        int rsu = 0;
        String sql = Vendas.ELIMINAR;

        try {
            ps = cn.prepareStatement(sql);
            ps.setString(1, id);
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println(sql);
        return rsu;
    }

    public static int eliminaTodos() {
        int rsu = 0;
        String sql = Vendas.ELIMINAR_TUDO;
        try {
            ps = cn.prepareStatement(sql);
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println(sql);
        return rsu;
    }

    public static void listar(String busca) {
        DefaultTableModel modelo = (DefaultTableModel) vendas.FrmVendas.tabela.getModel();

        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
        String sql = "";
        if (busca.equals("")) {
            sql = Vendas.LISTAR;
        } else {
            sql = "SELECT * FROM vendas WHERE (numero_ven like'" + busca + "%' or data_ven='" + busca + "')"
                    + " ORDER BY data_ven";
        }
        String dados[] = new String[4];
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                dados[0] = rs.getString("numero_ven");
                dados[1] = rs.getString("total_ven");
                dados[2] = rs.getString("data_ven");
                modelo.addRow(dados);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VendasSql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   public static void numeros() {
        int j;
        int cont = 1;
        String num = "";
        String c = "";
        String SQL = "SELECT MAX(numero_ven) FROM vendas";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            if (rs.next()) {
                c = rs.getString(1);
            }
            

            if (c == null) {
                vendas.FrmCaixa.lblvenda.setText("00000001");
            } else {
                j = Integer.parseInt(c);
                GerarNumero gen = new GerarNumero();
                gen.gerar(j);
                vendas.FrmCaixa.lblvenda.setText(gen.serie());

            }

        } catch (SQLException ex) {
            Logger.getLogger(VendasSql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void numeros1() {
        int j;
        int cont = 1;
        String num = "";
        String c = "";
        String SQL = "SELECT MAX(numero_venda) FROM vendas";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            if (rs.next()) {
                c = rs.getString(1);
            }

            if (c == null) {
                vendas.FrmCaixa.lblvenda.setText("00000001");
            } else {
                j = Integer.parseInt(c);
                GerarNumero gen = new GerarNumero();
                gen.gerar(j);
                vendas.FrmCaixa.lblvenda.setText(gen.serie());

            }

        } catch (SQLException ex) {
            Logger.getLogger(VendasSql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
