package Model.EstacionamentoAPI;

public class TicketInfo {
    public String Id;
    public String GroupId;
    public String ClientId;
    public String LicencePlate;
    public String StartDate;
    public String EndDate;
    public String ParkingSpot;
    public Boolean Active;

    public TicketInfo() {}

    public TicketInfo(String clientId, String licencePlate, String startDate, String endDate, String parkingSpot, Boolean active) {
        GroupId = "FG4";
        ClientId = clientId;
        LicencePlate = licencePlate;
        StartDate = startDate;
        EndDate = endDate;
        ParkingSpot = parkingSpot;
        Active = active;
    }
}
