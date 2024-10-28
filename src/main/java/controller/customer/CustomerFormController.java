package controller.customer;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import util.CrudUtil;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {

    @FXML
    private JFXComboBox<String> cmbTitle;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colCity;

    @FXML
    private TableColumn<?, ?> colDob;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPostalCode;

    @FXML
    private TableColumn<?, ?> colProvince;

    @FXML
    private TableColumn<?, ?> colSalary;

    @FXML
    private TableColumn<?, ?> colTitle;

    @FXML
    private DatePicker dateDob;

    @FXML
    private TableView<Customer> tblCustomers;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtCity;

    @FXML
    private JFXTextField txtCustomerId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPostalCode;

    @FXML
    private JFXTextField txtProvince;

    @FXML
    private JFXTextField txtSalary;

    CustomerService customerController = new CustomerController();

    @FXML
    void btnAddOnAction(ActionEvent event) {

        Customer customer = new Customer(
                txtCustomerId.getText(),
                cmbTitle.getValue(),
                txtName.getText(),
                dateDob.getValue(),
                Double.parseDouble(txtSalary.getText()),
                txtAddress.getText(),
                txtCity.getText(),
                txtProvince.getText(),
                txtPostalCode.getText()
        );

            boolean isCustomerAdd = customerController.addCustomer(customer);
            if (isCustomerAdd){
                new Alert(Alert.AlertType.INFORMATION,"Customer Added").show();
                loadTable();
            }else {
                new Alert(Alert.AlertType.ERROR,"Customer Not Added").show();
            }
        clearValues();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
            if (customerController.deleteCustomer(txtCustomerId.getText())){
                new Alert(Alert.AlertType.INFORMATION,""+txtCustomerId.getText()+"Customer Deleted !!").show();
                loadTable();
            }
        else{
            new Alert(Alert.AlertType.ERROR,""+txtCustomerId.getText()+"Customer Not Deleted !!").show();
        }
        clearValues();
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {

    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        setValueToText(customerController.searchCustomer(txtCustomerId.getText()));

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Customer customer = new Customer(
                txtCustomerId.getText(),
                cmbTitle.getValue(),
                txtName.getText(),
                dateDob.getValue(),
                Double.parseDouble(txtSalary.getText()),
                txtAddress.getText(),
                txtCity.getText(),
                txtProvince.getText(),
                txtPostalCode.getText()
        );
            if (customerController.updateCustomer(customer)){
                new Alert(Alert.AlertType.INFORMATION,"Customer Updated").show();
                loadTable();
            }else {
                new Alert(Alert.AlertType.ERROR,"Customer Not Updated").show();
            }
        clearValues();
    }

    private void loadTable(){
        ObservableList<Customer> customerObservableList = customerController.getAllCustomers();
        tblCustomers.setItems(customerObservableList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> customerTitleList = FXCollections.observableArrayList();
        customerTitleList.add("Mr");
        customerTitleList.add("Mrs");
        customerTitleList.add("Miss");
        customerTitleList.add("Ms");
        cmbTitle.setItems(customerTitleList);
        loadTable();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));

        tblCustomers.getSelectionModel().selectedItemProperty().addListener((observableValue, customer, newValue) -> {
            if (newValue!=null){
                setValueToText(newValue);
            }
        });

    }

    private void setValueToText(Customer newValue) {
        txtCustomerId.setText(newValue.getId());
        txtName.setText(newValue.getName());
        cmbTitle.setValue(newValue.getTitle());
        txtProvince.setText(newValue.getProvince());
        txtPostalCode.setText(newValue.getPostalCode());
        txtSalary.setText(newValue.getSalary().toString());
        dateDob.setValue(newValue.getDob());
        txtAddress.setText(newValue.getAddress());
        txtCity.setText(newValue.getCity());
    }

    private void clearValues(){
        txtCustomerId.setText("");
        txtName.setText("");
        cmbTitle.setValue("");
        txtProvince.setText("");
        txtPostalCode.setText("");
        txtSalary.setText("");
        //dateDob.setValue(LocalDate.parse(""));;
        txtAddress.setText("");
        txtCity.setText("");
    }
}
