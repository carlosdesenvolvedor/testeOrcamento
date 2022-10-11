/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas;

import java.sql.*;
import br.com.ingox.dal.ModuloConexao;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.codec.Base64.InputStream;
import com.mysql.cj.protocol.a.StringValueEncoder;
import com.mysql.cj.xdevapi.Result;
import static com.sun.mail.imap.protocol.Status.add;
import com.sun.net.httpserver.HttpExchange;
import static com.sun.tools.jdeps.VersionHelper.add;
import java.awt.Desktop;
import java.awt.List;
import java.io.File;

import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.FormView;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import principal.GerarNumero;
import static principal.MenuPrincipal.carregador;
import principal.TelaCadastroCliente2;
import produtos.FrmListaProd;
import static vendas.VendasSql.cn;



/**
 *
 * @author hugov
 */
public class FrmCaixa extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form FrmCaixa
     */
    public boolean estaFechado(Object obj) {
        JInternalFrame[] ativos = carregador.getAllFrames();
        boolean fechado = true;
        int i = 0;
        while (i < ativos.length && fechado) {
            if (ativos[i] == obj) {
                fechado = false;
            }
            i++;
        }
        return fechado;
    }
    //Construtor

    public FrmCaixa() {
        initComponents();
        conexao = ModuloConexao.conector();
        FrmCaixa.tabela.getTableHeader().setDefaultRenderer(new principal.EstiloTabelaHeader());
        FrmCaixa.tabela.setDefaultRenderer(Object.class, new principal.EstiloTabelaRenderer());
        
        FrmCaixa.tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        limparCampos();
    }
    

    public static String dataAtual() {
        Date data = new Date();
        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/YYYY");
        return formatoData.format(data);

    }

    //metodo para imprimir pedido
    private void imprimirVenda() {

    }

    void limparCampos() {
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();

        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
        recebido.setText("");
        troco.setText("");
        total.setText("0.0");
        data.setText("");
        data.setText(dataAtual());

        VendasSql.numeros();
    }
    void proximoPedido() {

        VendasSql.numeros();
    }


    private void consultarCliente() {
        // String sql = "select * from tbusuarios where iduser=?";
        String sql = "select * from tbclientes where cpf=?";
        try {
            pst = conexao.prepareStatement(sql);

            //pst.setString(1, txtUsuId.getText());
            pst.setString(1, txtCpf.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                lblIdcli.setText(rs.getString("idcli"));
                lblCpf.setText(rs.getString(10));
                lblNome.setText(rs.getString("nome"));
                lblTelefone.setText(rs.getString("telefone"));
                lblEmail.setText(rs.getString("email"));
                lblCep.setText(rs.getString("cep"));
                lblRua.setText(rs.getString("rua"));
                lblBairro.setText(rs.getString("bairro"));
                lblCidade.setText(rs.getString("cidade"));
                lblEstado.setText(rs.getString("estado"));
                //cboUsoPerfil.setSelectedItem(rs.getString(6)); //não tenho certeza se é isto

            } else {
                JOptionPane.showMessageDialog(null, "Cliente não cadastrado");
                //as linhas abqaixo linpa os campos
                lblCpf.setText(null);
                lblTelefone.setText(null);
                lblEmail.setText(null);
                lblRua.setText(null);
                lblBairro.setText(null);
                lblCidade.setText(null);
                lblEstado.setText(null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void gerarRelatorio(String codVenda) {
        

        String sql = "SELECT\n" +
"     tborc.`idlinha` AS tborc_idlinha,\n" +
"     tborc.`tipoProduto` AS tborc_tipoProduto,\n" +
"     tborc.`descricao` AS tborc_descricao,\n" +
"     tborc.`valor` AS tborc_valor,\n" +
"     tborc.`quantidade` AS tborc_quantidade,\n" +
"     tborc.`valortotal` AS tborc_valortotal,\n" +
"     tborc.`num_vem` AS tborc_num_vem,\n" +
"     vendas.`numero_ven` AS vendas_numero_ven,\n" +
"     vendas.`total_ven` AS vendas_total_ven,\n" +
"     vendas.`data_ven` AS vendas_data_ven,\n" +
"     vendas.`idcli` AS vendas_idcli,\n" +
"     tbclientes.`idcli` AS tbclientes_idcli,\n" +
"     tbclientes.`nome` AS tbclientes_nome,\n" +
"     tbclientes.`telefone` AS tbclientes_telefone,\n" +
"     tbclientes.`email` AS tbclientes_email,\n" +
"     tbclientes.`cep` AS tbclientes_cep,\n" +
"     tbclientes.`rua` AS tbclientes_rua,\n" +
"     tbclientes.`bairro` AS tbclientes_bairro,\n" +
"     tbclientes.`estado` AS tbclientes_estado,\n" +
"     tbclientes.`cidade` AS tbclientes_cidade,\n" +
"     tbclientes.`cpf` AS tbclientes_cpf,\n" +
"     vendas.`obs` AS vendas_obs\n" +
"FROM\n" +
"     `vendas` vendas INNER JOIN `tborc` tborc ON vendas.`numero_ven` = tborc.`num_vem`\n" +
"     INNER JOIN `tbclientes` tbclientes ON vendas.`idcli` = tbclientes.`idcli`\n" +
"where tborc.num_vem = '" + codVenda + "'  ";

        try {

            pst = conexao.prepareStatement(sql);
            //pst.setString(1,lblvenda.getText());
            rs = pst.executeQuery();
            JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
            //caminho do relatório
            HashMap filtro = new HashMap();
            filtro.put("tborc.num_vem", lblvenda.getText());
            System.out.println(codVenda);
            JasperPrint relatorioPreenchido;
            InputStream caminhoRelatorio;
            caminhoRelatorio = (InputStream) this.getClass().getClassLoader().getResourceAsStream("C:/Users/Plander/OneDrive/Documentos/NetBeansProjects/Arquivos/Arquivos Sistema Finalizado/sistema/src/reports/aula3.jasper");
            relatorioPreenchido = JasperFillManager.fillReport(caminhoRelatorio, filtro, jrRS);
            
            JasperExportManager.exportReportToPdfFile(relatorioPreenchido, "C:/Users/Plander/OneDrive/Ambiente de Trabalho/orcamentos/relatoriovenda.pdf");

            File file = new File("C:/Users/PlanderOneDrive/Ambiente de Trabalho/orcamentos/relatoriovenda.pdf");
            try {
                Desktop.getDesktop().open(file);

            } catch (Exception e) {
                JOptionPane.showConfirmDialog(null, e);
            }
            file.deleteOnExit();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

            e.printStackTrace();

        } finally {

        }

    }

    private void adicionarLnha() {
        String tipoDoProduto;
        String descricao;
        String valorUnit;
        String quantidade;
        String totalProduto;
        int adicionado;

        DefaultTableModel tblModel = (DefaultTableModel) tabela.getModel();

        if (tblModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "vazio");
        } else {

            try {

                for (int i = 0; i < tblModel.getRowCount(); i++) {

                    tipoDoProduto = tblModel.getValueAt(i, 1).toString();
                    descricao = tblModel.getValueAt(i, 2).toString();
                    valorUnit = tblModel.getValueAt(i, 3).toString();
                    quantidade = tblModel.getValueAt(i, 4).toString();
                    totalProduto = tblModel.getValueAt(i, 5).toString();

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
                            vendas.FrmCaixa.lblvenda.setText("1");
                        } else {
                            j = Integer.parseInt(c);
                            GerarNumero gen = new GerarNumero();
                            gen.gerar(j);

                        }

                    } catch (SQLException ex) {
                        Logger.getLogger(VendasSql.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    // v.setIdcli(lblIdcli.getText());
                    String sql = "INSERT INTO tborc(tipoProduto,descricao,valor,quantidade,valortotal,num_vem)VALUES(?,"
                            + "?,?,?,?,?)";
                    pst = conexao.prepareStatement(sql);

                    Vendas v = new Vendas();
                    pst.setString(1, tipoDoProduto);
                    pst.setString(2, descricao);
                    pst.setString(3, valorUnit.replace(".", ","));
                    pst.setString(4, quantidade);
                    pst.setString(5, totalProduto.replace(".", ","));

                    pst.setString(6, FrmCaixa.lblvenda.getText());

                    adicionado = pst.executeUpdate();

                    if (tblModel.getRowCount() == adicionado) {

                        /*txtNome.setText(null);
                txtTelefone.setText(null);
                txtEmail.setText(null);
                txtCep.setText(null);
                txtRua.setText(null);
                cboEstado.setSelectedItem(null);
                txtBairro.setText(null);
                txtCidade.setText(null);
                txtCpf.setText(null);*/
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }
    private void imprimirOs() {
       
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão desta OS?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                HashMap filtro = new HashMap();
                filtro.put("num_vem", lblvenda.getText());
                JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream("src/reports/aula4.jasper"), filtro, conexao);
                JasperViewer.viewReport(print, false);
                conexao.close();
            } catch (NumberFormatException | SQLException | JRException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        busca = new javax.swing.JButton();
        data = new app.bolivia.swing.JCTextField();
        codigoL5 = new javax.swing.JLabel();
        recebido = new app.bolivia.swing.JCTextField();
        codigoL6 = new javax.swing.JLabel();
        troco = new app.bolivia.swing.JCTextField();
        codigoL7 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        calculo = new javax.swing.JButton();
        vender = new javax.swing.JButton();
        excluir = new javax.swing.JButton();
        cancelar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabela = new javax.swing.JTable();
        total = new app.bolivia.swing.JCTextField();
        codigoL3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtCampoObs = new javax.swing.JTextPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        lblNome = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        lblCpf = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        lblTelefone = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        lblCidade = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        lblBairro = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        lblRua = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        lblCep = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txtCpf = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        lblIdcli = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        lblvenda = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Caixa");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        busca.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        busca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/vendas/produto1.png"))); // NOI18N
        busca.setBorder(null);
        busca.setContentAreaFilled(false);
        busca.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        busca.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        busca.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/vendas/produto.png"))); // NOI18N
        busca.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscaActionPerformed(evt);
            }
        });
        jPanel2.add(busca, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        data.setEditable(false);
        data.setBackground(new java.awt.Color(34, 102, 145));
        data.setBorder(null);
        data.setForeground(new java.awt.Color(255, 255, 255));
        data.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        data.setPhColor(new java.awt.Color(255, 255, 255));
        data.setPlaceholder("DATA");
        jPanel2.add(data, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 180, -1));

        codigoL5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/vendas/data.png"))); // NOI18N
        jPanel2.add(codigoL5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, 52));

        recebido.setBackground(new java.awt.Color(34, 102, 145));
        recebido.setBorder(null);
        recebido.setForeground(new java.awt.Color(255, 255, 255));
        recebido.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        recebido.setPhColor(new java.awt.Color(255, 255, 255));
        recebido.setPlaceholder("RECIBIDO");
        jPanel2.add(recebido, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 20, 130, -1));

        codigoL6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/vendas/recibi.png"))); // NOI18N
        codigoL6.setToolTipText("RECIBI");
        jPanel2.add(codigoL6, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, -1, 52));

        troco.setEditable(false);
        troco.setBackground(new java.awt.Color(34, 102, 145));
        troco.setBorder(null);
        troco.setForeground(new java.awt.Color(255, 255, 255));
        troco.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        troco.setPhColor(new java.awt.Color(255, 255, 255));
        troco.setPlaceholder("TROCO");
        jPanel2.add(troco, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 20, 130, -1));

        codigoL7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/vendas/valores.png"))); // NOI18N
        codigoL7.setToolTipText("CAMBIO");
        jPanel2.add(codigoL7, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, -1, 52));

        jButton5.setText("Recalcular");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 30, -1, -1));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "OPÇÕES", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        calculo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        calculo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/vendas/calculo1.png"))); // NOI18N
        calculo.setBorder(null);
        calculo.setContentAreaFilled(false);
        calculo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        calculo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        calculo.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/vendas/calculo2.png"))); // NOI18N
        calculo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        calculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculoActionPerformed(evt);
            }
        });

        vender.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        vender.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/vendas/venda1.png"))); // NOI18N
        vender.setBorder(null);
        vender.setContentAreaFilled(false);
        vender.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        vender.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        vender.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/vendas/venda2.png"))); // NOI18N
        vender.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        vender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                venderActionPerformed(evt);
            }
        });

        excluir.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        excluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/vendas/elimina1.png"))); // NOI18N
        excluir.setBorder(null);
        excluir.setContentAreaFilled(false);
        excluir.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        excluir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        excluir.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/vendas/elimina2.png"))); // NOI18N
        excluir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        excluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excluirActionPerformed(evt);
            }
        });

        cancelar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/vendas/cancela1.png"))); // NOI18N
        cancelar.setBorder(null);
        cancelar.setContentAreaFilled(false);
        cancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cancelar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/vendas/cancela2.png"))); // NOI18N
        cancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 204, 204));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Salvar Orçamento");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setText("Novo Orçamento");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(calculo)
                            .addComponent(excluir)
                            .addComponent(vender)
                            .addComponent(cancelar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addComponent(jButton1))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(calculo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(vender)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(excluir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CÓDIGO", "TIPO PRODUTO", "DESCRIÇÃO", "VALOR", "QUANTIDADE", "TOTAL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabela.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabela.setEditingRow(50);
        jScrollPane1.setViewportView(tabela);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 790, 170));

        total.setEditable(true);
        total.setBackground(new java.awt.Color(34, 102, 145));
        total.setBorder(null);
        total.setForeground(new java.awt.Color(255, 255, 255));
        total.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        total.setPhColor(new java.awt.Color(255, 255, 255));
        total.setPlaceholder("TOTAL");
        jPanel4.add(total, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 230, 80, 30));

        codigoL3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/vendas/total.png"))); // NOI18N
        jPanel4.add(codigoL3, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 220, -1, 50));

        jScrollPane2.setViewportView(txtCampoObs);

        jPanel4.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 550, 80));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setText("Empresa");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Orquestral", "M.R", "S.C", "Atelier", "Todos", " ", " " }));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Nome");

        lblNome.setText(".");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("CPF/CNPJ");

        lblCpf.setText(".");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setText("Tel");

        lblTelefone.setText(".");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel26.setText("Email");

        lblEmail.setText(".");

        lblEstado.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEstado.setText(".");

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel36.setText("UR");

        lblCidade.setText(".");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setText("Cidade");

        lblBairro.setText(".");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel32.setText("Bairro ");

        lblRua.setText(".");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel29.setText("Rua");

        lblCep.setText(".");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel28.setText("Cep");

        jButton6.setText("Cadastrar Cliente");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel11.setText("CPF/CNPJ");

        jButton8.setText("Buscar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel37.setText("id");

        lblIdcli.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblIdcli.setText("no");

        jButton3.setText("Orquestral Imprimir");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setText("S.C imprimir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton9.setText("M.R imprimir");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel23))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton8)
                        .addGap(136, 136, 136))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(lblTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblIdcli, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel28)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(lblCep, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel29)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(lblRua, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel32)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(lblBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel33)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(lblCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel36)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(lblEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(81, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jButton6)
                    .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmail)
                    .addComponent(jLabel26)
                    .addComponent(lblTelefone)
                    .addComponent(jLabel25)
                    .addComponent(lblCpf)
                    .addComponent(jLabel23)
                    .addComponent(lblNome)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(lblIdcli))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton2)
                    .addComponent(jButton9))
                .addContainerGap())
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(64, 64, 64)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblBairro)
                        .addComponent(jLabel33)
                        .addComponent(lblCidade)
                        .addComponent(jLabel36)
                        .addComponent(lblEstado)
                        .addComponent(jLabel32)
                        .addComponent(lblRua)
                        .addComponent(jLabel29)
                        .addComponent(lblCep)
                        .addComponent(jLabel28))
                    .addContainerGap(42, Short.MAX_VALUE)))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setText("Número do Orçamento");
        jPanel6.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));
        jLabel15.getAccessibleContext().setAccessibleName("NÚMERO DA VENDA");

        lblvenda.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblvenda.setText("0");
        jPanel6.add(lblvenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 120, -1));

        jButton7.setText("proximo");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 11, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    produtos.FrmListaProd lista;
    private void buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscaActionPerformed
        if (estaFechado(lista)) {
            lista = new FrmListaProd();
            principal.MenuPrincipal.carregador.add(lista);

            lista.toFront();
            lista.setVisible(true);
        } else {
            lista.toFront();

        }

    }//GEN-LAST:event_buscaActionPerformed

    private void calculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculoActionPerformed
       FrmListaProd cal = new FrmListaProd();
       cal.calcular();
        if (tabela.getRowCount() < 1) {
            JOptionPane.showMessageDialog(this, "Operação não realizada.", "Erro", JOptionPane.ERROR_MESSAGE);
        } else if (recebido.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Insira um valor.", "Cobrança", 0,
                    new ImageIcon(getClass().getResource("/imagens/usuarios/info.png")));
        } else {
            double recebi = Double.parseDouble(recebido.getText());
            double tot = Double.parseDouble(total.getText());

            if (recebi < tot) {
                JOptionPane.showMessageDialog(this, "Valor Inválido para essa venda", "Compra", 0,
                        new ImageIcon(getClass().getResource("/imagens/usuarios/info.png")));
            } else {
                this.troco.setText(String.valueOf(recebi - tot));
            }
            
        }
        
    }//GEN-LAST:event_calculoActionPerformed

    private void venderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_venderActionPerformed
        if (tabela.getRowCount() < 1) {
            JOptionPane.showMessageDialog(this, "Impossível realizar a venda.", "Erro", JOptionPane.ERROR_MESSAGE);
        } else {
            Vendas v = new Vendas();
//            v.setPrimaryKey(lblvenda.getText());
            v.setTotal(total.getText());
            v.setData(data.getText());
            v.setIdcli(lblIdcli.getText());

            int opc = VendasSql.registrar(v);
            if (opc != 0) {
                limparCampos();
                JOptionPane.showMessageDialog(this, "Orçamento efeturado.", "orçamento", 0,
                        new ImageIcon(getClass().getResource("/imagens/usuarios/info.png")));

            }
        }
    }//GEN-LAST:event_venderActionPerformed

    private void excluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excluirActionPerformed
        if (tabela.getRowCount() > 0) {
            DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                modelo.removeRow(linha);
                FrmListaProd lp = new FrmListaProd();
                lp.calcular();
            } else {
                JOptionPane.showMessageDialog(this, "Selecionar uma Linha.", "Venda", 0,
                        new ImageIcon(getClass().getResource("/imagens/usuarios/info.png")));
            }
        } else {
            JOptionPane.showMessageDialog(this, "Não há registro para excluir.", "Venda", 0,
                    new ImageIcon(getClass().getResource("/imagens/usuarios/info.png")));
        }
    }//GEN-LAST:event_excluirActionPerformed

    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed
        limparCampos();
    }//GEN-LAST:event_cancelarActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:TeT

        TelaCadastroCliente2 tela = new TelaCadastroCliente2();
        tela.setVisible(true);

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        consultarCliente();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code 
        if (tabela.getRowCount() < 1) {
            JOptionPane.showMessageDialog(this, "Impossível realizar a venda.", "Erro", JOptionPane.ERROR_MESSAGE);
        } else {
            Vendas v = new Vendas();

            v.setPrimaryKey(lblvenda.getText());
            v.setTotal(total.getText());
            v.setData(data.getText());
            v.setIdcli(lblIdcli.getText());
            v.setTxtCampoObs(txtCampoObs.getText());

            int opc = VendasSql.registrar(v);
            adicionarLnha();
            if (opc != 0) {

                //limparCampos();
                JOptionPane.showMessageDialog(this, "Orçamento efeturado.", "orçamento", 0,
                        new ImageIcon(getClass().getResource("/imagens/usuarios/info.png")));

            }
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        
        int confirma = JOptionPane.showConfirmDialog(null, "confima impreção do orcamento", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
                
            try 
            {
            //pst.setString(1,lblvenda.getText());
           
                HashMap filtro = new HashMap();
                filtro.put("num_vem", lblvenda.getText());
                //usando a classe JasperPrint para prepara a impressão de um relatorio
                JasperPrint print = JasperFillManager.fillReport("src/reports/aula3.jasper",
                        filtro, conexao);
                
               
                
                //a linha abaixo exibe o relatório através da classe JasperViewer
                
                
                JasperViewer.viewReport(print, false);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int confirma = JOptionPane.showConfirmDialog(null, "confima impreção do orcamento", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
                
            try 
            {
            //pst.setString(1,lblvenda.getText());
           
                HashMap filtro = new HashMap();
                filtro.put("num_vem", lblvenda.getText());
                //usando a classe JasperPrint para prepara a impressão de um relatorio
                JasperPrint print = JasperFillManager.fillReport("src/reports/aula6.jasper",
                        filtro, conexao);
                
               
                
                //a linha abaixo exibe o relatório através da classe JasperViewer
                
                
                JasperViewer.viewReport(print, false);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        limparCampos();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        FrmListaProd cal = new FrmListaProd();
         cal.calcular();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        proximoPedido();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
         int confirma = JOptionPane.showConfirmDialog(null, "confima impreção do orcamento", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
                
            try 
            {
            //pst.setString(1,lblvenda.getText());
           
                HashMap filtro2 = new HashMap();
                filtro2.put("num_vem", lblvenda.getText());
                //usando a classe JasperPrint para prepara a impressão de um relatorio
                JasperPrint print = JasperFillManager.fillReport("src/reports/aula5.jasper",
                        filtro2, conexao);
                
               
                
                //a linha abaixo exibe o relatório através da classe JasperViewer
                
                
                JasperViewer.viewReport(print, false);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }
    }//GEN-LAST:event_jButton9ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton busca;
    private javax.swing.JButton calculo;
    private javax.swing.JButton cancelar;
    private javax.swing.JLabel codigoL3;
    private javax.swing.JLabel codigoL5;
    private javax.swing.JLabel codigoL6;
    private javax.swing.JLabel codigoL7;
    private app.bolivia.swing.JCTextField data;
    private javax.swing.JButton excluir;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBairro;
    private javax.swing.JLabel lblCep;
    private javax.swing.JLabel lblCidade;
    private javax.swing.JLabel lblCpf;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblIdcli;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblRua;
    private javax.swing.JLabel lblTelefone;
    public static javax.swing.JLabel lblvenda;
    public static app.bolivia.swing.JCTextField recebido;
    public static javax.swing.JTable tabela;
    public static app.bolivia.swing.JCTextField total;
    public static app.bolivia.swing.JCTextField troco;
    private javax.swing.JTextPane txtCampoObs;
    private javax.swing.JTextField txtCpf;
    private javax.swing.JButton vender;
    // End of variables declaration//GEN-END:variables

    private static class MashMap {

        public MashMap() {
        }
    }
}
