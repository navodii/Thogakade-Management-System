package controller.item;

import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Item;
import util.CrudUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ItemFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colPackSize;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableView<Item> tblItems;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtPackSize;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    private JFXTextField txtitemCode;

    ItemService itemController = new ItemController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        loadTable();
        
        tblItems.getSelectionModel().selectedItemProperty().addListener((observableValue, item, newValue) -> {
            if (newValue != null) {
                setValueToText(newValue);
            }
        });
    }

    private void setValueToText(Item newValue) {
        txtitemCode.setText(newValue.getItemCode());
        txtDescription.setText(newValue.getDescription());
        txtPackSize.setText(newValue.getPackSize());
        txtUnitPrice.setText(newValue.getUnitPrice().toString());
        txtQty.setText(String.valueOf(newValue.getQty()));
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {

        Item item = new Item(
                txtitemCode.getText(),
                txtDescription.getText(),
                txtPackSize.getText(),
                Double.parseDouble(txtUnitPrice.getText()),
                Integer.parseInt(txtQty.getText())
        );

            Boolean isItemAdd = itemController.addItem(item);

            if (isItemAdd){
                new Alert(Alert.AlertType.INFORMATION,"Item Added").show();
                loadTable();
            }else {
                new Alert(Alert.AlertType.ERROR,"Item Not Added").show();
            }
        clearValues();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String SQL = "DELETE FROM item WHERE ItemCode=?";

        try {
            boolean isItemDeleted = CrudUtil.execute(SQL,txtitemCode.getText());

            if (isItemDeleted) {
                new Alert(Alert.AlertType.INFORMATION, ""+txtitemCode.getText()+"Item Deleted").show();
                loadTable();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR , ""+txtitemCode.getText()+"Item Not Deleted").show();
            loadTable();
        }
        clearValues();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String SQL = "SELECT * FROM item WHERE ItemCode=?";

        try {
            ResultSet resultSet = CrudUtil.execute(SQL, txtitemCode.getText());
            resultSet.next();
            Item item = new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getInt(5)
            );
            setValueToText(item);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Item item = new Item(
                txtitemCode.getText(),
                txtDescription.getText(),
                txtPackSize.getText(),
                Double.parseDouble(txtUnitPrice.getText()),
                Integer.parseInt(txtQty.getText())
        );

            if (itemController.updateItem(item)){
                new Alert(Alert.AlertType.INFORMATION,"Item Updated").show();
                loadTable();
            }else {
            new Alert(Alert.AlertType.ERROR,"Item Not Updated").show();
        }
        clearValues();
    }

    private void clearValues(){
        txtitemCode.setText("");
        txtDescription.setText("");
        txtPackSize.setText("");
        txtUnitPrice.setText("");
        txtQty.setText("");
    }

    private void loadTable(){
        ObservableList<Item> itemObservableList = itemController.getAllItem();
        tblItems.setItems(itemObservableList);
    }

    public void btnCustomerFormOnAction(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/customer_form.fxml"))));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
