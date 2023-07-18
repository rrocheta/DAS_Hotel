package BLL;

import DAL.CartaoDAL;
import DAL.QuartoDAL;
import Model.Cartao;
import Model.Quarto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class QuartoBLL {

    /**
     * Adiciona um quarto à base de dados.
     *
     * @param quarto O quarto a ser adicionado.
     * @throws SQLException Se houver um erro ao adicionar o quarto ou cartão à base de dados.
     */
    public void addQuarto(Quarto quarto) throws SQLException {

        CartaoDAL cdal = new CartaoDAL();
        cdal.addCartao(quarto.getCartao());
        QuartoDAL dal = new QuartoDAL();
        dal.addQuarto(quarto);

    }

    /**
     * Atualiza um quarto no banco de dados.
     *
     * @param quarto O quarto a ser atualizado.
     */
    public void updateQuarto(Quarto quarto) {
        QuartoDAL quartoDAL = new QuartoDAL();
        quartoDAL.updateQuarto(quarto);
    }

    /**
     * Método para remover um quarto através do seu identificador de cartão.
     *
     * @param idCartao identificador de cartão do quarto a ser removido
     * @throws SQLException em caso de erro na conexão com o banco de dados ou na execução da operação de remoção.
     */
    public void removeQuarto(int idCartao) throws SQLException {
        QuartoDAL dal = new QuartoDAL();
        dal.deleteQuarto(idCartao);
    }

    /**
     * Método que retorna uma lista observável de todos os quartos.
     *
     * @return uma lista observável de todos os objetos do tipo {@link Quarto}.
     */
    public static ObservableList<Quarto> getQuartos() {
        return QuartoDAL.getAllQuartos();
    }


}

