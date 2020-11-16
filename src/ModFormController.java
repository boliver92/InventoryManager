import classes.InHouse;
import classes.Inventory;
import classes.Outsourced;
import classes.Part;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import java.io.IOException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class ModFormController {

    // Variables ------------------------------------------------------------------------------------------------------

    private Part part;
    private InHouse inHouse;
    private Outsourced outsourced;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;


    // FXML Annotations -----------------------------------------------------------------------------------------------

        // Labels
    @FXML
    private Label modPartMachineCompanyLabel;

    @FXML
    private Label errorLabel;

        //TextFields

    @FXML
    private TextField modPartIdField;

    @FXML
    private TextField modPartNameTextField;

    @FXML
    private TextField modPartStockTextField;

    @FXML
    private TextField modPartPriceTextField;

    @FXML
    private TextField modPartMinTextField;

    @FXML
    private TextField modPartMaxTextField;

    @FXML
    private TextField modPartMachineCompanyTextField;

        //Radio Buttons

    @FXML
    private RadioButton inHouseRadio;

    @FXML
    private RadioButton outsourcedRadio;

        //Button

    @FXML
    private Button cancelButton;

    /**
     * Closes the ModForm.fxml scene and opens MainForm.fxml scene
     *
     * Logical/Runtime Errors: None
     *
     * Compatible Features on Updates: The method name can be updated in future iterations to reflect the standard
     * set[button name]Button naming standard.
     *
     * @param   event       Click event that causes the method to execute.
     * @throws  IOException if MainForm.fxml can not be loaded.
     *
     */
    @FXML
    void cancelButtonListener(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("MainForm.fxml"));

        Scene scene = new Scene(parent);
        Stage stage = (Stage) cancelButton.getScene().getWindow();

        stage.setTitle("Inventory Manager");
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switches the modPartMachineCompanyLabel string based on the radio button selected
     *
     * Logical/Runtime Errors: None
     * Compatible Features on Update: None
     *
     * @param   event   Radio button being clicked
     */
    @FXML
    void radioSelectionListener(ActionEvent event) {
        if (inHouseRadio.isSelected()){
            modPartMachineCompanyLabel.setText("Machine ID");
            modPartMachineCompanyTextField.clear();
            if(part instanceof InHouse){
                modPartMachineCompanyTextField.setText(Integer.toString(((InHouse) part).getMachineId()));
            }
        }
        if(outsourcedRadio.isSelected()){
            modPartMachineCompanyLabel.setText("Company Name");
            modPartMachineCompanyTextField.clear();
            if(part instanceof Outsourced){
                modPartMachineCompanyTextField.setText(((Outsourced) part).getCompanyName());
            }
        }
    }

    // Input Validation -----------------------------------------------------------------------------------------------

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

    /**
     * Populates the ModForm.fxml with MainFormController.part information, selects the appropriate radio button based on the
     * part type, and adds a TextFormatter to the price, stock, min, and max fields to filter the input in the text fields.
     *
     * Logical/Runtime Error:
     *
     * I attempted to create a TextValidation class to pass the text fields into so I could refrain
     * from having to reuse the Lambda functions in each controller (integerFilter, doubleFilter, doubleConverter) and to
     * update the UI labels associated with each field. However, I was unsuccessful in implementing a class that did not throw
     * errors when attempting to set the text formatter. I finally decided to include the lambda functions in each class to handle
     * the text validation and label updates.
     *
     *
     * Compatible Features for Updates:
     *
     * The textformatter can be updated to show a double with a precision of 2 decimal places.
     * The lambda functions in each controller may be implemented in a way where it is not listed in each controller.
     *
     */
    @FXML
    public void initialize(){
        this.part = MainFormController.getPart();

        modPartPriceTextField.setTextFormatter(new TextFormatter<Double>(doubleConverter, 0.00, doubleFilter));
        modPartStockTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
        modPartMinTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
        modPartMaxTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));


        Platform.runLater(() -> {
            modPartNameTextField.setText(part.getName());
            modPartStockTextField.setText(Integer.toString(part.getStock()));
            modPartIdField.setText(Integer.toString(part.getId()));
            modPartMinTextField.setText(Integer.toString(part.getMin()));
            modPartPriceTextField.setText(Double.toString(part.getPrice()));
            modPartMaxTextField.setText(Integer.toString(part.getMax()));

            System.out.println(part.getClass());

            if(part instanceof InHouse){
                inHouse = (InHouse) part;
                modPartMachineCompanyLabel.setText("Machine ID");
                modPartMachineCompanyTextField.setText(Integer.toString(inHouse.getMachineId()));
                inHouseRadio.setSelected(true);
                outsourcedRadio.setSelected(false);
            }
            if(part instanceof Outsourced){
                outsourced = (Outsourced) part;
                modPartMachineCompanyLabel.setText("Company Name");
                modPartMachineCompanyTextField.setText(outsourced.getCompanyName());
                outsourcedRadio.setSelected(true);
                inHouseRadio.setSelected(false);
            }
        });
    }

    /**
     * Removes previous copy of part in Inventory.allParts and saves the new copy.
     *
     * Logical/Runtime Error: This method kept throwing various Exceptions when attempting to save the updated Part. I made two changes to
     * correct the issues. In order to set the machineID or companyName, the Part being referenced has to be cast to the
     * corresponding child Part (InHouse / Outsourced). This can only be done if the Part is actually the corresponding
     * type. Otherwise, I had to create a new instance of the Part in the opposite class with the same information, update
     * the machineID/companyName, and replace the previous Part using the index of the Part (part.ID - 1) and
     * Inventory.updatePart method.
     *
     * Compatible Features on Update: None
     *
     * @throws  Exception   if a field is not completed.
     */
    public void saveButtonListener() throws IOException {
        try {
            name = modPartNameTextField.getText();
            stock = Integer.parseInt(modPartStockTextField.getText());
            min = Integer.parseInt(modPartMinTextField.getText());
            max = Integer.parseInt(modPartMaxTextField.getText());
            price = Double.parseDouble(modPartPriceTextField.getText());

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

            part.setName(name);
            part.setPrice(price);
            part.setMin(min);
            part.setMax(max);
            part.setStock(stock);

            if(inHouseRadio.isSelected()) {
                if (part instanceof InHouse) {
                    ((InHouse) part).setMachineId(Integer.parseInt(modPartMachineCompanyTextField.getText()));
                    Inventory.updatePart(part.getId() - 1, part);
                } else {
                    inHouse = new InHouse(part.getId(), part.getName(), part.getPrice(), part.getStock(), part.getMin(),
                            part.getMax(), Integer.parseInt(modPartMachineCompanyTextField.getText()));
                    Inventory.updatePart(inHouse.getId() - 1, inHouse);
                }
            }

            if(outsourcedRadio.isSelected()){
                if(part instanceof Outsourced){
                    ((Outsourced) part).setCompanyName(modPartMachineCompanyTextField.getText());
                    Inventory.updatePart(part.getId()-1, part);
                } else {
                    outsourced = new Outsourced(part.getId(), part.getName(), part.getPrice(), part.getStock(), part.getMin(),
                            part.getMax(), modPartMachineCompanyTextField.getText());
                    Inventory.updatePart(outsourced.getId()-1, outsourced);
                }
            }

        } catch (Exception e){
            errorLabel.setText("All fields must be completed.");
        }

        Parent parent = FXMLLoader.load(getClass().getResource("MainForm.fxml"));

        Scene scene = new Scene(parent);
        Stage stage = (Stage) cancelButton.getScene().getWindow();

        stage.setTitle("Inventory Manager");
        stage.hide();
        stage.setScene(scene);
        stage.show();

    }

}

