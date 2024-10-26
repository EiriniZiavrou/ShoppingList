package src.application;
import src.dom.Category;
import src.dom.Item;
import java.util.HashMap;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class GroceryListUI extends Application{

    private final HashMap<Category, VBox> categoryBoxes = new HashMap<Category, VBox>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("My Shopping List");
        primaryStage.getIcons().add(new Image("file:src/resources/images/icon.png"));
        
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        // screenshot button
        HBox optionsBox = new HBox();
        CheckBox checkBox = new CheckBox("Hide those in cart");
        checkBox.setSelected(false);

        // this is search bar
        ImageView searchIcon = new ImageView(new Image("file:src/resources/images/search.png"));
        searchIcon.setFitHeight(30);
        searchIcon.setFitWidth(30);
        TextField searchInput = new TextField();
        searchInput.setPromptText("Item name...");
        // TOTHINK: if text exists and new item is added, should it be visible?
        searchInput.textProperty().addListener(event -> {
            if (searchInput.getText().isEmpty()) {
                for (Category category : Category.values()) {
                    for (Item item : category.getItems()) {
                        if (item.isBought()) {
                            break;
                        }
                        item.getItemBox().setVisible(true);
                    }
                }
                return;
            }
            for (Category category : Category.values()) {
                for (Item item : category.getItems()) {
                    if (item.getName().toLowerCase().contains(searchInput.getText().toLowerCase())) {
                        if (item.isBought()) {
                            continue;
                        }
                        item.getItemBox().setVisible(true);
                    }
                    else {
                        item.getItemBox().setVisible(false);
                    }
                }
            }
            System.out.println("Searching for: " + searchInput.getText());
        });
        HBox inputBox = new HBox(searchIcon, searchInput);
       
        
        ImageView screenshotButton = new ImageView(new Image("file:src/resources/images/save.png"));
        screenshotButton.setFitHeight(30);
        screenshotButton.setFitWidth(30);
        screenshotButton.setOnMouseClicked(event -> saveScreenshot(vbox));
        HBox screenShotBox = new HBox();
        screenShotBox.getChildren().add(screenshotButton);
        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);
        optionsBox.getChildren().addAll(checkBox, leftSpacer, inputBox, rightSpacer, screenShotBox);
        optionsBox.setAlignment(Pos.CENTER);
             

        for (Category category : Category.values()) {
            VBox categoryBox = new VBox();
            categoryBoxes.put(category, categoryBox);
            ImageView addNewItemButton = new ImageView(new Image("file:src/resources/images/add_icon.png"));
            addNewItemButton.setFitHeight(20);
            addNewItemButton.setFitWidth(20);
            Label categoryName = new Label(category.getName());
            categoryName.setStyle("-fx-font-size: 20px; -fx-font-weight: 200; -fx-font-family: 'Georgia';");
            HBox tempBox = new HBox(10, categoryName, addNewItemButton);
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
        setPicturesForCategories();

        checkBox.setOnAction(event -> updateItemVisibility(checkBox));

        VBox sceneBox = new VBox();
        sceneBox.getChildren().addAll(optionsBox, vbox);
        ScrollPane scrollPane = new ScrollPane(sceneBox);
        scrollPane.setFitToWidth(true);

        BorderPane root = new BorderPane();
        root.setCenter(scrollPane);
        
        Image backgroundImage = new Image("file:src/resources/images/background.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, false);
        Background background = new Background(new BackgroundImage(backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            backgroundSize
        ));
        root.setBackground(background);

        Scene scene = new Scene(root, 550, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveScreenshot(VBox vbox) {
        WritableImage image = vbox.snapshot(new SnapshotParameters(), null);
        File file = new File("output/savedImages/screenshot.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            System.out.println("Screenshot saved: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPicturesForCategories(){
        for (Category category : categoryBoxes.keySet()) {
            HBox tempBox = (HBox) categoryBoxes.get(category).getChildren().get(0);
            ImageView image = new ImageView(new Image("file:src/resources/images/" + category + ".jpg"));
            image.setFitHeight(49.5);
            image.setFitWidth(75);
            category.setImage(image);
            tempBox.getChildren().add(0, image);
            tempBox.setAlignment(Pos.BOTTOM_LEFT);
        }
    }

    /// Creates the HBox for an item and handles all changes to the item and deletion
    private HBox createItemBox(Item item) {
        HBox itemBox = new HBox();
        CheckBox itemCheckBox = new CheckBox();
        TextField itemQuantity = new TextField(String.valueOf(item.getQuantity()));
        itemQuantity.setPrefWidth(50);
        //itemQuantity.setStyle("-fx-background-color: transparent;");
        ComboBox<String> itemUnit = new ComboBox<>();
        itemUnit.getItems().addAll("", "kg", "g", "L", "ml", "oz", "pcs");
        String unit = itemUnit.getItems().get(0);
        item.setUnit(unit);
        itemUnit.setValue(unit); 
        TextField itemName = new TextField(item.getName());
        //itemName.setStyle("-fx-background-color: transparent;");
        TextField itemBoughtTextField = new TextField(item.isBought() ? "true" : "false");

        ImageView trashCanIcon = new ImageView(new Image("file:src/resources/images/trash_can.png"));
        trashCanIcon.setFitHeight(20);
        trashCanIcon.setFitWidth(20);

        ImageView moveItemIcon = new ImageView(new Image("file:src/resources/images/move.png"));
        moveItemIcon.setFitHeight(20);
        moveItemIcon.setFitWidth(20);

        itemCheckBox.setSelected(item.isBought());
        ImageView initCartStatus;
        if (item.isBought()) {
            initCartStatus = new ImageView(new Image("file:src/resources/images/addedInCart.png"));
        }
        else {
            initCartStatus = new ImageView(new Image("file:src/resources/images/notInCart.png"));
        }
        initCartStatus.setFitHeight(30);
        initCartStatus.setFitWidth(30);
        itemCheckBox.setGraphic(initCartStatus);
        
        Region spacer = new Region();
        spacer.setMinWidth(10);
        itemBox.getChildren().addAll(itemCheckBox, spacer, itemQuantity, itemUnit, itemName, trashCanIcon, moveItemIcon);
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
            // TODO: duplicate code
            ImageView cartStatus;
            if (item.isBought()) {
                cartStatus = new ImageView(new Image("file:src/resources/images/addedInCart.png"));
            }
            else {
                cartStatus = new ImageView(new Image("file:src/resources/images/notInCart.png"));
            }
            cartStatus.setFitHeight(30);
            cartStatus.setFitWidth(30);
            itemCheckBox.setGraphic(cartStatus);

            CheckBox checkBox = (CheckBox) ((HBox) itemBox.getParent().getParent().getParent().getChildrenUnmodifiable().get(0)).getChildrenUnmodifiable().get(0);
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
            Scene scene = new Scene(moveItemBox, 470, 426+30); // 426 is the height of only the photos
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
            // ImageView categoryButton = category.getImage();
            // categoryButton.setFitHeight(99);
            // categoryButton.setFitWidth(150);
            Button categoryButton = new Button(category.getName());
            // categoryButton.setOnMouseClicked(event -> {
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