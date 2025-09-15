import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Random;

public class Encrypt {

    public static int generateKey() {
        Random rnd = new Random();
        return rnd.nextInt(255) + 1;
    }

    public static int encrypt(String inFile) throws IOException {
        Path input = Paths.get(inFile);
        Path output = Paths.get("encrypted.txt");

        if (!Files.exists(input)) {
            System.err.println("Не удалось открыть файл " + inFile);
            return 1;
        }

        byte[] data = Files.readAllBytes(input);
        int key = generateKey();
        System.out.println("Случайный ключ: " + key);

        int n = 5, k = 3;
        List<int[]> shares = SssSen.shamirSplit(key, n, k);

        System.out.println("Секрет разделен на части (для n=" + n + ", k=" + k + "):");
        for (int[] sh : shares) {
            System.out.println("  (" + sh[0] + ", " + sh[1] + ")");
        }

        for (int i = 0; i < data.length; i++) {
            data[i] ^= key;
        }

        Files.write(output, data);
        System.out.println("Файл зашифрован и сохранен в " + output.toString());
        return 0;
    }
}

