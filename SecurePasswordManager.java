import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SecurePasswordManager {
    private Map<String, byte[]> passwordMap;
    private SecretKey secretKey;

    public SecurePasswordManager() {
        passwordMap = new HashMap<>();
        generateSecretKey();
    }

    private void generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            secretKey = keyGen.generateKey();
        } catch (Exception e) {
            System.out.println("Error generating secret key: " + e.getMessage());
        }
    }

    public void addPassword(String username, String password) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedPassword = cipher.doFinal(password.getBytes());
            passwordMap.put(username, encryptedPassword);
        } catch (Exception e) {
            System.out.println("Error adding password: " + e.getMessage());
        }
    }

    public String getPassword(String username) {
        try {
            if (passwordMap.containsKey(username)) {
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] decryptedPassword = cipher.doFinal(passwordMap.get(username));
                return new String(decryptedPassword);
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error getting password: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        SecurePasswordManager passwordManager = new SecurePasswordManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Add password");
            System.out.println("2. Get password");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.next();
                    System.out.print("Enter password: ");
                    String password = scanner.next();
                    passwordManager.addPassword(username, password);
                    break;
                case 2:
                    System.out.print("Enter username: ");
                    username = scanner.next();
                    String retrievedPassword = passwordManager.getPassword(username);
                    if (retrievedPassword != null) {
                        System.out.println("Password: " + retrievedPassword);
                    } else {
                        System.out.println("Password not found");
                    }
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }
}