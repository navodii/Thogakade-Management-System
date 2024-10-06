package controller.item;

import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Item;
import util.CrudUtil;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        String SQL = "INSERT INTO item VALUES(?,?,?,?,?)";

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement psTm = connection.prepareStatement(SQL);
            psTm.setObject(1,item.getItemCode());
            psTm.setObject(2,item.getDescription());
            psTm.setObject(3,item.getPackSize());
            psTm.setObject(4,item.getUnitPrice());
            psTm.setObject(5,item.getQty());
            boolean isCustomerAdd = psTm.executeUpdate() > 0;

            if (isCustomerAdd){
                new Alert(Alert.AlertType.INFORMATION,"Item Added").show();
                loadTable();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Item Not Added").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String SQL = "DELETE FROM item WHERE ItemCode='"+txtitemCode.getText()+"'";

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            boolean isDeleted = connection.createStatement().executeUpdate(SQL)>0;

            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Item Deleted").show();
                loadTable();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR , "Item Not Deleted").show();
            loadTable();
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String SQL = "SELECT * FROM item WHERE ItemCode=?";

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement psTm = connection.prepareStatement(SQL);
            psTm.setObject(1,txtitemCode.getText());
            ResultSet resultSet = psTm.executeQuery();
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

        String SQL = "UPDATR item SET Description=?, PackSize=?, UnitPrice=?, QTYOnHand=? WHERE ItemCode=?";

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement psTm = connection.prepareStatement(SQL);
            psTm.setObject(1,item.getDescription());
            psTm.setObject(2,item.getPackSize());
            psTm.setObject(3,item.getUnitPrice());
            psTm.setObject(4,item.getQty());
            psTm.setObject(5,item.getItemCode());
            Boolean isUpdated = psTm.executeUpdate()>0;

            if (isUpdated){
                new Alert(Alert.AlertType.INFORMATION,"Item Updated");
                loadTable();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Item Not Updated");
        }
    }

    private void loadTable(){
        ObservableList<Item> itemObservableList = FXCollections.observableArrayList();

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade", "root", "12345");
            String SQL = "SELECT * FROM item";

            ResultSet resultSet = connection.createStatement().executeQuery(SQL);

            while (resultSet.next()){
                Item item = new Item(
                        resultSet.getString("ItemCode"),
                        resultSet.getString("Description"),
                        resultSet.getString("PackSize"),
                        resultSet.getDouble("UnitPrice"),
                        resultSet.getInt("QtyOnHand")
                );
                itemObservableList.add(item);
            }
            tblItems.setItems(itemObservableList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
