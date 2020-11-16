import classes.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * Controls the AddProductForm.fxml scene GUI.
 * @author Brian Oliver
 */
public class AddProductFormController {

    // Variables ------------------------------------------------------------------------------------------------------

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private FilteredList<Part> filteredPartList = new FilteredList<>(Inventory.getAllParts(), s -> true);
    public static int count = 1;


    //FXML Annotations ------------------------------------------------------------------------------------------------

        // Text Fields
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField stockTextField;

    @FXML
    private TextField minTextField;

    @FXML
    private TextField maxTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField searchTextField;

        // Buttons

    @FXML
    private Button cancelButton;


    // Labels
    @FXML
    private Label errorLabel;

    @FXML
    private Label inputErrorLabel;

        // TableViews

            //Parts Table
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

            //Associated parts Table

    @FXML
    private TableView<Part> assocPartsTableView;

    @FXML
    private TableColumn<Part, Integer> assocPartIdColumn;

    @FXML
    private TableColumn<Part, String> assocPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> assocPartStockColumn;

    @FXML
    private TableColumn<Part, Double> assocPartPriceColumn;


    // Input Validation -----------------------------------------------------------------------------------------------

    Pattern validDoubleState = Pattern.compile("(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

    private final UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String input = change.getControlNewText();
        if(input.matches("([0-9]*)?")) {
            inputErrorLabel.setText("");
            return change;
        }
        inputErrorLabel.setText("This field only accepts numbers");
        return null;
    };

    private final UnaryOperator<TextFormatter.Change> doubleFilter = change -> {
        String input = change.getControlNewText();
        if(validDoubleState.matcher(input).matches()) {
            inputErrorLabel.setText("");
            return change;
        } else {
            inputErrorLabel.setText("The input must be in price form: x.xx");
            return null;
        }
    };

    StringConverter<Double> doubleConverter = new StringConverter<Double>() {
        @Override
        public String toString(Double aDouble) {
            return aDouble.toString();
        }

        @Override
        public Double fromString(String s) {
            if(s.isEmpty() || ".".equals(s)){
                return 0.00;
            } else {
                return Double.valueOf(s);
            }
        }
    };


    // Methods --------------------------------------------------------------------------------------------------------

    /**
     * Populates the partsTableView and assocPartTableView, adds listeners to the search boxes, and
     * adds a TextFormatter to the price, stock, min, and max fields to filter the input in the text fields.
     *
     * Logical/Runtime Error:
     *
     * I attempted to create a TextValidation class to pass the text fields into so I could refrain
     * from having to reuse the Lambda functions in each controller (integerFilter, doubleFilter, doubleConverter) and to
     * update the UI labels associated with each field. However, I was unsuccessful in implementing a class that did not throw
     * errors when attempting to set the text formatter. I finally decided to include the lambda functions in each class to handle
     * the text validation and label updates.
     *
     * I initially received thread errors when attempting to initiate the listener and it seems to have been caused by me using
     * a comparison operator on an integer instead of string in the predicate lambda function. Since this way of
     * implementing the listener worked, it is implemented this way in each controller.
     *
     * Compatible Features for Updates:
     *
     * The textformatter can be updated to show a double with a precision of 2 decimal places.
     * The lambda functions in each controller may be implemented in a way where it is not listed in each controller.
     *
     * The search field listener can be updated to be initiated outside of the Platform.runLater method.
     */
    public void initialize(){
        priceTextField.setTextFormatter(new TextFormatter<Double>(doubleConverter, 0.00, doubleFilter));
        stockTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
        minTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
        maxTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));

        partIdColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        partStockColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partsTableView.setItems(filteredPartList);

        assocPartIdColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        assocPartNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        assocPartPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        assocPartStockColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        assocPartsTableView.setItems(associatedParts);

        Platform.runLater(()->{
            // Listener for parts search field.
            searchTextField.textProperty().addListener((obs) -> {
                String input = searchTextField.getText();

                // Restores all parts if the search field is blank.
                if(input == null || input.length() == 0) {
                    filteredPartList.setPredicate(s -> true);
                    return;
                }

                filteredPartList.setPredicate(s -> {
                    // Research further to see if this can be simplified. Other implementations caused Exceptions to be
                    // thrown.
                    if(s.getName().toLowerCase().contains(input.toLowerCase()) || input.contains(Integer.toString(s.getId()))){
                        return true;
                    } else return false;
                });
            });
        });
    }


    /**
     * Closes the Add Form and returns to the Main Form when the cancel button is selected.
     *
     * Logical/Runtime Errors: None
     *
     * Compatible Features on Update: This function can be expanded in future updates by better handling the IOException if thrown.
     * The method can be renamed to follow the normal naming standards of setSaveButton. However, I
     * continued the style of [button name]ButtonListener after defining a substantial amount of methods using.
     *
     * @throws  IOException   if MainForm.fxml fails to load.
     *
     */
    public void cancelButtonListener() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("MainForm.fxml"));

        Scene scene = new Scene(parent);
        Stage stage = (Stage) cancelButton.getScene().getWindow();

        stage.setTitle("Inventory Manager");
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Adds the selected Part in the partsTableView to the Product's associatedParts ObservableList
     *
     * Logical/Runtime Error: None
     *
     * Compatible Features on Update: The method can be renamed to follow the normal naming standards of setAddButton
     * (set[button name]Button). However, I continued the style of [button name]ButtonListener after defining a substantial
     * amount of methods.
     */
    public void addButtonListener(){
        Part part = partsTableView.getSelectionModel().getSelectedItem();
        if(part == null) {
            errorLabel.setTextFill(Color.web("#e12020"));
            errorLabel.setText("Select a part to be added.");
            return;
        }
        if(associatedParts.contains(part)){
            errorLabel.setTextFill(Color.web("#e12020"));
            errorLabel.setText("The part is already associated with this product.");
            return;
        }

        associatedParts.add(part);
        errorLabel.setTextFill(Color.web("#0f5700"));
        errorLabel.setText("Part added successfully");
    }


    /**
     * Removes the selected Part in the assocPartsTableView from assocPartsTableView
     *
     * Logical/Runtime Error: None
     *
     * Compatible Features on Update: The method can be renamed to follow the normal naming standards of setAddButton
     * (set[button name]Button). However, I continued the style of [button name]ButtonListener after defining a substantial
     * amount of methods.
     */
    public void RemoveAssocListener(){
        Part part = assocPartsTableView.getSelectionModel().getSelectedItem();
        if(part == null) {
            errorLabel.setTextFill(Color.web("#e12020"));
            errorLabel.setText("Select an associated product to be removed.");
            return;
        }
        associatedParts.remove(part);
        errorLabel.setTextFill(Color.web("#0f5700"));
        errorLabel.setText("Part removed successfully");
    }


    /**
     * Saves the entered Product information to Inventory.allProducts
     *
     * Logical/Runtime Error: None
     *
     * Compatible Features on Update: This function can be expanded in future updates by expanding confirming that the price's
     * double precision is set to 2. The method can be renamed to follow the normal naming standards of setSaveButton.
     * However, I continued the style of [button name]ButtonListener after defining a substantial amount of methods using.
     *
     */
    public void saveButtonListener() {
        try {

            String name = nameTextField.getText();
            int stock = Integer.parseInt(stockTextField.getText());
            int min = Integer.parseInt(minTextField.getText());
            int max = Integer.parseInt(maxTextField.getText());
            double price = Double.parseDouble(priceTextField.getText());

            if (name.isEmpty()) {
                errorLabel.setText("A part name is required.");
                return;
            }
            if (min < 0) {
                errorLabel.setText("You can not have a minimum amount less than 0");
                return;
            }
            if (max < min) {
                errorLabel.setText("The max amount is less than the minimum amount.");
                return;
            }
            if (stock > max) {
                errorLabel.setText("The entered stock is greater than the maximum amount allowed");
                return;
            }
            if (stock < min) {
                errorLabel.setText("The entered stock is less than the required amount.");
                return;
            }

            Product product = new Product(count, name, price, stock, min, max);

            for (Part associatedPart : associatedParts) {
                product.addAssociatedPart(associatedPart);
            }

            Inventory.addProduct(product);

            nameTextField.clear();
            priceTextField.clear();
            stockTextField.clear();
            minTextField.clear();
            maxTextField.clear();

            associatedParts.clear();

            errorLabel.setTextFill(Color.web("#0f0"));
            errorLabel.setText(name + " was successfully created.");

            count++;

            cancelButtonListener();

        } catch (Exception e){
            errorLabel.setText("All fields are required");
        }
    }
}
