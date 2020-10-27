import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Supplier;

public class ResFileFilling {

    private static final int thousand = 1000;

    private static final int million = 1000000;

    private static final int billion = 1000000000;
    private static final Supplier<Integer> randomPositiveBelowBillion = () -> (int) (Math.random() * billion);

    public static void main(String[] args) throws IOException {
        //taskB();
        taskE();
    }

    private static void taskB() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("resources/task_b.txt"));

        int alarmsCount = (int) (Math.random() * 2000) + 8000;
        int period = 1 + randomPositiveBelowBillion.get();
        int required = 1 + randomPositiveBelowBillion.get();

        writer.write(alarmsCount + " " + period + " " + required);
        writer.newLine();
        for (int i = 0; i < alarmsCount; ++i) {
            writer.write((randomPositiveBelowBillion.get() + 1) + " ");
        }
        writer.newLine();
    }

    private static void taskE() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("resources/task_e.txt"));
        writer.write(String.valueOf(million));
        writer.newLine();
        int[] servers = new int[million];
        for (int i = 0; i < million; ++i) {
            int left = servers[randomPositiveBelowBillion.get()];
            int right = servers[randomPositiveBelowBillion.get()];
            while (left == right) {
                left = servers[randomPositiveBelowBillion.get()];
                right = servers[randomPositiveBelowBillion.get()];
            }
            writer.write(servers[randomPositiveBelowBillion.get()] + " " + servers[randomPositiveBelowBillion.get()]);
            writer.newLine();
        }

        writer.write(String.valueOf(thousand));
        for (int i = 0 ; i < thousand; ++i) {

        }
    }
}
