package Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class AdherentDAO {

    private static final Random random = new Random();

    public Adherent login(String email, String password) {

        try (Connection conn = ConnectionSQL.getConnection()) {
            String query = "SELECT * FROM LIBRIGO_ADHERENT WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("mot_de_passe");
                if (PasswordUtils.verifyPassword(password, dbPassword)) {
                    return new Adherent(
                            rs.getInt("id"),
                            rs.getString("numero_adherent"),
                            rs.getString("email"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getDate("date_naissance"),
                            rs.getString("adresse"),
                            rs.getString("cp"),
                            rs.getString("ville"),
                            rs.getInt("role")
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur BDD : " + e.getMessage());
        }
        return null;
    }

    public boolean emailExists(String email) {
        try (Connection conn = ConnectionSQL.getConnection()) {
            String query = "SELECT COUNT(*) AS count FROM LIBRIGO_ADHERENT WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }

        } catch (Exception e) {
            System.out.println("Erreur BDD : " + e.getMessage());
        }
        return false;
    }

    public String generateUniqueAdherentNumber() {
        String num;
        do {
            int randomNum = random.nextInt(100000);
            num = String.format("%05d", randomNum);
        } while (adherentExists(num));

        return num;
    }

    private boolean adherentExists(String numeroAdherent) {
        try (Connection conn = ConnectionSQL.getConnection()) {
            String query = "SELECT COUNT(*) AS count FROM LIBRIGO_ADHERENT WHERE numero_adherent = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, numeroAdherent);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (Exception e) {
            System.out.println("Erreur BDD : " + e.getMessage());
        }
        return false;
    }

    public void registerAdherent(String nom, String prenom, String email, String hashedPassword,
                                 Date dateNaissance, String adresse, String cp, String ville, String numeroAdherent) {

        try (Connection conn = ConnectionSQL.getConnection()) {
            String query = "INSERT INTO LIBRIGO_ADHERENT (numero_adherent, email, mot_de_passe, nom, prenom, date_naissance, adresse, cp, ville, role) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, numeroAdherent);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, nom);
            stmt.setString(5, prenom);
            stmt.setDate(6, dateNaissance);
            stmt.setString(7, adresse);
            stmt.setString(8, cp);
            stmt.setString(9, ville);
            stmt.setInt(10, 0);

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePasswordByEmail(String email, String hashedPassword) {
        try (Connection conn = ConnectionSQL.getConnection()) {
            String query = "UPDATE LIBRIGO_ADHERENT SET mot_de_passe = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, hashedPassword);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erreur BDD : " + e.getMessage());
        }
    }
}
