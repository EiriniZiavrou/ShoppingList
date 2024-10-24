package src.dom;
import java.util.ArrayList;
import java.util.List;

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

    Category(String name) {
        this.name = name;
        this.items = new ArrayList<Item>();
        //this is just for testing purposes
        // Item item = new Item("Apple", this, 1.0, "kg");
        // Item item2 = new Item("Milk", this, 1.0, "l");
        // this.items.add(item);
        // this.items.add(item2);
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
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
