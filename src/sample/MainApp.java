package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainApp extends Application {
    private Stage primaryStage;
    private Inventory inventory = new Inventory();
    static int numberOfParts = 0;
    static int numberOfProducts = 0;

    public static final int indexAfterDollarSign = 1;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("MainScreen.fxml"));
        Parent root = loader.load();

        MainScreenController controller = loader.getController();
        controller.setMainApp(this);

        primaryStage.setTitle("Inventory Management System App");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public static String formatToMoney(double d){
        boolean isToTenthsPlace = false;

        switch ( String.format("%.2f", d - Math.floor(d)) ) {
            case "0.00":
            case "0.10":
            case "0.20":
            case "0.30":
            case "0.40":
            case "0.50":
            case "0.60":
            case "0.70":
            case "0.80":
            case "0.90":
                isToTenthsPlace = true;
                break;
        }
        if(isToTenthsPlace){
            return "$" + d + "0";
        }
        return "$" + Double.toString(d);
    }

    public static  boolean isIntegerString(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDoubleString(String string, AtomicBoolean dollarSignUsed) {
        try {
            //returns true if double with no $ in front
            Double.parseDouble(string);
            dollarSignUsed.set(false);
            return true;
        } catch (NumberFormatException e) {
            try{
                //returns true if double with $ in front
                Double.parseDouble(string.substring(indexAfterDollarSign));
                dollarSignUsed.set(true);
                return true;
            } catch(NumberFormatException e2){
                return false;
            }
        }
    }

}