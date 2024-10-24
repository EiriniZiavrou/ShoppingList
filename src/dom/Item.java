package src.dom;

import javafx.scene.layout.HBox;

public class Item {
    
    private String name;
    private Category category;
    private double quantity;
    private String unit;
    private Boolean isBought;
    private HBox itemBox;

    public Item(String name, Category category, double quantity) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.isBought = false;
    }

    public void changeBoughtStatus() {
        this.isBought = !this.isBought;
    }

    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }

    public String toString() {
        return " " + this.name + " " + this.quantity + " " + this.unit + " " + this.isBought;
    }
    
    public void showItem() {
        System.out.println(this.name + " " + this.quantity + " " + this.unit + " " + this.isBought);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean isBought() {
        return isBought;
    }

    public HBox getItemBox() {
        return itemBox;
    }

    public void setItemBox(HBox itemBox) {
        this.itemBox = itemBox;
    }
}
