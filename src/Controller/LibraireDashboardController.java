package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LibraireDashboardController {

    @FXML private Button ajouterButton;

    @FXML
    private void handleAjouterButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/fxml/libraireCreateElements.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ajouterButton.getScene().getWindow();
            stage.setTitle("Librigo - Ajouter un élément");
            stage.setScene(new javafx.scene.Scene(root, 900, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
