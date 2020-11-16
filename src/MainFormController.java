import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import classes.Inventory;
import classes.Part;
import classes.Product;

import java.io.IOException;

/**
 * Controls the MainForm.fxml scene.
 * @author Brian Oliver
 */
public class MainFormController{

    // Variables ------------------------------------------------------------------------------------------------------

    private static Part part;
    private static Product product;
    private FilteredList<Part> filteredPartList = new FilteredList<>(Inventory.getAllParts(), s -> true);
    private FilteredList<Product> filteredProductList = new FilteredList<>(Inventory.getAllProducts(), s -> true);


    // FXML Annotations -----------------------------------------------------------------------------------------------

        // TextFields
    @FXML
    private TextField partSearchText;

    @FXML
    private TextField productSearchText;

        // Labels
    @FXML
    private Label productSearchErrorLabel;

    @FXML
    private Label partSearchErrorLabel;

    @FXML
    private Label productDeleteLabel;

    @FXML
    private Label partDeleteLabel;

        // Buttons
    @FXML
    private Button partsAddButton;

    @FXML
    private Button exitButton;

        // Tables
    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<Part, Integer> partIdColumn;

    @FXML
    private TableColumn<Part, String> partNameColumn;

    @FXML
    private TableColumn<Part, Integer> partStockColumn;

    @FXML
    private TableColumn<Part, Double> partPriceColumn;

     @FXML
    private TableView<Product> productsTableView;

    @FXML
    private TableColumn<Product, Integer> productIdColumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, Integer> productStockColumn;

    @FXML
    private TableColumn<Product, Double> productPriceColumn;


    // Methods --------------------------------------------------------------------------------------------------------

    /**
     * Populates the partsTableView and productTableView tables and sets the event listeners to assign the class
     * part and product variables on table row selections.
     *
     * Logical/Runtime Errors: I initially received thread errors when attempting to initiate the listener and it seems
     * to have been caused by me using a comparison operator on an integer instead of string in the predicate lambda function.
     * Since this way of implementing the listener with Platform.runLater() worked, it is implemented this way in each controller.
     *
     *  Compatible Features on Update: The search field listener can be updated to be initiated outside of the
     * Platform.runLater method.
     */
    public void initialize(){

        // Populates the part table
        partIdColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        partStockColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partsTableView.setItems(filteredPartList);

        productIdColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        productStockColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        productsTableView.setItems(filteredProductList);

        // Tableview event listeners
        partsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null){
                setPart(newSelection);
            }
        });

        productsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->{
            if(newSelection != null){
                setProduct(newSelection);
            }
        });

        Platform.runLater(()->{
            // Listener for parts search field.
            partSearchText.textProperty().addListener((obs) -> {
                String input = partSearchText.getText();

                // Restores all parts if the search field is blank.
                if(input == null || input.length() == 0) {
                    filteredPartList.setPredicate(s -> true);
                    partSearchErrorLabel.setVisible(false);
                    return;
                }

                filteredPartList.setPredicate(s -> {
                    // Research further to see if this can be simplified. Other implementations caused Exceptions to be
                    // thrown.
                    if(s.getName().toLowerCase().contains(input.toLowerCase()) || input.contains(Integer.toString(s.getId()))){
                        partSearchErrorLabel.setVisible(false);
                        return true;
                    } else return false;
                });

                if(filteredPartList.size() == 0){
                    partSearchErrorLabel.setVisible(true);
                }
            });

            productSearchText.textProperty().addListener((obs) -> {
                String input = productSearchText.getText();

                // Restores all parts if the search field is blank.
                if(input == null || input.length() == 0) {
                    filteredProductList.setPredicate(s -> true);
                    productSearchErrorLabel.setVisible(false);
                    return;
                }

                filteredProductList.setPredicate(s -> {
                    // Research further to see if this can be simplified. Other implementations caused Exceptions to be
                    // thrown.
                    if(s.getName().toLowerCase().contains(input.toLowerCase()) || input.contains(Integer.toString(s.getId()))){
                        productSearchErrorLabel.setVisible(false);
                        return true;
                    } else return false;
                });
                if(filteredProductList.size() == 0){
                    productSearchErrorLabel.setVisible(true);
                }
            });
        });

    }


    /**
     * Returns the current part selected in the Parts tableview. This function is needed to pass the selected part to the
     * ModForm.fxml scene.
     *
     * Logical/Runtime Errors: None
     * Compatible Features on Updates: None
     *
     * @return      The selected part on the Parts tableview.
     */
    public static Part getPart(){
        return part;
    }


    /**
     * Returns the current product selected in the Products tableview. This function is needed to pass the selected product to the
     * ModProductForm.fxml scene.
     *
     * Logical/Runtime Errors: None
     * Compatible Features on Updates: None
     *
     * @return      The selected product on the Products tableview.
     */
    public static Product getProduct(){ return product; }


    /**
     * Assigns the static part variable to the part input
     *
     * Logical/Runtime Errors: None
     * Compatible Features on Update: None
     *
     * @param   newPart     The part to set
     */
    public static void setPart(Part newPart){
        part = newPart;
    }


    /**
     * Assigns the static product variable to the product input
     *
     * Logical/Runtime Errors: None
     * Compatible Features on Update: None
     *
     * @param   newProduct     The product to set
     */
    public static void setProduct(Product newProduct){
        product = newProduct;
    }


    /**
     * Closes the program when the exit button is clicked.
     *
     * Logical/Runtime Error: None
     * Compatible Features on Update: None
     */
    public void exitButtonListener(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }


    /**
     * Switches the scene to the AddForm.fxml scene when the partsAddButton is clicked.
     *
     * Logical/Runtime Errors: None
     *
     * Compatible Features on Update: The method name can be updated in future iterations to reflect the standard
     * set[button name]Button naming standard.
     *
     * @throws IOException if AddForm.fxml can not be loaded.
     *
     */
    public void partsAddButtonListener() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("AddForm.fxml"));

        Scene scene = new Scene(parent);

        Stage stage = (Stage) partsAddButton.getScene().getWindow();

        stage.hide();
        stage.setTitle("Inventory Manager - Add Part");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Opens the AddProductForm.fxml scene to create a new Product
     *
     * Logical/Runtime Errors: None
     *
     * Compatible Features on Update: The method name can be updated in future iterations to reflect the standard
     * set[button name]Button naming standard.
     *
     * @throws IOException if the scene is unable to be loaded.
     *
     */
    public void productAddButtonListener() throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("AddProductForm.fxml"));

        Scene scene = new Scene(parent);

        Stage stage = (Stage) partsAddButton.getScene().getWindow();

        stage.hide();
        stage.setTitle("Inventory Manager - Add Product");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Switches the scene to the ModForm.fxml scene when the partsModifyButton is clicked and the MainControllerForm
     * part variable is not null. If the part variable is null, the UI displays an error message.
     *
     * Logical/Runtime Error: None
     *
     * Compatible Feature Updates: The method name can be updated in future iterations to reflect the standard
     * set[button name]Button naming standard.
     *
     * @throws  IOException if ModForm.fxml can not be loaded'
     */
    public void partsModifyButtonListener() throws IOException {

        // Stops the method if part is not assigned.
        if(part == null){
            partDeleteLabel.setTextFill(Color.web("#e12020"));
            partDeleteLabel.setText("Select a part to be modified.");
            return;
        }

        Parent parent = FXMLLoader.load(getClass().getResource("ModForm.fxml"));

        Scene scene = new Scene(parent);

        Stage stage = (Stage) partsAddButton.getScene().getWindow();

        stage.hide();
        stage.setTitle("Inventory Manager - Modify Part");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Switches the scene to the ModForm.fxml scene when the productsModifyButton is clicked and the MainControllerForm
     * product variable is not null. If the product variable is null, the UI displays an error message.
     *
     * Logical/Runtime Errors: None
     *
     * Compatible Features on Update: The method name can be updated in future iterations to reflect the standard
     * set[button name]Button naming standard.
     *
     * @throws  IOException if the scene is unable to be loaded.
     */
    public void productsModifyButtonListener() throws IOException{
        // Stops the method if product is not assigned.
        if(product == null){
            productDeleteLabel.setTextFill(Color.web("#e12020"));
            productDeleteLabel.setText("Select a product to be modified.");
            return;
        }

        Parent parent = FXMLLoader.load(getClass().getResource("ModProductForm.fxml"));

        Scene scene = new Scene(parent);

        Stage stage = (Stage) partsAddButton.getScene().getWindow();

        stage.hide();
        stage.setTitle("Inventory Manager - Modify Product");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Deletes the currently selected part in the partsTableView from Inventory.allParts. If the part variable is null,
     * the UI displays an error message.
     *
     * Logical/Runtime Errors: None
     *
     * Compatible Features on Update: The method name can be updated in future iterations to reflect the standard
     * set[button name]Button naming standard.
     */
    public void partsDeleteButtonListener(){
        Part part = partsTableView.getSelectionModel().getSelectedItem();
        if(part == null) {
            partDeleteLabel.setTextFill(Color.web("#e12020"));
            partDeleteLabel.setText("Select a part to be deleted.");
            return;
        }

        String name = part.getName();
        if(Inventory.deletePart(part)){
            partDeleteLabel.setTextFill(Color.web("#0f5700"));
            partDeleteLabel.setText(name + " was deleted.");
            setPart(null);
        }
    }


    /**
     * Deletes the currently selected product in the partsTableView from Inventory.allProducts. If the product variable is null,
     * the UI displays an error message.
     *
     *  Logical/Runtime Errors: None
     *
     *  Compatible Features on Update: The method name can be updated in future iterations to reflect the standard
     *  set[button name]Button naming standard.
     */
    public void productsDeleteButtonListener(){
        if(product == null) {
            productDeleteLabel.setTextFill(Color.web("#e12020"));
            productDeleteLabel.setText("Select a product to be deleted.");
            return;
        }
        String name = product.getName();
        if(product.getAllAssociatedParts().size() > 0){
            productDeleteLabel.setTextFill(Color.web("#e12020"));
            productDeleteLabel.setText(name + " was not deleted. Remove all associated parts first.");
            return;
        }
        if(Inventory.deleteProduct(product)){
            productDeleteLabel.setTextFill(Color.web("#0f5700"));
            productDeleteLabel.setText(name + " was deleted.");
            setProduct(null);
        }
    }

}