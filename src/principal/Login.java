/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;


import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.sql.*;
import br.com.ingox.dal.ModuloConexao;
import javax.swing.JOptionPane;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.awt.Color;


/**
 *
 * @author hugov
 */
public class Login extends javax.swing.JFrame {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
   
    SplashScreen inicio;

    /**
     * Creates new form Login
     */
    
    
    public Login(SplashScreen inicio){
        
        this.inicio = inicio;
        setProgress(0, "Carregando componentes do sistema");
        initComponents();
        setProgress(20, "Conectando ao banco de dados!");
        setProgress(40, "Carregando os módulos");
        setProgress(60, "Carregamento de módulos concluidos");
        setProgress(80, "Carregando interfaces");
        setProgress(90, "Interface carregada");
        setProgress(100,"Bem vindo ao sistema!!");
        //this.setSize(410,500);
        conexao = ModuloConexao.conector();
    }
    
    public Login() {
         initComponents();
        conexao = ModuloConexao.conector();
        //System.out.println(conexao);
        if(conexao != null){
            lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/dbok.png")));
        }
        else{
            lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/dberror.png")));
        }
        
    }
    void setProgress(int percent, String informacao){
        inicio.getJLabel().setText(informacao);
        inicio.getJProgressBar().setValue(percent);
        try {
            Thread.sleep(200);
            
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(this, "Não foi possível carregar a inicialização");
        }
    }
    
    
    //INICIANDO A CONEXAO
    
   public void logar(){
        String sql = "select * from usuarios where nome_us= ? and senha =?";
        try {
            
            //as linhas abaixo prepara a consulta ao bd
            pst = conexao.prepareStatement(sql);
            
            pst.setString(1, txtUsuario.getText());
            pst.setString(2, txtSenha.getText());
            rs = pst.executeQuery();
            //se existir usuario e senha
            if(rs.next()){
                
                MenuPrincipal orc = new MenuPrincipal();
                orc.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(null, "Usuario ou senha inválida!!");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
  
    /**
     * Creates new form TelaLogin
     */
    /*
    public void Logar(String id, String senha){
        String dado = null;
        try {
            String sql = "SELECT usuario FROM usuarios WHERE usuario = '" +id+ "' ";
            Statement pst = conexao.createStatement();
            ResultSet rs = pst.executeQuery(sql);
            
            if(rs.first()){
                String sql1 = "SELECT senha FROM usuarios WHERE senha = '" +senha+ "' ";
                Statement st1 = conexao.createStatement();
                ResultSet rs1 = st1.executeQuery(sql1);
                if(rs1.first()){
                    String sql2 = "SELECT tipo_us FROM usuarios WHERE nome_us = '" +id+ "' "
                            + "and senha = '" +senha+ "' ";
                    Statement st2 = conexao.createStatement();
                    ResultSet rs2 = st2.executeQuery(sql2);
                    
                    while(rs2.next()){
                        dado = rs2.getString(1);
                        
                    }
                    
                    if (dado.equals("ADM")){
                        String sql3 = "SELECT nome_us FROM usuarios WHERE nome_us = '" +id+ "' ";
                        Statement st3 = conexao.createStatement();
                        ResultSet rs3 = st3.executeQuery(sql3);
                        
                         while(rs3.next()){
                         dado = rs3.getString(1);                            
                         }
                         
                         dispose();
                         MenuPrincipal menu = new MenuPrincipal();
                          JOptionPane.showMessageDialog(this, "Bem vindo ao Sistema " + dado, "Administrador", 0,
                    new ImageIcon(getClass().getResource("/imagens/usuarios/info.png")));
                          
                          menu.userConect.setText(dado);
                          menu.setLocationRelativeTo(null);
                          menu.setVisible(true);
                          
                    }else{
                        String sql4 = "SELECT nome_us FROM usuarios WHERE nome_us = '" +id+ "' ";
                        Statement st4 = conexao.createStatement();
                        ResultSet rs4 = st4.executeQuery(sql4);
                        
                         while(rs4.next()){
                         dado = rs4.getString(1);                            
                         }
                         
                         dispose();
                         MenuPrincipalP menuP = new MenuPrincipalP();
                          JOptionPane.showMessageDialog(this, "Bem vindo ao Sistema " + dado, "Usuário Padrão", 0,
                    new ImageIcon(getClass().getResource("/imagens/usuarios/info.png")));
                          
                          menuP.userConect.setText(dado);
                          menuP.setLocationRelativeTo(null);
                          menuP.setVisible(true);
                          
                    }
                    
                    
                }else{
                     JOptionPane.showMessageDialog(this, "Senha Incorreta!", "Login", 0,
                    new ImageIcon(getClass().getResource("/imagens/usuarios/info.png")));
                }
                
            }else{
                JOptionPane.showMessageDialog(this, "O usuário não existe no banco de dados!", "Login", 0,
                    new ImageIcon(getClass().getResource("/imagens/usuarios/info.png")));
            }
            
            
            
        } catch (Exception e) {
        }
    }*/
  

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        painelLogin = new javax.swing.JPanel();
        painelImgCab = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        painelCentral = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txtUsuario = new app.bolivia.swing.JCTextField();
        txtSenha = new jpass.JRPasswordField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        painelRodape = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();
        btnEntrar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setLocation(new java.awt.Point(0, 0));
        setName("telaPrincipal"); // NOI18N
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        painelLogin.setLayout(new java.awt.BorderLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/principal/imagemLogin.jpg"))); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(400, 300));

        javax.swing.GroupLayout painelImgCabLayout = new javax.swing.GroupLayout(painelImgCab);
        painelImgCab.setLayout(painelImgCabLayout);
        painelImgCabLayout.setHorizontalGroup(
            painelImgCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelImgCabLayout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(81, Short.MAX_VALUE))
        );
        painelImgCabLayout.setVerticalGroup(
            painelImgCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelImgCabLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        painelLogin.add(painelImgCab, java.awt.BorderLayout.PAGE_START);

        painelCentral.setLayout(new java.awt.BorderLayout());

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/principal/user.png"))); // NOI18N
        painelCentral.add(jLabel3, java.awt.BorderLayout.LINE_START);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtUsuario.setBackground(new java.awt.Color(34, 102, 145));
        txtUsuario.setBorder(null);
        txtUsuario.setForeground(new java.awt.Color(255, 255, 255));
        txtUsuario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtUsuario.setName("txtUsuario"); // NOI18N
        txtUsuario.setPhColor(new java.awt.Color(255, 255, 255));
        txtUsuario.setPlaceholder("USUARIO");
        txtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUsuarioKeyReleased(evt);
            }
        });
        jPanel1.add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 180, -1));

        txtSenha.setBackground(new java.awt.Color(34, 102, 145));
        txtSenha.setBorder(null);
        txtSenha.setForeground(new java.awt.Color(255, 255, 255));
        txtSenha.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtSenha.setName("txtSenha"); // NOI18N
        txtSenha.setPhColor(new java.awt.Color(255, 255, 255));
        txtSenha.setPlaceholder("SENHA");
        txtSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSenhaActionPerformed(evt);
            }
        });
        jPanel1.add(txtSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 180, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/principal/campoLoginUs.png"))); // NOI18N
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/principal/campoLoginPass.png"))); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        painelCentral.add(jPanel1, java.awt.BorderLayout.CENTER);

        painelRodape.setPreferredSize(new java.awt.Dimension(635, 60));
        painelRodape.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/principal/icon1.png"))); // NOI18N
        lblStatus.setText(".");
        painelRodape.add(lblStatus);

        btnEntrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/principal/entrar2.png"))); // NOI18N
        btnEntrar.setBorder(null);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEntrar.setName("btnEntrar"); // NOI18N
        btnEntrar.setPreferredSize(new java.awt.Dimension(135, 45));
        btnEntrar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/principal/entrar1.png"))); // NOI18N
        btnEntrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntrarActionPerformed(evt);
            }
        });
        painelRodape.add(btnEntrar);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/principal/sair2.png"))); // NOI18N
        jButton2.setBorder(null);
        jButton2.setBorderPainted(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton2.setPreferredSize(new java.awt.Dimension(135, 45));
        jButton2.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/principal/sair1.png"))); // NOI18N
        painelRodape.add(jButton2);

        painelCentral.add(painelRodape, java.awt.BorderLayout.PAGE_END);

        painelLogin.add(painelCentral, java.awt.BorderLayout.CENTER);

        getContentPane().add(painelLogin);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsuarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioKeyReleased
        txtUsuario.setText(txtUsuario.getText().toUpperCase());
    }//GEN-LAST:event_txtUsuarioKeyReleased

    private void txtSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSenhaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSenhaActionPerformed

    private void btnEntrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntrarActionPerformed
       /* String us = txtUsuario.getText();
        String pass = txtSenha.getText();
        if(us.equals("") || pass.equals("")){
             JOptionPane.showMessageDialog(this, "Preencha os Campos", "Login", 0,
                    new ImageIcon(getClass().getResource("/imagens/usuarios/info.png")));
        }else{
            Logar();
        }*/
        logar();
      
    }//GEN-LAST:event_btnEntrarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Login tela = new Login();
                tela.setLocationRelativeTo(null);
                tela.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntrar;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPanel painelCentral;
    private javax.swing.JPanel painelImgCab;
    private javax.swing.JPanel painelLogin;
    private javax.swing.JPanel painelRodape;
    private jpass.JRPasswordField txtSenha;
    private app.bolivia.swing.JCTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
