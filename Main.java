import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void showUsage() {
        System.err.println("Usage:");
        System.err.println("  java Main e <file_path>");
        System.err.println("  java Main d <file_path>");
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            showUsage();
            return;
        }

        char mode = args[0].charAt(0);
        String filePath = args[1];

        if (mode == 'e') {
            int res = Encrypt.encrypt(filePath);
            if (res != 0) {
                System.err.println("Error in generation: " + filePath);
            }
        } else if (mode == 'd') {
            boolean ok = Decrypt.decrypt(filePath);
            if (!ok) {
                System.err.println("Wrong password. Please, try again");
            }
        } else {
            System.err.println("Unknown mode: " + mode + ". Use 'e' for encrypt or 'd' for decrypt.");
        }
    }
}

