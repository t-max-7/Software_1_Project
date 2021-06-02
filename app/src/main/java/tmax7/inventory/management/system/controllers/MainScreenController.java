package tmax7.inventory.management.system.controllers;

import tmax7.inventory.management.system.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class MainScreenController {
    private MainApp mainApp;

    @FXML
    private TextField partSearchTextField;
    @FXML
    private TextField productSearchTextField;

    @FXML
    private TableView<Part> partTableView;
    @FXML
    private TableColumn<Part, String> partIdColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, String> partInventoryLevelColumn;
    @FXML
    private TableColumn<Part, String>  partPricePerUnitColumn;

    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableColumn<Product, String> productIdColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, String> productInventoryLevelColumn;
    @FXML
    private TableColumn<Product, String> productPricePerUnitColumn;

    @FXML
    private void initialize(){
        //part columns cellValue Factories
        partIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> cellDataFeatures) {
                return new ReadOnlyObjectWrapper(cellDataFeatures.getValue().getId());
            }
        });

        partNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> cellDataFeatures) {
                return new ReadOnlyObjectWrapper(cellDataFeatures.getValue().getName());
            }
        });

        partInventoryLevelColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> cellDataFeatures) {
                return new ReadOnlyObjectWrapper(cellDataFeatures.getValue().getStock());
            }
        });
        //formatted to money
        partPricePerUnitColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> cellDataFeatures) {
                return new ReadOnlyObjectWrapper(MainApp.formatToMoney(cellDataFeatures.getValue().getPrice())) ;
            }
        });

        /*product columns cellValue Factories*/
        productIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Product, String> cellDataFeatures) {
                return new ReadOnlyObjectWrapper(cellDataFeatures.getValue().getId());
            }
        });

        productNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Product, String> cellDataFeatures) {
                return new ReadOnlyObjectWrapper(cellDataFeatures.getValue().getName());
            }
        });

        productInventoryLevelColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Product, String> cellDataFeatures) {
                return new ReadOnlyObjectWrapper(cellDataFeatures.getValue().getStock());
            }
        });
        //formatted to money
        productPricePerUnitColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Product, String> cellDataFeatures) {
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
                partTableView.getSelectionModel().select(part);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("");
                alert.setContentText("Part not Found");

                alert.showAndWait();
            }

        }
    }

    @FXML
    private void onAddPartClicked(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainScreenController.class.getResource("/AddPart.fxml"));
            AnchorPane anchorPane = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Part");
            stage.initOwner(mainApp.getPrimaryStage());
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);

            AddPartController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setStage(stage);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void onModifyPartClicked() {
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
        if(selectedPart != null){
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainScreenController.class.getResource("/ModifyPart.fxml"));
                AnchorPane anchorPane = (AnchorPane) loader.load();

                Stage stage = new Stage();
                stage.setTitle("Modify Part");
                stage.initOwner(mainApp.getPrimaryStage());
                stage.initModality(Modality.WINDOW_MODAL);
                Scene scene = new Scene(anchorPane);
                stage.setScene(scene);

                ModifyPartController controller = loader.getController();
                controller.setMainApp(mainApp);
                controller.setStage(stage);
                controller.setPartToModify(selectedPart);
                controller.setPartTableView(partTableView);

                stage.showAndWait();

            } catch(IOException e) {
                e.printStackTrace();
            }
        }

    }
    @FXML
    private void onDeletePartClicked(){
        int selectedIndex = partTableView.getSelectionModel().getSelectedIndex();
        if(selectedIndex > -1){
            partTableView.getItems().remove(selectedIndex);
        }
    }

    @FXML
    private void onSearchProductClicked(){
        String inputAsString = productSearchTextField.getText();
        if(inputAsString != null && inputAsString.length() > 0){
            Product product = null;
            try {
                int inputAsInt = Integer.parseInt(inputAsString);
                product = mainApp.getInventory().lookupProduct(inputAsInt);

            } catch (NumberFormatException e) {
                ObservableList<Product> productList = mainApp.getInventory().lookupProduct(inputAsString);
                if(productList != null){
                    product = productList.get(0);
                }
            }
            if(product != null) {
                productTableView.getSelectionModel().select(product);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("");
                alert.setContentText("Product not Found");

                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onAddProductClicked(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainScreenController.class.getResource("/AddProduct.fxml"));
            AnchorPane anchorPane = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Product");
            stage.initOwner(mainApp.getPrimaryStage());
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);

            AddProductController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setStage(stage);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onModifyProductClicked(){
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if(selectedProduct != null) {
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainScreenController.class.getResource("/ModifyProduct.fxml"));
                AnchorPane anchorPane = (AnchorPane) loader.load();

                Stage stage = new Stage();
                stage.setTitle("Modify Product");
                stage.initOwner(mainApp.getPrimaryStage());
                stage.initModality(Modality.WINDOW_MODAL);
                Scene scene = new Scene(anchorPane);
                stage.setScene(scene);

                ModifyProductController controller = loader.getController();
                controller.setMainApp(mainApp);
                controller.setStage(stage);
                controller.setProductToModify(selectedProduct);
                controller.setProductTableView(productTableView);

                stage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onDeleteProductClicked(){
        int selectedIndex = productTableView.getSelectionModel().getSelectedIndex();
        if(selectedIndex > -1) {
            productTableView.getItems().remove(selectedIndex);
        }
    }

    @FXML
    private void onExitClicked(){
        System.exit(0);
    }


    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
        partTableView.setItems(this.mainApp.getInventory().getAllParts());
        productTableView.setItems(this.mainApp.getInventory().getAllProducts());
    }

}
