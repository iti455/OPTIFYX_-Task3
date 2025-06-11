import java.util.*;

/* ----------  Book POJO ------------- */
class Book {
    String title;
    String author;
    boolean isIssued;

    Book(String title, String author) {
        this.title   = title;
        this.author  = author;
        this.isIssued = false;
    }
}

/* ----------  Digital Library ------------- */
public class DigitalLibrary {

    private static final ArrayList<Book> library = new ArrayList<>();
    private static final Scanner sc = new Scanner(System.in);

    /* ---- Add a new book ---- */
    private static void addBook() {
        System.out.print("Enter book title : ");
        String title = sc.nextLine().trim();
        System.out.print("Enter author name: ");
        String author = sc.nextLine().trim();

        if (title.isEmpty() || author.isEmpty()) {
            System.out.println("❌ Title and author cannot be empty.");
            return;
        }
        library.add(new Book(title, author));
        System.out.println("✅ Book added.\n");
    }

    /* ---- Issue a book ---- */
    private static void issueBook() {
        if (library.isEmpty()) {
            System.out.println("❌ No books in library to issue.\n");
            return;
        }

        System.out.print("Enter book title to issue: ");
        String title = sc.nextLine().trim();

        for (Book book : library) {
            if (book.title.equalsIgnoreCase(title)) {
                if (!book.isIssued) {
                    book.isIssued = true;
                    System.out.println("✅ Book issued.\n");
                } else {
                    System.out.println("❌ Book is already issued.\n");
                }
                return;
            }
        }
        System.out.println("❌ Book not found.\n");
    }

    /* ---- Return a book ---- */
    private static void returnBook() {
        if (library.isEmpty()) {
            System.out.println("❌ No books in library to return.\n");
            return;
        }

        System.out.print("Enter book title to return: ");
        String title = sc.nextLine().trim();

        for (Book book : library) {
            if (book.title.equalsIgnoreCase(title)) {
                if (book.isIssued) {
                    book.isIssued = false;
                    System.out.println("✅ Book returned.\n");
                } else {
                    System.out.println("❌ This book wasn’t issued.\n");
                }
                return;
            }
        }
        System.out.println("❌ Book not found.\n");
    }

    /* ---- View all books ---- */
    private static void viewBooks() {
        System.out.println("\n📚 Library Books:");
        if (library.isEmpty()) {
            System.out.println("No books available.\n");
            return;
        }
        for (Book book : library) {
            System.out.println("- " + book.title + " by " + book.author +
                               " — " + (book.isIssued ? "Issued" : "Available"));
        }
        System.out.println(); // blank line for neatness
    }

    /* ----------  Main menu loop ------------- */
    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("=== DIGITAL LIBRARY MENU ===");
            System.out.println("1. Add Book");
            System.out.println("2. Issue Book");
            System.out.println("3. Return Book");
            System.out.println("4. View Books");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            /* input validation for menu option */
            while (!sc.hasNextInt()) {
                System.out.println("❌ Please enter a number 1-5.");
                sc.next(); // discard invalid token
            }
            choice = sc.nextInt();
            sc.nextLine();           // clear the newline

            switch (choice) {
                case 1 -> addBook();
                case 2 -> issueBook();
                case 3 -> returnBook();
                case 4 -> viewBooks();
                case 5 -> System.out.println("👋 Exiting... Goodbye!");
                default -> System.out.println("❌ Invalid choice.\n");
            }
        } while (choice != 5);

        sc.close();
    }
}
