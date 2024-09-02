import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.DigestInputStream;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.*;

public class LibraryManagementSystem {
    private static final String STUDENTS_CSV = "data/students.csv";
    private static final String BOOKS_CSV = "data/books.csv";
    private static final String LOANS_CSV = "data/loans.csv";
    private static final SecretKey SECRET_KEY = generateSecretKey();

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter your name:");
            String studentName = scanner.nextLine();
            if (!isValidInput(studentName)) {
                System.out.println("Invalid input. Please use alphanumeric characters only.");
                return;
            }
            storeStudentName(studentName);

            System.out.println("Available books:");
            List<String> books = getAvailableBooks();
            for (int i = 0; i < books.size(); i++) {
                System.out.println((i + 1) + ". " + books.get(i));
            }

            System.out.println("Enter the number of the book you want to loan:");
            int bookChoice = scanner.nextInt();
            if (bookChoice < 1 || bookChoice > books.size()) {
                System.out.println("Invalid book choice.");
                return;
            }
            String bookName = books.get(bookChoice - 1);

            storeLoanDetails(studentName, bookName);
            System.out.println("Book loaned successfully!");
        } catch (Exception e) {
            logError("Error occurred", e);
        }
    }

    private static boolean isValidInput(String input) {
        // Allow only alphanumeric characters and spaces
        return input.matches("[a-zA-Z0-9 ]+");
    }

    private static void storeStudentName(String studentName) {
        try {
            String encryptedName = encrypt(studentName);
            try (FileWriter fw = new FileWriter(STUDENTS_CSV, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                out.println(encryptedName);
            }
            setFilePermissions(STUDENTS_CSV);
            generateChecksum(STUDENTS_CSV);
        } catch (IOException | NoSuchAlgorithmException e) {
            logError("Error storing student name", e);
        }
    }

    private static List<String> getAvailableBooks() {
        List<String> books = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(BOOKS_CSV))) {
            String line;
            while ((line = br.readLine()) != null) {
                books.add(line);
            }
        } catch (IOException e) {
            logError("Error reading available books", e);
        }
        return books;
    }

    private static void storeLoanDetails(String studentName, String bookName) {
        try {
            String encryptedStudentName = encrypt(studentName);
            String encryptedBookName = encrypt(bookName);
            try (FileWriter fw = new FileWriter(LOANS_CSV, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String timestamp = sdf.format(new Date());
                out.println(encryptedStudentName + "," + encryptedBookName + "," + timestamp);
            }
            setFilePermissions(LOANS_CSV);
            generateChecksum(LOANS_CSV);
        } catch (IOException | NoSuchAlgorithmException e) {
            logError("Error storing loan details", e);
        }
    }

    private static void setFilePermissions(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            file.setReadable(true, false);
            file.setWritable(true, false);
            file.setExecutable(false, false);
        }
    }

    private static void generateChecksum(String filePath) throws IOException, NoSuchAlgorithmException {
        File file = new File(filePath);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream fis = new FileInputStream(file);
                DigestInputStream dis = new DigestInputStream(fis, digest)) {
            byte[] buffer = new byte[1024];
            while (dis.read(buffer) != -1) {
                // Reading file
            }
        }
        byte[] checksumBytes = digest.digest();
        String checksum = Base64.getEncoder().encodeToString(checksumBytes);
        logInfo("Checksum for " + filePath + ": " + checksum);
    }

    private static String encrypt(String plainText) throws NoSuchAlgorithmException {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new NoSuchAlgorithmException("Error encrypting data", e);
        }
    }

    private static String decrypt(String encryptedText) throws NoSuchAlgorithmException {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new NoSuchAlgorithmException("Error decrypting data", e);
        }
    }

    private static void logInfo(String message) {
        // Implement logging to a file or console
        System.out.println("INFO: " + message);
    }

    private static void logError(String message, Throwable t) {
        // Implement logging to a file or console
        System.err.println("ERROR: " + message);
        t.printStackTrace();
    }

    private static SecretKey generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128); // AES key size
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating secret key", e);
        }
    }
}
