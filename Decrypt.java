import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Decrypt {

    public static boolean decrypt(String inFile) throws IOException {
        Path input = Paths.get("encrypted.txt");
        Path output = Paths.get("decrypted.txt");

        if (!Files.exists(input)) {
            System.err.println("Не удалось открыть " + input);
            return false;
        }

        Scanner sc = new Scanner(System.in);
        List<int[]> shares = new ArrayList<>();
        System.out.println("Введите 3 или более частей (x y):");
        for (int i = 0; i < 3; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            shares.add(new int[]{x, y});
        }

        int key = SssSen.shamirCombine(shares);
        System.out.println("Восстановленный ключ: " + key);

        byte[] data = Files.readAllBytes(input);
        for (int i = 0; i < data.length; i++) {
            data[i] ^= key;
        }

        Files.write(output, data);
        System.out.println("Файл расшифрован и сохранён в " + output.toString());
        return true;
    }
}

