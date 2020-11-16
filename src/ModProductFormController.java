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
 * Controls the ModProductForm.fxml scene
 * @author Brian Oliver
 */
public class ModProductFormController {

    // Variables ------------------------------------------------------------------------------------------------------
    private Product product;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private final FilteredList<Part> filteredPartList = new FilteredList<>(Inventory.getAllParts(), s -> true);

    //FXML Annotations ------------------------------------------------------------------------------------------------

        //Text Fields
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

    @FXML
    private TextField productIdTextField;

        // Labels
    @FXML
    private Label errorLabel;

    @FXML
    private Label inputErrorLabel;

        // Buttons

    @FXML
    private Button cancelButton;

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


    //Methods ---------------------------------------------------------------------------------------------------------

    /**
     Closes the ModProductForm.fxml scene and opens MainForm.fxml scene
     *
     * Logical/Runtime Errors: None
     *
     * Compatible Features on Updates: The method name can be updated in future iterations to reflect the standard
     * set[button name]Button naming standard.
     *
     * @throws  IOException if MainForm.fxml can not be loaded.
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
     * Adds the part selected in the PartsTableView to the associatedParts ObservableList
     *
     * Logical/Runtime Errors: None
     *
     * Compatible Features on Update: The method name can be updated in future iterations to reflect the standard
     * set[button name]Button naming standard.
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
     * Removes the part selected in the assocPartsTableView from the product's associatedParts ObservableList
     *
     * Logical/Runtime Errors: None
     * Compatible Features on Update: The method name can be updated in future iterations to reflect the standard
     * set[button name]Button naming standard.
     */
    public void RemoveAssocListener(){
        Part part = assocPartsTableView.getSelectionModel().getSelectedItem();
        if(part == null) {
            errorLabel.setTextFill(Color.web("#e12020"));
            errorLabel.setText("Select an associated product to be removed.");
            return;
        }
        associatedParts.remove(part);
        product.deleteAssociatedPart(part);
        errorLabel.setTextFill(Color.web("#0f5700"));
        errorLabel.setText("Part removed successfully");
    }

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
     * The search field lis
     */
    public void initialize(){

        priceTextField.setTextFormatter(new TextFormatter<Double>(doubleConverter, 0.00, doubleFilter));
        stockTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
        minTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
        maxTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));

        product = MainFormController.getProduct(); // Needed to populate fields.
        productIdTextField.setText(Integer.toString(product.getId()));
        nameTextField.setText(product.getName());
        priceTextField.setText(Double.toString(product.getPrice()));
        stockTextField.setText(Integer.toString(product.getStock()));
        minTextField.setText(Integer.toString(product.getMin()));
        maxTextField.setText(Integer.toString(product.getMax()));
        associatedParts = product.getAllAssociatedParts();

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
                    return s.getName().toLowerCase().contains(input.toLowerCase()) || input.contains(Integer.toString(s.getId()));
                });
            });
        });
    }

    /**
     * Updates the Product with the newly entered information.
     *
     * Logical/Runtime Errors: None
     * Compatible Features on Update: The method name can be updated in future iterations to reflect the standard set[button name]Button naming standard.
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
                inputErrorLabel.setText("A part name is required.");
                return;
            }
            if (min < 0) {
                inputErrorLabel.setText("You can not have a minimum amount less than 0");
                return;
            }
            if (max < min) {
                inputErrorLabel.setText("The max amount is less than the minimum amount.");
                return;
            }
            if (stock > max) {
                inputErrorLabel.setText("The entered stock is greater than the maximum amount allowed");
                return;
            }
            if (stock < min) {
                inputErrorLabel.setText("The entered stock is less than the required amount.");
                return;
            }

            product.setName(name);
            product.setMax(max);
            product.setMin(min);
            product.setPrice(price);
            product.setStock(stock);


            inputErrorLabel.setTextFill(Color.web("#0f0"));
            inputErrorLabel.setText(name + " was successfully updated.");

            cancelButtonListener();

        } catch (Exception e){
            inputErrorLabel.setText("All fields are required");
        }

    }
}

