package Controller;

import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UserSpaceController {

    @FXML private GridPane genreContainer;
    @FXML private Button backButton;

    private List<LivreReserved> booksReserved = new ArrayList<>();

    @FXML
    public void initialize() {
        booksReserved = LivreDAO.getBooksReservedByUser(Session.getInstance().getAdherent().getId());

        LocalDate today = LocalDate.now();
        int daysAllowed = 30;

        booksReserved.sort(
                Comparator.comparingLong(lr -> calculerJoursRestants(lr, today, daysAllowed))
        );

        loadBookCards(booksReserved);

    }

    private void loadBookCards(List<LivreReserved> livresReserved) {
        genreContainer.getChildren().clear();

        int COLUMNS_PER_ROW = 2;
        int column = 0;
        int row = 0;

        for (LivreReserved livreReserved : livresReserved) {
            HBox bookCard = createBookCard(livreReserved);
            genreContainer.add(bookCard, column, row);

            column++;
            if (column == COLUMNS_PER_ROW) {
                column = 0;
                row++;
            }
        }
    }

    private HBox createBookCard(LivreReserved livreReserved) {

        Livre livre = livreReserved.getLivre();
        Reservation reservation = livreReserved.getReservation();

        LocalDate dateReservation = reservation.getDateReservation().toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateReservationFormatted = dateReservation.format(formatter);

        LocalDate today = LocalDate.now();
        int daysAllowed = 30;
        long joursRestants = calculerJoursRestants(livreReserved, today, daysAllowed);

        VBox infoContainer = new VBox(5);

        infoContainer.getChildren().addAll(
                new Label(livre.getTitre()) {{ getStyleClass().add("book-title"); }},
                new Label(livre.getAuteurPrenom() + " " + livre.getAuteurNom()) {{ getStyleClass().add("book-author"); }},
                new Label(String.valueOf(livre.getDateParution())) {{ getStyleClass().add("book-year"); }},
                new Label("Livre emprunté le " + dateReservationFormatted) {{ getStyleClass().add("book-reservation-date"); }},
                new Label(
                        (joursRestants > 1)
                                ? "📅  À rendre dans " + joursRestants + " jours"
                                : (joursRestants == 1)
                                    ? "📅  À rendre demain"
                                    : (joursRestants == 0)
                                        ? "📅  À rendre aujourd'hui"
                                        : (joursRestants == -1)
                                            ? "⚠️  En retard depuis hier"
                                            : "⚠️  En retard de " + Math.abs(joursRestants) + " jours"
                ) {{
                    getStyleClass().add("book-days-status");
                    if (joursRestants >= 0) {
                        getStyleClass().add("book-days-left");
                    } else {
                        getStyleClass().add("book-days-late");
                    }

                }}

        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button returnBookButton = new Button("Rendre le livre");
        returnBookButton.getStyleClass().add("book-button");
        returnBookButton.setOnAction(event -> handleReturnBookButton(livre));


        HBox card = new HBox(20);
        card.getStyleClass().add("book-card");
        card.getChildren().addAll(infoContainer, spacer, returnBookButton);
        card.setStyle("-fx-alignment: CENTER_LEFT;");

        return card;
    }

    private long calculerJoursRestants(LivreReserved lr, LocalDate today, int daysAllowed) {
        LocalDate dateReservation = lr.getReservation().getDateReservation().toLocalDate();
        LocalDate dateLimite = dateReservation.plusDays(daysAllowed);
        return ChronoUnit.DAYS.between(today, dateLimite);
    }


    @FXML
    private void handleReturnBookButton(Livre livre) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer le retour");
        alert.setHeaderText("Rendre ce livre ?");
        alert.setContentText("Souhaitez-vous vraiment rendre \""+ livre.getTitre() + "\" ?");

        ButtonType yesButton = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                boolean success = ReservationDAO.rendreLivre(Session.getInstance().getAdherent().getId(), livre.getId());

                if (!success) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Une erreur est survenue lors du retour du livre. Veuillez réessayer plus tard.");
                    errorAlert.showAndWait();
                    return;
                }

                livre.setDisponibilite(1);

                Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                confirmation.setTitle("Retour réussi");
                confirmation.setHeaderText(null);
                confirmation.setContentText("Le livre \"" + livre.getTitre() + "\" a été rendu avec succès !");
                confirmation.showAndWait();

                initialize();
            }
        });

    }

    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/fxml/dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("LibriGo - Accueil");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
