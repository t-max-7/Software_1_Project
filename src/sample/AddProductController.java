package sample;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.concurrent.atomic.AtomicBoolean;

public class AddProductController {
    private MainApp mainApp;
    private ObservableList<Part> availableParts;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private Stage stage;

    private AtomicBoolean dollarSignUsed = new AtomicBoolean(false);

    @FXML
    private TextField partSearchTextField;

    @FXML
    private TableView<Part> availablePartsTableView;
    @FXML
    private TableColumn<Part, String> availablePartIdColumn;
    @FXML
    private TableColumn<Part, String> availablePartNameColumn;
    @FXML
    private TableColumn<Part, String> availablePartInventoryLevelColumn;
    @FXML
    private TableColumn<Part, String> availablePartPricePerUnitColumn;

    @FXML
    private TableView<Part> associatedPartsTableView;
    @FXML
    private TableColumn<Part, String> associatedPartIdColumn;
    @FXML
    private TableColumn<Part, String> associatedPartNameColumn;
    @FXML
    private TableColumn<Part, String> associatedPartInventoryLevelColumn;
    @FXML
    private TableColumn<Part, String> associatedPartPricePerUnitColumn;

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
    private void initialize(){

        //make idTextField uneditable because id is auto generated
        idTextField.setEditable(false);
        //set selectedPartsTableView with the parts chosen from available parts table
        associatedPartsTableView.setItems(associatedParts);

        //part columns cellValue Factories
        availablePartIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> cellDataFeatures) {
                return new ReadOnlyObjectWrapper(cellDataFeatures.getValue().getId());
            }
        });

        //set table column cell value factories for both table views
        availablePartNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> cellDataFeatures) {
                return new ReadOnlyObjectWrapper(cellDataFeatures.getValue().getName());
            }
        });

        availablePartInventoryLevelColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> cellDataFeatures) {
                return new ReadOnlyObjectWrapper(cellDataFeatures.getValue().getStock());
            }
        });
        //formatted to money
        availablePartPricePerUnitColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> cellDataFeatures) {
                return new ReadOnlyObjectWrapper(MainApp.formatToMoney(cellDataFeatures.getValue().getPrice()));
            }
        });

        /*product columns cellValue Factories*/
        associatedPartIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> cellDataFeatures) {
                return new ReadOnlyObjectWrapper(cellDataFeatures.getValue().getId());
            }
        });

        associatedPartNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> cellDataFeatures) {
                return new ReadOnlyObjectWrapper(cellDataFeatures.getValue().getName());
            }
        });

        associatedPartInventoryLevelColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> cellDataFeatures) {
                return new ReadOnlyObjectWrapper(cellDataFeatures.getValue().getStock());
            }
        });
        //formatted to money
        associatedPartPricePerUnitColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> cellDataFeatures) {
                return new ReadOnlyObjectWrapper(MainApp.formatToMoney(cellDataFeatures.getValue().getPrice()));
            }
        });
    }


    @FXML
    private void onSearchPartClicked() {
        String inputAsString = partSearchTextField.getText();
        if(inputAsString != null && inputAsString.length() > 0) {
            Part part = null;

            try {
                int inputAsInt = Integer.parseInt(inputAsString);
                part = mainApp.getInventory().lookupPart(inputAsInt);
            } catch (NumberFormatException e) {
                ObservableList<Part> partList = mainApp.getInventory().lookupPart(inputAsString);
                if(partList != null){
                    part = partList.get(0);
                }
            }

            if(part != null) {
                availablePartsTableView.getSelectionModel().select(part);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("");
                alert.setContentText("Part Not Found");

                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onAddPartClicked() {
        Part partToAdd = availablePartsTableView.getSelectionModel().getSelectedItem();
        if(partToAdd != null){
            Part partToAddsClone = null;

            int id = partToAdd.getId();
            String name = partToAdd.getName();
            double price = partToAdd.getPrice();
            int stock = partToAdd.getStock();
            int min = partToAdd.getMin();
            int max = partToAdd.getMax();

            if(partToAdd instanceof InHouse){
                int machineId = ((InHouse) partToAdd).getMachineId();
                partToAddsClone = new InHouse(id, name, price, stock, min, max, machineId);
            } else if(partToAdd instanceof Outsourced) {
                String companyName = ((Outsourced) partToAdd).getCompanyName();
                partToAddsClone = new Outsourced(id, name, price, stock, min, max, companyName);
            }

            if(partToAddsClone != null) {
                associatedParts.add(partToAddsClone);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Something Went Wrong");
                alert.setContentText("The part could not be added.");

                alert.showAndWait();
            }
        }
    }


    @FXML
    private void onDeletePartClicked() {
        Part partToDelete = associatedPartsTableView.getSelectionModel().getSelectedItem();
        if(partToDelete != null) {
            associatedPartsTableView.getItems().remove(partToDelete);
        }
    }

    @FXML
    private void onSaveClicked(){

        if(isInputValid() && isPriceValid()){
            int id = ++MainApp.numberOfProducts;
            String name = nameTextField.getText();


            double price;
            if(dollarSignUsed.get()){
                price = Double.parseDouble(priceTextField.getText().substring(MainApp.indexAfterDollarSign));
            } else {
                price = Double.parseDouble(priceTextField.getText());
            }

            int stock = Integer.parseInt(inventoryTextField.getText());
            int max = Integer.parseInt(maxTextField.getText());
            int min = Integer.parseInt(minTextField.getText());

            Product product = new Product(id, name, price, stock, min, max);
            product.getAllAssociatedPart().addAll(associatedParts);

            mainApp.getInventory().addProduct(product);

            stage.close();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameTextField.getText() == null || nameTextField.getText().length() == 0) {
            errorMessage += "Must Enter Name\n";
        }

        if (inventoryTextField.getText() == null || inventoryTextField.getText().length() == 0) {
            errorMessage += "Must Enter Inventory\n";
        } else if (!MainApp.isIntegerString(inventoryTextField.getText())) {
            errorMessage += "Inventory  Must Be An Integer (e.g. 15)\n";
        } else if (
                !(
                        Integer.parseInt(inventoryTextField.getText()) <= Integer.parseInt(maxTextField.getText())
                                && Integer.parseInt(inventoryTextField.getText()) >= Integer.parseInt(minTextField.getText())
                )
        ) {
            errorMessage += "Inventory Must Be A Value Equal To Or Between Max And Min (i.e. Max >= Inventory >= Min)\n";
        }

        if (priceTextField.getText() == null || priceTextField.getText().length() == 0) {
            errorMessage += "Must Enter Price\n";
        } else if (!MainApp.isDoubleString(priceTextField.getText(), dollarSignUsed)) {
            errorMessage += "Price Must Be In Decimal Form (e.g. 140.56, 50.00)\n";
        }

        if (maxTextField.getText() == null || maxTextField.getText().length() == 0) {
            errorMessage += "Must Enter Max\n";
        } else if (!MainApp.isIntegerString(maxTextField.getText())) {
            errorMessage += "Max Must Be An Integer (e.g 15)\n";
        }

        if (minTextField.getText() == null || minTextField.getText().length() == 0) {
            errorMessage += "Must Enter Min\n";
        } else if (!MainApp.isIntegerString(minTextField.getText())) {
            errorMessage += "Min Must Be An Integer (e.g 15)\n";
        } else if (Integer.parseInt(minTextField.getText()) > Integer.parseInt(maxTextField.getText())) {
            errorMessage += "Min Must Be An Integer Smaller Than Max\n";
        }

        if(associatedParts.size() < 1){
            errorMessage+= "At Least One Part Must Be Added\n";
        }

        //see if error message is empty
        if (errorMessage.equals("")) {
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

    //checks if price of product is >= sum of the price of all the parts added together.
    private boolean isPriceValid(){
        double priceOfProduct = 0;
        if(dollarSignUsed.get()){
            priceOfProduct = Double.parseDouble(priceTextField.getText().substring(MainApp.indexAfterDollarSign));
        } else {
            priceOfProduct = Double.parseDouble(priceTextField.getText());
        }

        double priceOfParts = 0;
        for(Part part : associatedParts){
            priceOfParts += part.getPrice();
        }

        if(priceOfProduct >= priceOfParts){
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText("Price cannot be less than the sum of the prices of all the parts");

            alert.showAndWait();
            return false;
        }
    }

    @FXML
    private void onCancelClicked() {
        stage.close();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        availableParts = this.mainApp.getInventory().getAllParts();
        availablePartsTableView.setItems(availableParts);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
