package classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds object Part to ObservableList<Part> allParts.
     *
     * Runtime/Logical Error: None
     * Compatible Features for Updates: The method can be expanded to allow an array of parts to be passed to allow
     * for bulk additions.
     *
     * @param   newPart     Part to be added o allParts
     *
     */
    public static void addPart(Part newPart){
        allParts.addAll(newPart);
    }

    /**
     * Adds object Product to ObservableList<Product> allProducts
     *
     * Runtime/Logical Error: None
     * Compatible Features for Updates: The method can be expanded to allow an array of parts to be passed to allow
     * for bulk additions.
     *
     * @param   newProduct  Product to be added to allProducts
     */
    public static void addProduct(Product newProduct){
        allProducts.addAll(newProduct);
    }

    /**
     * Returns a Part object based on the partId
     *
     * Logical/Runtime Errors: None
     * Compatible Features for Updates: The method can be expanded to accept an array of part IDs as an input and return
     * an array of parts.
     *
     * @param   partId  The ID of the part that is being searched.
     * @return          Part that was found
     */
    public static Part lookupPart(int partId){
        for(Part part : allParts){
            if(part.getId() == partId){
                return part;
            }
        }
        return null;
    }

    /**
     * Returns a Product object in allProduct
     *
     * Logical/Runtime Errors: None
     * Compatible Features for Updates: The method can be expanded to accept an array of product IDs as an input and return
     * an array of products.
     *
     * @param   productId   ID of the product being searched.
     * @return              Product object that was found
     */
    public static Product lookupProduct(int productId){
        for(Product product : allProducts){
            if(product.getId() == productId){
                return product;
            }
        }
        return null;
    }

    /**
     * Returns a Part found based on the Part name
     *
     * Logical/Runtime Errors: None
     * Compatible Features for Updates: The method can be expanded to accept an array of part names as an input and return
     * an array of parts.
     *
     * @param   partName    The name of the part to be searched.
     * @return              Part that was found
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> partsToReturn = FXCollections.observableArrayList();
        for(Part part : allParts){
            if(part.getName().toLowerCase() == partName.toLowerCase()){
                partsToReturn.add(part);
            }
        }
        return partsToReturn;
    }

    /**
     * Returns a Part found based on the Product's name.
     *
     * Logical/Runtime Errors: None
     * Compatible Features for Updates: The method can be expanded to accept an array of product names as an input and return
     * an array of products.
     *
     * @param   productName Name of the product to be searched
     * @return              Product that was found
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> productsToReturn = FXCollections.observableArrayList();
        for(Product product : allProducts){
            if(product.getName().toLowerCase() == productName.toLowerCase()){
                productsToReturn.add(product);
            }
        }
        return  productsToReturn;
    }

    /**
     * Replaces/Updates the part provided by the input index.
     *
     * Logical/Runtime Errors: When using this method, I was passing the part's ID as the index instead of the Parts ID - 1.
     * This caused the program to attempt to update the wrong/non-existent part.
     * Compatible Features for Updates: The method can be expanding by overloading it to accept a Part input and searching the index
     * instead of having to include the index as a parameter.
     *
     * @param   index           The index of the part to be updated/replaced.
     * @param   selectedPart    The part to update/replace at the given index.
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    /**
     * Replaces the Product at the index with the input Product
     *
     * Logical/Runtime Errors: When using this method, I was passing the product's ID as the index instead of the product's ID - 1.
     * This caused the program to attempt to update the wrong/non-existent product.
     * Compatible Features for Updates: The method can be expanding by overloading it to accept a product input and searching the index
     * instead of having to include the index as a parameter.
     *
     * @param   index       The index of the product to be updated/replaced.
     * @param   newProduct  The product to update/replace at the given index.
     */
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    /**
     * Finds and deletes object Part from ObservableList<Part>
     *
     * Logical/Runtime Errors: None
     * Compatible Features for Updates: This method can be expanded by allowing an array of parts as a parameter for deletion.
     *
     * @param   selectedPart    The part to remove
     * @return                  true if the part was found and deleted. false if the part was not found.
     */
    public static boolean deletePart(Part selectedPart){
        if(allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes Product object from allProducts
     *
     * Logical/Runtime Errors: None
     * Compatible Features for Updates: This method can be expanded by allowing an array of products as a parameter for deletion.
     *
     * @param   selectedProduct The Product to remove
     * @return                  true if product was deleted, false if the Product could not be found.
     */
    public static boolean deleteProduct(Product selectedProduct){
        if(allProducts.contains(selectedProduct)){
            allProducts.remove(selectedProduct);
            return true;
        }
        return false;
    }

    /**
     * Returns a copy of the ObservableList<Part> allParts
     *
     * Logical/Runtime Errors: None
     * Compatible Features for Updates: None
     *
     * @return  ObservableList<Part>    The ObservableList<Part> with all of the parts information.
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * Returns the allProducts ObservableList
     *
     * Logical/Runtime Errors: None
     * Compatible Features for Updates: None
     *
     * @return  ObservableList<Product> The ObservableList<Part> with all of the parts information.
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}
