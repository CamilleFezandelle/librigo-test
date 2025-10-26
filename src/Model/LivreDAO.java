package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO {

    public static List<Livre> getAllLivres() {

        List<Livre> livres = new ArrayList<>();

        String query = "SELECT l.id, " +
                "l.ISBN, " +
                "l.titre, " +
                "l.auteur_id, " +
                "a.nom, " +
                "a.prenom, " +
                "l.genre_id, " +
                "g.nom, " +
                "l.date_parution, " +
                "l.description, " +
                "l.disponible " +
                "FROM LIBRIGO_LIVRE l " +
                "JOIN LIBRIGO_AUTEUR a ON l.auteur_id = a.id " +
                "JOIN LIBRIGO_GENRE g ON l.genre_id = g.id";

        try (Connection conn = ConnectionSQL.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Livre livre = new Livre(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getInt(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getInt(11)
                );
                livres.add(livre);
            }
        } catch (Exception e) {
            System.out.println("Erreur BDD : " + e.getMessage());
        }
        return livres;
    }

    public static List<LivreReserved> getBooksReservedByUser(int adherentId) {

        List<LivreReserved> livresReserved = new ArrayList<>();

        String query = "SELECT l.id, " +
                "l.ISBN, " +
                "l.titre, " +
                "l.auteur_id, " +
                "a.nom, " +
                "a.prenom, " +
                "l.genre_id, " +
                "g.nom, " +
                "l.date_parution, " +
                "l.description, " +
                "l.disponible, " +
                "r.id, " +
                "r.date_reservation, " +
                "r.date_retour " +
                "FROM LIBRIGO_LIVRE l " +
                "JOIN LIBRIGO_AUTEUR a ON l.auteur_id = a.id " +
                "JOIN LIBRIGO_GENRE g ON l.genre_id = g.id " +
                "JOIN LIBRIGO_RESERVATION r ON r.livre_id = l.id " +
                "WHERE r.adherent_id = ? " +
                "AND r.date_retour IS NULL";

        try (Connection conn = ConnectionSQL.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, adherentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Livre livre = new Livre(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getInt(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getInt(11)
                );

                Reservation reservation = new Reservation(
                        rs.getInt(12),
                        adherentId,
                        rs.getInt(1),
                        rs.getDate(13),
                        rs.getDate(14)
                );

                livresReserved.add(new LivreReserved(livre, reservation));
            }
        } catch (Exception e) {
            System.out.println("Erreur BDD : " + e.getMessage());
        }
        return livresReserved;
    }

    public List<Livre> getLivresByGenre(int genreId) {

        List<Livre> livres = new ArrayList<>();

        String query = "SELECT l.id, " +
                "l.ISBN, " +
                "l.titre, " +
                "l.auteur_id, " +
                "a.nom, " +
                "a.prenom, " +
                "l.genre_id, " +
                "g.nom, " +
                "l.date_parution, " +
                "l.description, " +
                "l.disponible " +
                "FROM LIBRIGO_LIVRE l " +
                "JOIN LIBRIGO_AUTEUR a ON l.auteur_id = a.id " +
                "JOIN LIBRIGO_GENRE g ON l.genre_id = g.id " +
                "WHERE l.genre_id = ?";

        try (Connection conn = ConnectionSQL.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, genreId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Livre livre = new Livre(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getInt(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getInt(11)
                );
                livres.add(livre);
            }
        } catch (Exception e) {
            System.out.println("Erreur BDD : " + e.getMessage());
        }
        return livres;
    }

    public List<Genre> getAllGenres() {

        List<Genre> genres = new ArrayList<>();

        String query = "SELECT id, nom FROM LIBRIGO_GENRE";

        try (Connection conn = ConnectionSQL.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                genres.add(new Genre(rs.getInt(1), rs.getString(2)));
            }
        } catch (Exception e) {
            System.out.println("Erreur BDD : " + e.getMessage());
        }
        return genres;
    }

}
