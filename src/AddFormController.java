import classes.Outsourced;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import classes.InHouse;
import classes.Inventory;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import java.io.IOException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * Controls the AddForm.fxml scene
 * @author Brian Oliver
 */
public class AddFormController {

    // Variables ------------------------------------------------------------------------------------------------------

    private String name;
    private int stock;
    private int min;
    private int max;
    private int id;
    private double price;
    private static int currentID = 1;
    private int machineId;
    private InHouse inHouse;
    private Outsourced outsourced;


    // FXML Annotations -----------------------------------------------------------------------------------------------

        // Labels
    @FXML
    private Label addPartMachineCompanyLabel;

    @FXML
    private Label errorLabel;

        // TextFields
    @FXML
    private TextField addPartNameTextField;

    @FXML
    private TextField addPartStockTextField;

    @FXML
    private TextField addPartPriceTextField;

    @FXML
    private TextField addPartMaxTextField;

    @FXML
    private TextField addPartMachineCompanyTextField;

    @FXML
    private TextField addPartMinTextField;

        // Radio Buttons
    @FXML
    private RadioButton inHouseRadio;

    @FXML
    private RadioButton outsourcedRadio;

        // Buttons
    @FXML
    private Button cancelButton;


    // Input Validation Methods ---------------------------------------------------------------------------------------


    Pattern validDoubleState = Pattern.compile("(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

    private final UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String input = change.getControlNewText();
        if(input.matches("([0-9]*)?")) {
            errorLabel.setText("");
            return change;
        }
        errorLabel.setText("This field only accepts numbers");
        return null;
    };

    private final UnaryOperator<TextFormatter.Change> doubleFilter = change -> {
        String input = change.getControlNewText();
        if(validDoubleState.matcher(input).matches()) {
            errorLabel.setText("");
            return change;
        } else {
            errorLabel.setText("The input must be in price form: x.xx");
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


    // Methods ---------------------------------------------------------------------------------------------------------


    /**
     * This method is run when the Add part scene is loaded.
     *
     * The method adds a TextFormatter to the price, stock, min, and max fields to filter the input in the text fields.
     *
     * Logical/Runtime Error: I attempted to create a TextValidation class to pass the text fields into so I could refrain
     * from having to reuse the Lambda functions in each controller (integerFilter, doubleFilter, doubleConverter) and to
     * update the UI labels associated with each field. However, I was unsuccessful in implementing a class that did not throw
     * errors when attempting to set the text formatter. I finally decided to include the lambda functions in each class to handle
     * the text validation and label updates.
     *
     * Compatible Features for Updates: The textformatter can be updated to show a double with a precision of 2 decimal places.
     * The lambda functions in each controller may be implemented in a way where it is not listed in each controller.
     */
    public void initialize(){
        addPartPriceTextField.setTextFormatter(new TextFormatter<Double>(doubleConverter, 0.00, doubleFilter));
        addPartStockTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
        addPartMinTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
        addPartMaxTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));

    }


    /**
     * Saves input from the Add Part Form and adds it to the Inventory ObservableList<Part>
     *
     * Runtime/Logical Errors: This method kept throwing an Exception and displaying the "All Fields Required" message on
     * the errorLabel. This was due to me placing the method to return to the main screen on the wrong line.
     *
     * Compatible Features on Updates: This function can be expanded in future updates by expanding confirming that the price's
     * double precision is set to 2. The method can be renamed to follow the normal naming standards of setSaveButton. However, I
     * continued the style of [button name]ButtonListener after defining a substantial amount of methods using that standard.
     *
     */
    public void saveButtonListener() {
        try {

            name = addPartNameTextField.getText();
            stock = Integer.parseInt(addPartStockTextField.getText());
            min = Integer.parseInt(addPartMinTextField.getText());
            max = Integer.parseInt(addPartMaxTextField.getText());
            price = Double.parseDouble(addPartPriceTextField.getText());

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

            if (inHouseRadio.isSelected()) {
                machineId = Integer.parseInt(addPartMachineCompanyTextField.getText());
                if (machineId > 0) {

                    inHouse = new InHouse(currentID, name, price, stock, min, max, machineId);
                    Inventory.addPart(inHouse);

                    errorLabel.setTextFill(Color.web("#00ff00"));
                    errorLabel.setText(name + " was added successfully.");

                    addPartNameTextField.clear();
                    addPartStockTextField.clear();
                    addPartPriceTextField.clear();
                    addPartMinTextField.clear();
                    addPartMaxTextField.clear();
                    addPartMachineCompanyTextField.clear();

                    currentID++;

                } else {
                    errorLabel.setText("The Machine ID must be greater than 0");
                }
            }

            if (outsourcedRadio.isSelected()) {
                String companyName = addPartMachineCompanyTextField.getText();
                if (!companyName.isEmpty()) {

                    Outsourced outsourced = new Outsourced(currentID, name, price, stock, min, max, companyName);
                    Inventory.addPart(outsourced);

                    errorLabel.setTextFill(Color.web("#00ff00"));
                    errorLabel.setText(name + " was added successfully.");

                    addPartNameTextField.clear();
                    addPartStockTextField.clear();
                    addPartMinTextField.clear();
                    addPartMaxTextField.clear();
                    addPartMachineCompanyTextField.clear();
                    addPartPriceTextField.clear();



                } else {
                    errorLabel.setText("The company name is required.");
                }
            }

            cancelButtonListener();

        } catch (Exception e){
            errorLabel.setText("All fields are required");
        }
    }


    /**
     * Updates the addPartMachineCompanyLabel to Machine ID if the inHouseRadio is selected. If the outsourcedRadio is
     * selected, the label is updated to show Company Name
     *
     * Logical/Runtime Errors: None
     *
     * Compatible Features on Update:  The method can be renamed to follow the normal naming standards of setRadioSelection.
     * However, I continued the style of [control item]Listener after defining a substantial amount of methods using this
     * naming standard.
     */
    public void radioSelectionListener(){
        if(inHouseRadio.isSelected()){
            addPartMachineCompanyLabel.setText("Machine ID");
        }
        if(outsourcedRadio.isSelected()){
            addPartMachineCompanyLabel.setText("Company Name");
        }
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
}
