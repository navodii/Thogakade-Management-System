package controller.item;

import javafx.collections.ObservableList;
import model.Item;

public class ItemController implements ItemService{
    @Override
    public boolean addItem(Item item) {
        return false;
    }

    @Override
    public boolean updateItem(Item item) {
        return false;
    }

    @Override
    public Item searchItem(String itemCode) {
        return null;
    }

    @Override
    public boolean deleteItem(String itemCode) {
        return false;
    }

    @Override
    public ObservableList<Item> getAllItem() {
        return null;
    }
}
