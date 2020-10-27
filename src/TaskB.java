import java.io.*;
import java.util.*;

public class TaskB {

    private static class LineReader {
        private final BufferedReader reader;

        private LineReader(Reader reader) {
            this.reader = new BufferedReader(reader);
        }

        private String readLine() throws IOException {
            return reader.readLine();
        }

        private int readNextInt() throws IOException {
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

    private static class AlarmPeriodHolder {
        private final byte[] arr;
        private final int capacity;
        private int currentAmount = 0;

        public AlarmPeriodHolder(int capacity) {
            arr = new byte[capacity / 8 + 1];
            this.capacity = capacity;
        }

        public void add(long number) {
            if (number >= capacity) {
                throw new IndexOutOfBoundsException();
            }
            if (check(number)) {
                return;
            }
            arr[(int)(number / 8)] |= 1 << (number % 8);
            currentAmount++;
        }

        // Получить индекс внутри периода для предыдущего аларма
        public long getPreviousCheckedInclusive(long periodIdx) {
            boolean nextByteMoved = false;
            byte maskIfNextByteNotMoved = Byte.MIN_VALUE;
            long bitIdx = periodIdx % 8;
            for (long i = 7; i >= 0; --i) {
                if (i > bitIdx) {
                    maskIfNextByteNotMoved &= ~(1 << i);
                } else {
                    maskIfNextByteNotMoved |= 1 << i;
                }
            }
            // Сначала проверяем текущий байт, за исключением более старших битов
            if ((arr[(int)(periodIdx / 8)] & maskIfNextByteNotMoved) == 0) {
                nextByteMoved = true;
                periodIdx -= 8;
                while (arr[(int)(periodIdx / 8)] == 0) {
                    periodIdx -= 8;
                }
            }

            for (long i = nextByteMoved ? 7 : periodIdx % 8 ; i >= 0; --i) {
                long periodIdxToCheck = periodIdx - periodIdx % 8 + i;
                if (check(periodIdxToCheck)) {
                    return periodIdxToCheck;
                }
            }

            throw new IllegalStateException();
        }

        private boolean check(long number) {
            if (number >= capacity) {
                return false;
            }
            return ((arr[(int)(number / 8)] >> (number % 8)) & 1) == 1;
        }

        public int getCurrentAmount() {
            return currentAmount;
        }
    }

    private static long getTimeOnceAlarmsReached(
            int alarmsRequired,
            long periodIdx,
            long alarmsPeriod,
            AlarmPeriodHolder alarmPeriodHolder
    ) {
        long time = periodIdx * alarmsPeriod;

        do {
            time--;
            long repeatingIdx = time % alarmsPeriod;
            long repeatingIdxPrev = alarmPeriodHolder.getPreviousCheckedInclusive(repeatingIdx);
            time -= repeatingIdx - repeatingIdxPrev;
            alarmsRequired ++;
        } while (alarmsRequired <= 0);

        return time;
    }

    public static void main(String[] args) throws IOException {
        startCapturingTime();

        LineReader reader = new LineReaderConsole();
        //LineReader reader = new LineReaderFile("resources/task_b_super_simple.txt");
        //LineReader reader = new LineReaderFile("resources/task_b_simple.txt");
        //LineReader reader = new LineReaderFile("resources/task_b.txt");
        String[] input = reader.readLine().split(" ");
        int alarmsCount = Integer.parseInt(input[0]);
        int alarmsPeriod = Integer.parseInt(input[1]);
        int alarmsRequired = Integer.parseInt(input[2]);

        Set<Integer> alarmsStartedAtTime = new TreeSet<>();
        logMemory("initial");

        for (int i = 0; i < alarmsCount; ++i) {
            int alarm = reader.readNextInt();
            alarmsStartedAtTime.add(alarm);
        }
        logMemory("after reading");
        reader.close();
        logMemory("after closing reader");

        AlarmPeriodHolder alarmPeriodHolder = new AlarmPeriodHolder(alarmsPeriod);
        long periodIdx = 0;
        long time = 0;
        for (Integer alarmStartTime : alarmsStartedAtTime) {
            long currentPeriodIdx = alarmStartTime / alarmsPeriod;
            if (currentPeriodIdx > periodIdx) {
                alarmsRequired -= alarmPeriodHolder.getCurrentAmount() * (currentPeriodIdx - periodIdx);
                if (alarmsRequired <= 0) {
                    time = getTimeOnceAlarmsReached(alarmsRequired, currentPeriodIdx, alarmsPeriod, alarmPeriodHolder);
                    break;
                }
            }
            long repeatingIdx = alarmStartTime % alarmsPeriod;
            alarmPeriodHolder.add(repeatingIdx);
            periodIdx = currentPeriodIdx;
        }

        if (alarmsRequired > 0) {
            long periodsLeft = alarmsRequired / alarmPeriodHolder.getCurrentAmount() +
                    (alarmsRequired % alarmPeriodHolder.getCurrentAmount() != 0 ? 1 : 0);
            periodIdx += periodsLeft;
            alarmsRequired -= alarmPeriodHolder.getCurrentAmount() * periodsLeft;
            time = getTimeOnceAlarmsReached(alarmsRequired, periodIdx, alarmsPeriod, alarmPeriodHolder);
        }

        logMemory("finally");

        System.out.println(
                time
        );

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
