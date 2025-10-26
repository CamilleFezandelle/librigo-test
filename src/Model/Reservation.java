package Model;

import java.sql.Date;

public class Reservation {

    private int id;
    private int adherentId;
    private int livreId;
    private Date dateReservation;
    private Date dateRetour;

    public Reservation(int id, int adherentId, int livreId, Date dateReservation, Date dateRetour) {
        this.id = id;
        this.adherentId = adherentId;
        this.livreId = livreId;
        this.dateReservation = dateReservation;
        this.dateRetour = dateRetour;
    }

    public Reservation(int id, int adherentId, int livreId, Date dateReservation) {
        this.id = id;
        this.adherentId = adherentId;
        this.livreId = livreId;
        this.dateReservation = dateReservation;
        this.dateRetour = null;
    }

    // Getters
    public int getId() { return id; }
    public int getAdherentId() { return adherentId; }
    public int getLivreId() { return livreId; }
    public Date getDateReservation() { return dateReservation; }
    public Date getDateRetour() { return dateRetour; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setAdherentId(int adherentId) { this.adherentId = adherentId; }
    public void setLivreId(int livreId) { this.livreId = livreId; }
    public void setDateReservation(Date dateReservation) { this.dateReservation = dateReservation; }
    public void setDateRetour(Date dateRetour) { this.dateRetour = dateRetour; }

}
