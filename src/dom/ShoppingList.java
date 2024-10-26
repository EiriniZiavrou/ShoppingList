// package src.dom;
// public class ShoppingList {
    
//     public void addItemToShoppingList(Item item) {
//         item.getCategory().addItem(item);
//     }

//     public void removeItemFromShoppingList(Item item) {
//         item.getCategory().removeItem(item);
//     }

//     private void showShoppingList() {
//         System.out.println("Shopping list:");
//         for (Category category : Category.values()) {
//             System.out.println(category.getName());
//             category.printItemsInCategory();
//         }
//     }

//     public static void main(String[] args) {
//         ShoppingList shoppingList = new ShoppingList();
//         Item apple = new Item("Apple", Category.FRUIT_AND_VEGETABLES, 1.0);   
//         Item milk = new Item("Milk", Category.DAIRY_PRODUCTS, 1.0);
//         Item beef = new Item("Beef", Category.MEAT_AND_SEAFOOD, 1.0);
//         shoppingList.addItemToShoppingList(apple);
//         shoppingList.addItemToShoppingList(milk);
//         shoppingList.addItemToShoppingList(beef);
//         shoppingList.showShoppingList();
//         shoppingList.removeItemFromShoppingList(apple);
//         shoppingList.showShoppingList();
//     }
    
// }
