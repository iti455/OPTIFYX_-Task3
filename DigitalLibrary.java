import java.io.*;
import java.util.*;
import java.text.*;

/* ----------  Book POJO ------------- */
class Book implements Serializable {
    String title;
    String author;
    boolean isIssued;
    Date issuedDate;
    int issuePeriod;

    Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isIssued = false;
        this.issuedDate = null;
        this.issuePeriod = 0;
    }
}

/* ----------  Digital Library Main ------------- */
public class DigitalLibrary {
    private static final Scanner sc = new Scanner(System.in);
    private static final String FILE_NAME = "library_data.ser";
    private static ArrayList<Book> library = new ArrayList<>();

    /* ---- Load books from file ---- */
    @SuppressWarnings("unchecked")
    private static void loadLibrary() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            library = (ArrayList<Book>) ois.readObject();
        } catch (Exception e) {
            library = new ArrayList<>();
        }
    }

    /* ---- Save books to file ---- */
    private static void saveLibrary() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(library);
        } catch (IOException e) {
            System.out.println("❌ Failed to save library.");
        }
    }

    /* ---- Add a new book ---- */
    private static void addBook() {
        System.out.print("Enter book title : ");
        String title = sc.nextLine().trim();
        System.out.print("Enter author name: ");
        String author = sc.nextLine().trim();

        if (title.isEmpty() || author.isEmpty()) {
            System.out.println("❌ Title and author cannot be empty.\n");
            return;
        }
        library.add(new Book(title, author));
        saveLibrary();
        System.out.println("✅ Book added.\n");
    }

    /* ---- Issue a book ---- */
    private static void issueBook() {
        System.out.print("Enter book title to issue: ");
        String title = sc.nextLine().trim();
        for (Book book : library) {
            if (book.title.equalsIgnoreCase(title)) {
                if (!book.isIssued) {
                    System.out.print("Enter issue period (in days): ");
                    int period = Integer.parseInt(sc.nextLine());
                    book.isIssued = true;
                    book.issuedDate = new Date();
                    book.issuePeriod = period;
                    saveLibrary();
                    System.out.println("✅ Book issued.\n");
                } else {
                    System.out.println("❌ Book already issued.\n");
                }
                return;
            }
        }
        System.out.println("❌ Book not found.\n");
    }

    /* ---- Return a book (with fine) ---- */
    private static void returnBook() {
        System.out.print("Enter book title to return: ");
        String title = sc.nextLine().trim();
        for (Book book : library) {
            if (book.title.equalsIgnoreCase(title)) {
                if (book.isIssued) {
                    book.isIssued = false;
                    Date today = new Date();
                    long diff = today.getTime() - book.issuedDate.getTime();
                    long daysUsed = diff / (1000 * 60 * 60 * 24);
                    int overdueDays = (int)(daysUsed - book.issuePeriod);
                    if (overdueDays > 0) {
                        int fine = overdueDays * 5; // ₹5 per day
                        System.out.println("⚠️ Overdue by " + overdueDays + " days. Fine: ₹" + fine);
                    } else {
                        System.out.println("✅ Book returned on time.");
                    }
                    book.issuedDate = null;
                    book.issuePeriod = 0;
                    saveLibrary();
                } else {
                    System.out.println("❌ This book wasn't issued.\n");
                }
                return;
            }
        }
        System.out.println("❌ Book not found.\n");
    }

    /* ---- Advance Booking ---- */
    private static void reserveBook() {
        System.out.print("Enter book title to reserve: ");
        String title = sc.nextLine().trim();
        for (Book book : library) {
            if (book.title.equalsIgnoreCase(title)) {
                if (book.isIssued) {
                    System.out.println("🔒 Book is currently issued. Your advance booking is noted.");
                } else {
                    System.out.println("✅ Book is available. You can issue it directly.");
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for (Book book : library) {
            String status = book.isIssued ?
                    "Issued (Due: " + sdf.format(new Date(book.issuedDate.getTime() + book.issuePeriod * 86400000L)) + ")"
                    : "Available";
            System.out.println("- " + book.title + " by " + book.author + " — " + status);
        }
        System.out.println();
    }

    /* ---- Report Generation ---- */
    private static void report() {
        System.out.println("\n📊 Report of Issued Books:");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        boolean found = false;
        for (Book book : library) {
            if (book.isIssued) {
                found = true;
                System.out.println(book.title + " by " + book.author +
                        " | Issued on: " + sdf.format(book.issuedDate) +
                        " | Period: " + book.issuePeriod + " days");
            }
        }
        if (!found) System.out.println("No books currently issued.");
        System.out.println();
    }

    /* ----------  Main Menu Loop ------------- */
    public static void main(String[] args) {
        loadLibrary();
        int choice;
        do {
            System.out.println("=== DIGITAL LIBRARY MENU ===");
            System.out.println("1. Add Book");
            System.out.println("2. Issue Book");
            System.out.println("3. Return Book");
            System.out.println("4. View Books");
            System.out.println("5. Reserve Book");
            System.out.println("6. Report");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("❌ Please enter a number 1-7.");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine(); // clear newline

            switch (choice) {
                case 1 -> addBook();
                case 2 -> issueBook();
                case 3 -> returnBook();
                case 4 -> viewBooks();
                case 5 -> reserveBook();
                case 6 -> report();
                case 7 -> System.out.println("👋 Exiting... Goodbye!");
                default -> System.out.println("❌ Invalid choice.\n");
            }
        } while (choice != 7);
        sc.close();
    }
}
