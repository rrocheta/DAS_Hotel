package BLL;

import DAL.ComentarioDAL;
import Model.Comentario;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class ComentarioBLL {

    /**
     * A função serve para conectar o controlador e a DAL e grava o comentário do cliente
     * @param comentario objeto do comentário
     * @throws SQLException mostra a informacao do erro de SQL
     */
    public void addComentario(Comentario comentario) throws SQLException {
        ComentarioDAL dal = new ComentarioDAL();
        dal.addComentario(comentario);
    }

    /**
     * A função serve para conectar o controlador e a DAL e tem um ID para eliminar o comentario
     * @param id id do comentario a eliminar
     * @throws SQLException mostra a informacao do erro de SQL
     */
    public void deleteComentario(int id) throws SQLException{
        ComentarioDAL dal = new ComentarioDAL();
        dal.deleteComentario(id);
    }

    /**
     * A função serve para conectar o controlador e a DAL e tem um ID para eliminar o comentario
     * @return retorna uma ObservableList com todos os tipo de comentários
     * @throws SQLException mostra a informacao do erro de SQL
     */
    public static ObservableList<Comentario> getAllComentarios() {
        return ComentarioDAL.getAllComentarios();
    }

    /**
     * A função serve para conectar o controlador e a DAL e tem um ID para eliminar o comentario
     * @return retorna uma ObservableList com todos os tipo de comentários "sugestão"
     * @throws SQLException mostra a informacao do erro de SQL
     */
    public static ObservableList<Comentario> getAllSugestoes() {
        return ComentarioDAL.getAllSugestoes();
    }

    /**
     * A função serve para conectar o controlador e a DAL e tem um ID para eliminar o comentario
     * @return retorna uma ObservableList com todos os tipo de comentários "queixa"
     * @throws SQLException mostra a informacao do erro de SQL
     */
    public static ObservableList<Comentario> getAllQueixas() {
        return ComentarioDAL.getAllQueixas();
    }

}
