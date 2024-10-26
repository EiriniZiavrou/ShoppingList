package src.dom;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;

public enum Category {
    FRUIT_AND_VEGETABLES("Fruit and Vegetables"),
    DAIRY_PRODUCTS("Dairy Products"),
    MEAT_AND_SEAFOOD("Meat and Seafood"),
    BAKERY_ITEMS("Bakery Items"),
    PANTRY_STAPLES("Pantry Staples"),
    SNACKS("Snacks"),
    BEVERAGES("Beverages"),
    FROZEN_FOODS("Frozen Foods"),
    CONDIMENTS_AND_SAUCES("Condiments and Sauces"),
    HOUSEHOLD_SUPPLIES("Household Supplies"),
    PERSONAL_CARE("Personal Care"),
    MISCELLANEOUS("Miscellaneous");

    private final String name;
    private List<Item> items;
    private ImageView image;

    Category(String name) {
        this.name = name;
        this.items = new ArrayList<Item>();
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public void removeItem(Item item) {
        this.items.remove(item);
    }

    public void printItemsInCategory() {
        for (Item item : items) {
            item.showItem();
        }
    }
}
