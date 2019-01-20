package view;

import javafx.fxml.FXML;

import javax.xml.soap.Text;

public class ErrorController {

    @FXML
    Text errorMassage;

    @FXML
    public void initialize(String errorMassage) {
        this.errorMassage.setValue(errorMassage);
    }

    @FXML
    void handleOkButton() {

    }
}
