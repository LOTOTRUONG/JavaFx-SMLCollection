package main.collection.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.collection.bean.AttributBean;
import main.collection.metier.Attribut;

import java.net.URL;
import java.util.ResourceBundle;

public class PaneAttributAjouterController implements Initializable {
    @FXML
    private TextField attributTextField;

    @FXML
    private ComboBox<Attribut> attributComboBox;
    @FXML
    private PaneTypeObjetModifyController parentController;

    private AttributBean attributBean;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        attributBean = new AttributBean();
        attributComboBox.setItems(FXCollections.observableList(attributBean.getAllAttributs()));
    }

    public void setParentController(PaneTypeObjetModifyController parentController) {
        this.parentController = parentController;
    }

    public void addNewAttribut() {
        Attribut selectedAttribute = attributComboBox.getValue();
        String newAttributeName = attributTextField.getText();

        // Trim whitespace from the new attribute name
        newAttributeName = newAttributeName.trim();

        // Check if the new attribute name already exists in the attributListView
        if (isAttributeNameDuplicate(newAttributeName)) {
            showAlert("Error", "Attribute with the same name already exists");
            return;  // Stop further processing if duplicate
        }

        // Add the new attribute to the parentController
        if (selectedAttribute != null) {
            parentController.addNewAttributeToType(selectedAttribute);
        } else {
            Attribut newAttribut = new Attribut(newAttributeName);
            parentController.addNewAttributeToType(newAttribut);
        }

        closeScene();
    }


    private boolean isAttributeNameDuplicate(String attributeName) {
        // Exclude the current attribute being added from the check
        return parentController.getAttributListView().getItems().stream()
                .filter(attribut -> !attribut.getLibelle().equalsIgnoreCase(attributeName))
                .anyMatch(attribut -> attribut.getLibelle().equalsIgnoreCase(attributeName));
    }


    public void setCancelButton() {
        closeScene();
    }

    private void closeScene() {
        Stage stage = (Stage) attributComboBox.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // Add a custom "Close" button to the alert
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(closeButton);

        // Show the alert and wait for the user's response
        alert.showAndWait();
    }

}