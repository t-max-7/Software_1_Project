package tmax7.inventory.management.system.controllers;

import tmax7.inventory.management.system.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public class ModifyPartController {
    private MainApp mainApp;
    private Stage stage;
    private Part partToModify;
    private TableView<Part> partTableView;

    private Part modifiedPart;

    private AtomicBoolean dollarSignUsed = new AtomicBoolean(false);

    @FXML
    private RadioButton inHouseRadioButton;
    @FXML
    private RadioButton outSourcedRadioButton;

    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField inventoryTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField maxTextField;
    @FXML
    private TextField minTextField;
    @FXML
    private TextField lastTextField;

    @FXML
    private Label lastLabel;

    @FXML
    private void initialize() {
        //make id non-editable
        idTextField.setEditable(false);

        //Change lastLabel text and lastTextField text based on radio button selection amd initialize modifiedPart with object of correct Class;
        inHouseRadioButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!lastLabel.getText().equals("Machine ID")){
                    lastLabel.setText("Machine ID");
                    lastTextField.setText("Mach ID");
                    modifiedPart = new InHouse(0, null, 0.0,0,0,0,0);
                }
            }
        });

        outSourcedRadioButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(!lastLabel.getText().equals("Company Name")){
                    lastLabel.setText("Company Name");
                    lastTextField.setText("Comp Nm");
                    modifiedPart = new Outsourced(0, null, 0.0, 0, 0, 0, null);
                }
            }
        });
    }

    @FXML
    private void onSaveClicked() {
        if(isInputValid()){
            int id = partToModify.getId();
            String name = nameTextField.getText();

            double price;
            if(dollarSignUsed.get()){
                price = Double.parseDouble(priceTextField.getText().substring(MainApp.indexAfterDollarSign));
            } else {
                price = Double.parseDouble(priceTextField.getText());
            }

            int stock = Integer.parseInt(inventoryTextField.getText());
            int min = Integer.parseInt(minTextField.getText());
            int max = Integer.parseInt(maxTextField.getText());

            if(isInHouse() && modifiedPart instanceof InHouse){
                int machineId = Integer.parseInt(lastTextField.getText());

                modifiedPart.setID(id);
                modifiedPart.setName(name);
                modifiedPart.setPrice(price);
                modifiedPart.setStock(stock);
                modifiedPart.setMin(min);
                modifiedPart.setMax(max);
                ((InHouse) modifiedPart).setMachineId(machineId);
            } else if(isOutsource() && modifiedPart instanceof Outsourced){
                String companyName = lastTextField.getText();

                modifiedPart.setID(id);
                modifiedPart.setName(name);
                modifiedPart.setPrice(price);
                modifiedPart.setStock(stock);
                modifiedPart.setMin(min);
                modifiedPart.setMax(max);
                ((Outsourced) modifiedPart).setCompanyName(companyName);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Something Went Wrong");
                alert.setContentText("Could Not Modify Part");

                alert.showAndWait();
            }

            //Must use this method because Part's fields are not instances of ObservableProperty so must update TableView manually;
            updatePartTableView();

            stage.close();
        }
    }

    private boolean isInputValid(){
        String errorMessage = "";

        if(nameTextField.getText() == null || nameTextField.getText().length() == 0) {
            errorMessage += "Must Enter Name\n";
        }

        if(inventoryTextField.getText() == null || inventoryTextField.getText().length() == 0) {
            errorMessage += "Must Enter Inventory\n";
        } else if (!MainApp.isIntegerString(inventoryTextField.getText())) {
            errorMessage += "Inventory Must Be An Integer (e.g. 15)\n";
        } else if (
                !(
                        Integer.parseInt(inventoryTextField.getText()) <= Integer.parseInt(maxTextField.getText())
                                && Integer.parseInt(inventoryTextField.getText()) >= Integer.parseInt(minTextField.getText())
                )
        ) {
            errorMessage += "Inventory Must Be A Value Equal To Or Between Max And Min (i.e. Max >= Inventory >= Min)\n";
        }

        if(priceTextField.getText() == null || priceTextField.getText().length() == 0) {
            errorMessage += "Must Enter Price\n";
        } else if (!MainApp.isDoubleString(priceTextField.getText(), dollarSignUsed)) {
            errorMessage += "Price Must Be In Decimal Form (e.g. 140.56, 50.00)\n";
        }

        if(maxTextField.getText() == null || maxTextField.getText().length() == 0) {
            errorMessage += "Must Enter Max\n";
        } else if (!MainApp.isIntegerString(maxTextField.getText())) {
            errorMessage += "Max Must Be An Integer (e.g 15)\n";
        }

        if(minTextField.getText() == null || minTextField.getText().length() == 0) {
            errorMessage += "Must Enter Min\n";
        } else if(!MainApp.isIntegerString(minTextField.getText())){
            errorMessage += "Min Must Be An Integer (e.g 15)\n";
        } else if (Integer.parseInt(minTextField.getText()) > Integer.parseInt(maxTextField.getText())){
            errorMessage += "Min Must Be An Integer Smaller Than Max\n";
        }

        if(lastTextField.getText() == null || lastTextField.getText().length() == 0) {
            if(isInHouse()){
                errorMessage += "Must Enter Machine ID\n";
            } else if (isOutsource()) {
                errorMessage += "Must Enter Company Name\n";
            }
        } else if(isInHouse() && !MainApp.isIntegerString(lastTextField.getText())) {
            errorMessage += "Machine ID Must Be An Integer (e.g. 15)\n";
        }

        //check errorMessage to see if empty
        if(errorMessage.equals("")) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText(errorMessage);

            alert.showAndWait();
        }

        return false;
    }



    private boolean isInHouse(){
        return inHouseRadioButton.isSelected();
    }

    private boolean isOutsource(){
        return outSourcedRadioButton.isSelected();
    }


    @FXML
    private void onCancelClicked() {
        stage.close();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPartToModify(Part partToModify){
        if(partToModify != null){
            this.partToModify = partToModify;

            idTextField.setText(Integer.toString(this.partToModify.getId()));
            nameTextField.setText(this.partToModify.getName());
            inventoryTextField.setText(Integer.toString(this.partToModify.getStock()));
            //formatted to money
            priceTextField.setText(MainApp.formatToMoney(this.partToModify.getPrice()));
            maxTextField.setText(Integer.toString(this.partToModify.getMax()));
            minTextField.setText(Integer.toString(this.partToModify.getMin()));

            if(this.partToModify instanceof InHouse){
                inHouseRadioButton.setSelected(true);
                modifiedPart = new InHouse(0, null, 0.0,0,0,0,0);
                lastLabel.setText("Machine ID");
                lastTextField.setText(Integer.toString(((InHouse) this.partToModify).getMachineId()));
            } else if(this.partToModify instanceof Outsourced){
                outSourcedRadioButton.setSelected(true);
                modifiedPart = new Outsourced(0, null, 0.0, 0, 0, 0, null);
                lastLabel.setText("Company Name");
                lastTextField.setText( ((Outsourced) this.partToModify).getCompanyName() );
            }
        }
    }


    public void setPartTableView(TableView<Part> partTableView){
        this.partTableView = partTableView;
    }

    private void updatePartTableView() {
        // Replace old part (partToModify) with new one (modifiedPart)
        int indexOfPartToModify = mainApp.getInventory().getAllParts().indexOf(this.partToModify);
        partTableView.getItems().set(indexOfPartToModify, modifiedPart);
    }
}
