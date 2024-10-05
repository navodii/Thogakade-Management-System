package controller.item;

import com.jfoenix.controls.JFXTextField;
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
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade", "root", "12345");
            PreparedStatement psTm = connection.prepareStatement(SQL);
            psTm.setObject(1,item.getItemCode());
            psTm.setObject(2,item.getDescription());
            psTm.setObject(3,item.getPackSize());
            psTm.setObject(4,item.getUnitPrice());
            psTm.setObject(5,item.getQty());
            boolean isCustomerAdd = psTm.executeUpdate() > 0;

            if (isCustomerAdd){
                new Alert(Alert.AlertType.INFORMATION,"Customer Added").show();
                loadTable();
            }
            System.out.println(isCustomerAdd);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Customer Not Added").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

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
