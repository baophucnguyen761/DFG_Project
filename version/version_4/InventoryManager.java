import java.util.ArrayList;
import java.util.Scanner;

public class InventoryManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> products = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();
        String tempProduct; // Temporary variable for product name
        int tempQuantity;   // Temporary variable for product quantity

        System.out.println("Welcome to the Inventory Management System!");

        // Input loop
        while (true) {
            System.out.print("Enter a product name (or 'exit' to stop): ");
            tempProduct = scanner.nextLine();
            if (tempProduct.equalsIgnoreCase("exit")) {
                break;
            }

            System.out.print("Enter the quantity for " + tempProduct + ": ");
            tempQuantity = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            if (tempQuantity >= 0) {
                products.add(tempProduct);
                quantities.add(tempQuantity);
                System.out.println(tempProduct + " with quantity " + tempQuantity + " added!");
            } else {
                System.out.println("Quantity cannot be negative. Try again.");
            }
        }

        if (products.isEmpty()) {
            System.out.println("No products entered. Exiting program.");
            return;
        }

        // Display all products
        System.out.println("\nCurrent Inventory:");
        for (int i = 0; i < products.size(); i++) {
            tempProduct = products.get(i);
            tempQuantity = quantities.get(i);
            System.out.println(tempProduct + ": " + tempQuantity);
        }

        // Find the product with the highest quantity
        int maxIndex = 0;
        for (int i = 1; i < quantities.size(); i++) {
            if (quantities.get(i) > quantities.get(maxIndex)) {
                maxIndex = i;
            }
        }
        System.out.println("\nProduct with the highest quantity: " 
                + products.get(maxIndex) + " (" + quantities.get(maxIndex) + ")");

        // Count products with low stock (< 10)
        int lowStockCount = 0;
        for (int qty : quantities) {
            tempQuantity = qty; // Assign to temp variable
            if (tempQuantity < 10) {
                lowStockCount++;
            }
        }
        System.out.println("Number of products with low stock (< 10): " + lowStockCount);

        scanner.close();
        System.out.println("Inventory processing complete. Thank you!");
    }
}
