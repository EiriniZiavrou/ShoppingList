package src.application;
import src.dom.Category;
import src.dom.Item;
import java.util.HashMap;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GroceryListUI extends Application{

    private final HashMap<Category, VBox> categoryBoxes = new HashMap<Category, VBox>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("My Shopping List");
        primaryStage.getIcons().add(new Image("file:src/application/icon.png"));
        
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        CheckBox checkBox = new CheckBox("Hide purchased items");
        checkBox.setSelected(false);
        vbox.getChildren().add(checkBox);

        // this is for later thought
        // TextField itemInput = new TextField();
        // itemInput.setPromptText("Enter smth");
        // Button addButton = new Button("Do smth");
        // HBox inputBox = new HBox(10, itemInput, addButton);
        // vbox.getChildren().add(inputBox);

        for (Category category : Category.values()) {
            VBox categoryBox = new VBox();
            categoryBoxes.put(category, categoryBox);
            ImageView addNewItemButton = new ImageView(new Image("file:src/application/add_icon.png"));
            addNewItemButton.setFitHeight(15);
            addNewItemButton.setFitWidth(15);
            HBox tempBox = new HBox(10, new Label(category.getName()), addNewItemButton);
            categoryBox.getChildren().add(tempBox);
            for (Item item : category.getItems()) {
                HBox itemBox = createItemBox(item);
                categoryBox.getChildren().add(itemBox);
            }
            vbox.getChildren().add(categoryBox);

            addNewItemButton.setOnMouseClicked(event -> {
                Item newItem = new Item("Name", category, 0.0);
                category.addItem(newItem);
                HBox newItemBox = createItemBox(newItem);
                categoryBox.getChildren().add(newItemBox);
                System.out.println("Category " + category + newItem.toString());
            });
        }

        checkBox.setOnAction(event -> updateItemVisibility(checkBox));

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);

        BorderPane root = new BorderPane();
        root.setCenter(scrollPane);
        
        Image backgroundImage = new Image("file:src/application/background.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, false);
        Background background = new Background(new BackgroundImage(backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            backgroundSize
        ));
        root.setBackground(background);

        Scene scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /// Creates the HBox for an item and handles all changes to the item and deletion
    private HBox createItemBox(Item item) {
        HBox itemBox = new HBox();
        CheckBox itemCheckBox = new CheckBox();
        TextField itemQuantity = new TextField(String.valueOf(item.getQuantity()));
        ComboBox<String> itemUnit = new ComboBox<>();
        itemUnit.getItems().addAll("", "kg", "g", "L", "ml", "oz", "pcs");
        String unit = itemUnit.getItems().get(0);
        item.setUnit(unit);
        itemUnit.setValue(unit); 
        TextField itemName = new TextField(item.getName());
        TextField itemBoughtTextField = new TextField(item.isBought() ? "true" : "false");

        ImageView trashCanIcon = new ImageView(new Image("file:src/application/trash_can.png"));
        trashCanIcon.setFitHeight(20);
        trashCanIcon.setFitWidth(20);

        ImageView moveItemIcon = new ImageView(new Image("file:src/application/move.png"));
        moveItemIcon.setFitHeight(20);
        moveItemIcon.setFitWidth(20);

        // Set the initial state of the CheckBox
        itemCheckBox.setSelected(item.isBought());

        itemBox.getChildren().addAll(itemCheckBox, itemQuantity, itemUnit, itemName, trashCanIcon, moveItemIcon);
        // for debugging purposes
        // itemBox.getChildren().addAll(itemCheckBox, itemQuantity, itemUnit, itemName, itemBoughtTextField, trashCanIcon, moveItemIcon);
        item.setItemBox(itemBox);

        // Update item Fields when TextField is changed
        itemQuantity.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (item.getQuantity() == Double.valueOf(itemQuantity.getText())) return;
                item.setQuantity(Double.valueOf(itemQuantity.getText()));
                System.out.println("Category " + item.getCategory() + item.toString());
            } 
        });
        itemUnit.valueProperty().addListener((observable, oldValue, newValue) -> {
            item.setUnit(newValue);
            System.out.println("Category " + item.getCategory() + item.toString());
        });
        itemName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (item.getName().equals(itemName.getText())) return;
                item.setName(itemName.getText());
                System.out.println("Category " + item.getCategory() + item.toString());
            } 
        });
        itemCheckBox.setOnAction(event -> {
            item.changeBoughtStatus();
            itemBoughtTextField.setText(item.isBought() ? "true" : "false");
            // itemBox parent is the VBox of Category, the parent of the Category Box is the VBox and the first child is the CheckBox
            CheckBox checkBox = (CheckBox) itemBox.getParent().getParent().getChildrenUnmodifiable().get(0);
            updateItemVisibility(checkBox);
            System.out.println("Category " + item.getCategory() + item.toString());
        });
        trashCanIcon.setOnMouseClicked(event -> {
            item.getCategory().removeItem(item);
            ((VBox) itemBox.getParent()).getChildren().remove(itemBox);
            System.out.println("DELETING --- Category " + item.getCategory() + item.toString());
        });
        moveItemIcon.setOnMouseClicked(event -> {
            Stage moveItemStage = new Stage();
            moveItemStage.setTitle("Move Item");
            moveItemStage.getIcons().add(moveItemIcon.getImage());
            VBox moveItemBox = new VBox();
            moveItemBox.getChildren().addAll(new Label("Choose the category you would like to move your item"), addCategoriesButtonToGridPane(item, moveItemStage));
            Scene scene = new Scene(moveItemBox, 400, 300);
            moveItemStage.setScene(scene);
            moveItemStage.show();
            System.out.println("SELECTED ITEM ---" +item.getCategory() + item.toString());
        });
        return itemBox;
    }
    
    private void updateItemVisibility(CheckBox checkBox) {
        for (Category category : Category.values()) {
            for (Item item : category.getItems()) {
                if (item.isBought() && checkBox.isSelected()) {
                    HBox itemBox = item.getItemBox();
                    itemBox.setVisible(false);
                }
                else {
                    HBox itemBox = item.getItemBox();
                    itemBox.setVisible(true);
                }
            }
        }
    }

    /// This method creates a GridPane of buttons for each category
    /// In this case we have 12 categories, so we will have 4 rows and 3 columns
    private GridPane addCategoriesButtonToGridPane(Item item, Stage moveItemStage) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int col = 0;
        int row = 0;
        for (Category category : Category.values()) {
            Button categoryButton = new Button(category.getName());
            categoryButton.setOnAction(event -> {
                item.getCategory().removeItem(item);
                HBox tempBox = item.getItemBox();
                ((VBox) tempBox.getParent()).getChildren().remove(tempBox);
                item.setCategory(category);
                category.addItem(item);
                categoryBoxes.get(category).getChildren().add(tempBox);
                moveItemStage.close();
                System.out.println("DEBUG ---" + item.getCategory() + item.toString());
            });
            gridPane.add(categoryButton, col, row);
            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
            if (row == 4) {
                break;
            }
        }
        return gridPane;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
