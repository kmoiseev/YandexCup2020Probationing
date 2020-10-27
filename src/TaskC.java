import java.io.*;

public class TaskC {

    private static class LineReader {
        private final BufferedReader reader;

        private LineReader(Reader reader) {
            this.reader = new BufferedReader(reader);
        }

        private String readLine() throws IOException {
            return reader.readLine();
        }

        private int readNextShort() throws IOException {
            int res = 0;
            while (true) {
                int b = reader.read();
                if (b == ' ' || b == '\n' || b == -1) {
                    break;
                }
                if (b == '\r') {
                    continue;
                }
                res = Character.getNumericValue(b) + res * 10;
            }
            return res;
        }

        private void close() throws IOException {
            reader.close();
        }
    }

    private static class LineReaderConsole extends LineReader {

        private LineReaderConsole() {
            super(new InputStreamReader(System.in));
        }
    }

    private static class LineReaderFile extends LineReader {

        private LineReaderFile(String fileName) throws FileNotFoundException {
            super(new InputStreamReader(new FileInputStream(fileName)));
        }
    }

    public static void main(String[] args) throws IOException {
        startCapturingTime();

        LineReader reader = new LineReaderConsole();
        //LineReader reader = new LineReaderFile("resources/task_c_super_simple.txt");
        //LineReader reader = new LineReaderFile("resources/task_c_simple.txt");
        //LineReader reader = new LineReaderFile("resources/task_c.txt");
        String[] input = reader.readLine().split(" ");
        int winCount = Integer.parseInt(input[0]);
        int cardsCount = Integer.parseInt(input[1]);

        logMemory("initial");

        byte[] score_per_number = new byte[1000];
        for (int i = 0 ; i < 1000 ; ++i) {
            int number = i + 1;
            boolean divides_by_3 = number % 3 == 0;
            boolean divides_by_5 = number % 5 == 0;
            score_per_number[i] = (byte)(divides_by_3 && divides_by_5 ? 0 : (divides_by_3 ? -1 : divides_by_5 ? 1 : 0));
        }

        logMemory("after making map");

        int vasya = 0;
        int petya = 0;
        for (int i = 0; i < cardsCount; ++i) {
            int card = reader.readNextShort();
            if (score_per_number[card - 1] > 0) {
                vasya++;
            } else if (score_per_number[card - 1] < 0) {
                petya++;
            }

            if (vasya == winCount || petya == winCount) {
                break;
            }
        }
        reader.close();


        System.out.println(
                vasya > petya ? "Vasya" : vasya == petya ? "Draw" : "Petya"
        );

        logMemory("finally");
        logCapturingTime();
    }

    private static final Boolean logEnabled = Boolean.FALSE;

    private static void logMemory(String prefix) {
        if (!logEnabled) {
            return;
        }
        System.out.println(
                prefix + ". MBytes taken : " +
                        ((float) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())) / 1024 / 1024
        );
    }

    private static long timeMeasuringStarted;
    private static void startCapturingTime() {
        timeMeasuringStarted = System.currentTimeMillis();
    }

    private static void logCapturingTime() {
        if (!logEnabled) {
            return;
        }
        System.out.println("Time taken = " + (System.currentTimeMillis() - timeMeasuringStarted) / 1000.0 + " secs ");
    }
}
