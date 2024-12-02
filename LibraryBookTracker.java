import java.util.ArrayList;
import java.util.Scanner;

public class LibraryBookTracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> books = new ArrayList<>();
        ArrayList<Boolean> availability = new ArrayList<>();
        String tempBook; // Temporary variable for book name
        boolean tempStatus; // Temporary variable for availability status

        System.out.println("Welcome to the Library Book Tracker!");

        // Input loop for adding books
        while (true) {
            System.out.print("Enter a book title to add (or 'done' to finish): ");
            tempBook = scanner.nextLine();
            if (tempBook.equalsIgnoreCase("done")) {
                break;
            }

            books.add(tempBook);
            availability.add(true); // By default, new books are available
            System.out.println(tempBook + " added to the library.");
        }

        if (books.isEmpty()) {
            System.out.println("No books entered. Exiting program.");
            return;
        }

        // Menu for library operations
        while (true) {
            System.out.println("\nLibrary Menu:");
            System.out.println("1. Borrow a book");
            System.out.println("2. Return a book");
            System.out.println("3. View all books");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            if (choice == 4) {
                break;
            }

            switch (choice) {
                case 1: // Borrow a book
                    System.out.print("Enter the title of the book to borrow: ");
                    tempBook = scanner.nextLine();
                    if (books.contains(tempBook)) {
                        int index = books.indexOf(tempBook);
                        tempStatus = availability.get(index);
                        if (tempStatus) {
                            availability.set(index, false);
                            System.out.println("You borrowed " + tempBook);
                        } else {
                            System.out.println(tempBook + " is currently unavailable.");
                        }
                    } else {
                        System.out.println("Book not found in the library.");
                    }
                    break;

                case 2: // Return a book
                    System.out.print("Enter the title of the book to return: ");
                    tempBook = scanner.nextLine();
                    if (books.contains(tempBook)) {
                        int index = books.indexOf(tempBook);
                        tempStatus = availability.get(index);
                        if (!tempStatus) {
                            availability.set(index, true);
                            System.out.println("You returned " + tempBook);
                        } else {
                            System.out.println(tempBook + " was not borrowed.");
                        }
                    } else {
                        System.out.println("Book not found in the library.");
                    }
                    break;

                case 3: // View all books
                    System.out.println("\nLibrary Catalog:");
                    for (int i = 0; i < books.size(); i++) {
                        tempBook = books.get(i);
                        tempStatus = availability.get(i);
                        System.out.println(tempBook + " - " + (tempStatus ? "Available" : "Borrowed"));
                    }
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
        System.out.println("Library session ended. Thank you!");
    }
}
