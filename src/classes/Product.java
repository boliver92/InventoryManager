package classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {

    // Class Variables
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    // Methods
    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Sets object id
     *
     * Logical/Runtime Error: None
     * Compatible Features for Updates: None
     *
     * @param   id  The ID of the product
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets object name
     *
     * Logical/Runtime Error: None
     * Compatible Features for Updates: None
     *
     * @param   name    The name of the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets object price
     *
     * Logical/Runtime Error: None
     * Compatible Features for Updates: None
     *
     * @param   price   The price of the product
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets object stock
     *
     * Logical/Runtime Error: None
     * Compatible Features for Updates: None
     *
     * @param   stock   The stock/inventory level of the product.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Sets object min stock
     *
     * Logical/Runtime Error: None
     * Compatible Features for Updates: None
     *
     * @param   min The minimum amount of stock required.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Sets objects max stock
     *
     * Logical/Runtime Error: None
     * Compatible Features for Updates: None
     *
     * @param   max     Maximum stock allowed.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Gets the product's id
     *
     * Logical/Runtime Error: None
     * Compatible Features for Updates: None
     *
     * @return      The ID of the product
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the objects name
     *
     * Logical/Runtime Error: None
     * Compatible Features for Updates: None
     *
     * @return  The product's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the objects price
     *
     * Logical/Runtime Error: None
     * Compatible Features for Updates: None
     *
     * @return  The price of the product
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the objects stock
     *
     * Logical/Runtime Error: None
     * Compatible Features for Updates: None
     *
     * @return      The stock/inventory level of the product.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Returns the objects min stock
     *
     * Logical/Runtime Error: None
     * Compatible Features for Updates: None
     *
     * @return  The minimum allowed stock of the product
     */
    public int getMin() {
        return min;
    }

    /**
     * Returns the objects max stock
     *
     * Logical/Runtime Error: None
     * Compatible Features for Updates: None
     *
     * @return  The maximum allowed stock of the product.
     */
    public int getMax() {
        return max;
    }

    /**
     * Adds a Part to the ObservableList<Part> associatedParts
     *
     * Logical/Runtime Error: None
     * Compatible Features for Updates: The method can be updated in the future to accept an array of parts to create a
     * bulk add to the associatedParts list.
     *
     * @param   part    The part to be added to the product's associated parts.
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * Removes Part from ObservableList<Part> associatedParts
     *
     * Logical/Runtime Error: None
     * Compatible Features for Updates: The method can be updated in the future to accept an array of parts to process a
     * bulk removal from the associatedParts list.
     *
     * @param   selectedAssociatedPart  The Part to remove from the product's associated parts.
     * @return                          true if part was found and removed, false if not
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        if(associatedParts.contains(selectedAssociatedPart)){
            associatedParts.remove(selectedAssociatedPart);
            return true;
        }
        return false;
    }

    /**
     * Returns the ObservableList<Part> associatedParts;
     *
     * Logical/Runtime Error: None
     * Compatible Features for Updates: None
     *
     * @return      An ObservableList object with the product's associated parts.
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}
