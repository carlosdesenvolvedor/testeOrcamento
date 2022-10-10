    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas;

/**
 *
 * @author hugov
 */
public class Vendas {
    public static String LISTAR = "SELECT * FROM vendas ORDER BY data_ven";
    
   // public static String REGISTRARLINHA = "INSERT INTO tborc(codigo,tipoProduto,descricao,valor,quantidade,valortotal)VALUES(?,"
     //       + "?,?,?,?,?)";
    
    public static String REGISTRAR = "INSERT INTO vendas(numero_ven, total_ven, data_ven,idcli,obs) VALUES(?,?,?,?,?)";
          
    
    public static String ELIMINAR = "DELETE FROM vendas WHERE numero_ven = ?";
    
    public static String ELIMINAR_TUDO = "DELETE FROM vendas";
    
    private String primaryKey;
    private String total;
    private String data;
    private String idcli;
    //linhas da jtable
    private String idlinha;
    private String codigo;

    public String getIdlinha() {
        return idlinha;
    }

    public void setIdlinha(String idlinha) {
        this.idlinha = idlinha;
    }
    
    private String tipoDoProduto;
    private String descricao;
    private String valorUnit;
    private String quantidade1;
    private String totalProduto1;
    private String txtCampoObs;

    public String getTxtCampoObs() {
        return txtCampoObs;
    }

    public void setTxtCampoObs(String txtCampoObs) {
        this.txtCampoObs = txtCampoObs;
    }
    

    

    public String getTipoDoProduto() {
        return tipoDoProduto;
    }

    public void setTipoDoProduto(String tipoDoProduto) {
        this.tipoDoProduto = tipoDoProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getValorUnit() {
        return valorUnit;
    }

    public void setValorUnit(String valorUnit) {
        this.valorUnit = valorUnit;
    }

 

    public String getQuantidade() {
        return quantidade1;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade1 = quantidade;
    }

    public String getTotalProduto() {
        return totalProduto1;
    }

    public void setTotalProduto(String totalProduto) {
        this.totalProduto1 = totalProduto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getIdcli() {
        return idcli;
    }

    public void setIdcli(String idcli) {
        this.idcli = idcli;
    }

    public Vendas(){
        
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
