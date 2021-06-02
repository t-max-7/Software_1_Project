package tmax7.inventory.management.system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Inventory {
    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public void addPart(Part newPart){
        allParts.add(newPart);
    }

    public void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    public Part lookupPart(int partId){
        for(Part part: allParts){
           if(part.getId() == partId){
               return part;
           }
        }
        return null;
    }

    public Product lookupProduct(int productId){
        for(Product product: allProducts){
            if(product.getId() == productId){
                return product;
            }
        }
        return null;
    }

    public ObservableList<Part> lookupPart(String partName){
        for(Part part: allParts){
            if(part.getName().equals(partName)){
                return FXCollections.observableArrayList(part);
            }
        }
        return null;
    }

    public ObservableList<Product> lookupProduct(String productName) {
        for(Product product: allProducts){
            if(product.getName().equals(productName)){
                return FXCollections.observableArrayList(product);
                           }
        }
        return null;
    }

    public void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    public void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    public boolean deletePart(Part selectedPart){
        return allParts.remove(selectedPart);
    }

    public boolean deleteProduct(Product selectedProduct){
        return allProducts.remove(selectedProduct);
    }

    public ObservableList<Part> getAllParts(){
        return allParts;
    }

    public ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}
