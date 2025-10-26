package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReservationDAO {

    public static boolean emprunterLivre(int adherentId, int livreId) {
        String insertReservation = "INSERT INTO LIBRIGO_RESERVATION (adherent_id, livre_id, date_reservation) " +
                       "VALUES (?, ?, CURRENT_DATE)";
        String updateLivre = "UPDATE LIBRIGO_LIVRE SET disponible = 0 WHERE id = ?";

        try (Connection conn = ConnectionSQL.getConnection()) {
            // Début transaction
            conn.setAutoCommit(false);

            PreparedStatement insertStmt = conn.prepareStatement(insertReservation);
            insertStmt.setInt(1, adherentId);
            insertStmt.setInt(2, livreId);
            insertStmt.executeUpdate();

            PreparedStatement updateStmt = conn.prepareStatement(updateLivre);
            updateStmt.setInt(1, livreId);
            updateStmt.executeUpdate();

            // Fin transaction
            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean rendreLivre(int adherentId, int livreId) {
        String endReservation = "UPDATE LIBRIGO_RESERVATION SET date_retour = CURRENT_DATE " +
                                "WHERE adherent_id = ? AND livre_id = ? AND date_retour IS NULL";
        String updateLivre = "UPDATE LIBRIGO_LIVRE SET disponible = 1 WHERE id = ?";

        try (Connection conn = ConnectionSQL.getConnection()) {
            // Début transaction
            conn.setAutoCommit(false);

            PreparedStatement endStmt = conn.prepareStatement(endReservation);
            endStmt.setInt(1, adherentId);
            endStmt.setInt(2, livreId);
            endStmt.executeUpdate();

            PreparedStatement updateStmt = conn.prepareStatement(updateLivre);
            updateStmt.setInt(1, livreId);
            updateStmt.executeUpdate();

            // Fin transaction
            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}