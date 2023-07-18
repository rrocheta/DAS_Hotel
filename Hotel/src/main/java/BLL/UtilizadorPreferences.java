package BLL;

import java.util.prefs.Preferences;

public class UtilizadorPreferences {

    private static String tipoUtilizador = "";

    private static Integer utilizadorId;

    /**
     * Guarda o tipo de utilizador logado na aplicação.
     *
     * @param tipo O tipo de utilizador (exemplo: gestor, cliente)
     */
    public static void guardartipoLogin(String tipo) {
        tipoUtilizador = tipo;
    }

    /**
     * Limpa o tipo de utilizador armazenado nas preferências.
     */
    public static void apagarTipoLogin() {
        tipoUtilizador = "";
    }

    /**
     * Método que retorna o ID da MasterCard.
     *
     * @return o ID da MasterCard (inteiro com valor 9999).
     */
    public static int masterCardId() {
        int masterCardId = 9999;
        return masterCardId;
    }

    /**
     * Verifica se o tipo de utilizador atual é Gestor ou Funcionário.
     *
     * @return Retorna verdadeiro se o tipo de utilizador é Gestor ou Funcionário, e falso caso contrário.
     */
    public static boolean useMasterCard() {
        if (getTipoLogin().equals("Gestor") || getTipoLogin().equals("Funcionario")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica se o tipo de utilizador armazenado nas preferências é o tipo "Gestor".
     *
     * @return Retorna verdadeiro se o tipo de utilizador armazenado for "Gestor". Caso contrário, retorna falso.
     */
    public static boolean comparaTipoLogin() {
        if (tipoUtilizador.equals("Gestor")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Armazena o ID do cliente na memória.
     *
     * @param id O ID do cliente a ser armazenado.
     */
    public static void guardaridCliente(Integer id) {
        utilizadorId = id;
    }

    /**
     * Obtém o ID do utilizador atualmente logado.
     *
     * @return O ID do utilizador logado.
     */
    public static Integer utilizadorId() {
        return utilizadorId;
    }

    /**
     * Método que retorna o tipo de utilizador logado na aplicação.
     *
     * @return O tipo de utilizador logado na aplicação.
     */
    public static String getTipoLogin() {
        return tipoUtilizador;
    }

    /**
     * Verifica se o tipo de utilizador armazenado nas preferências é o tipo "Funcionario".
     *
     * @return Retorna verdadeiro se o tipo de utilizador armazenado for "Funcionario". Caso contrário, retorna falso.
     */
    public static boolean comparaTipoFuncionario() {
        if (tipoUtilizador.equals("Funcionario")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica se o tipo de utilizador armazenado nas preferências é o tipo "Cliente".
     *
     * @return Retorna verdadeiro se o tipo de utilizador armazenado for "Cliente". Caso contrário, retorna falso.
     */
    public static boolean comparaTipoUtilizador() {
        if (tipoUtilizador.equals("Cliente")) {
            return true;
        } else {
            return false;
        }
    }
}


