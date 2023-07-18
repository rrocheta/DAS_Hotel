package Model;

import java.sql.Date;

public class CheckIn {
    private int id;
    private int quartoId;
    private Date dataCheckIn;
    private Date dataCheckOut;

    public CheckIn(int id, int quartoId, Date dataCheckIn, Date dataCheckOut) {
        this.id = id;
        this.quartoId = quartoId;
        this.dataCheckIn = dataCheckIn;
        this.dataCheckOut = dataCheckOut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuartoId() {
        return quartoId;
    }

    public void setQuartoId(int quartoId) {
        this.quartoId = quartoId;
    }

    public Date getDataCheckIn() {
        return dataCheckIn;
    }

    public void setDataCheckIn(Date dataCheckIn) {
        this.dataCheckIn = dataCheckIn;
    }

    public Date getDataCheckOut() {
        return dataCheckOut;
    }

    public void setDataCheckOut(Date dataCheckOut) {
        this.dataCheckOut = dataCheckOut;
    }
}
