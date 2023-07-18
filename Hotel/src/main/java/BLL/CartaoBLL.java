package BLL;

import DAL.CartaoDAL;

import java.sql.SQLException;

public class CartaoBLL {

    /**
     * Recebe por parâmetro o ID de um cartão.
     *
     * @param idCartao ID do cartao para ser apagado
     */
    public void deleteCartao(int idCartao) throws SQLException {
        CartaoDAL dal = new CartaoDAL();
        dal.deleteCartao(idCartao);
    }



}
