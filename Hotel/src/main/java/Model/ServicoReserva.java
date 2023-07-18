package Model;

public class ServicoReserva {
    private int idReserva;
    private int idServico;

    public ServicoReserva(){

    }
    public ServicoReserva(int idReserva, int idServico) {
        this.idReserva = idReserva;
        this.idServico = idServico;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public int getIdServico() {
        return idServico;
    }
}
