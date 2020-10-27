import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TaskE {
    private static class Reader {
        private final BufferedReader reader;

        private Reader(java.io.Reader reader) {
            this.reader = new BufferedReader(reader);
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

    private static class ReaderConsole extends Reader {

        private ReaderConsole() {
            super(new InputStreamReader(System.in));
        }
    }

    private static class ReaderFile extends Reader {

        private ReaderFile(String fileName) throws FileNotFoundException {
            super(new InputStreamReader(new FileInputStream(fileName)));
        }
    }

    private static class Writer {

        private final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        public void writeLine(String line) throws IOException {
            writer.write(line);
            writer.newLine();
        }

        public void flush() throws IOException {
            writer.flush();
        }
    }

    private static class RelationsHolder {

        private final Map<Integer, Set<Integer>> clustersPerServerId = new HashMap<>();

        public void put(int left, int right) {
            Set<Integer> leftCluster = clustersPerServerId.get(left);
            Set<Integer> rightCluster = clustersPerServerId.get(right);

            // Ни один не имеет кластер
            if (leftCluster == null && rightCluster == null) {
                Set<Integer> newCluster = new HashSet<>();
                newCluster.add(left);
                newCluster.add(right);
                clustersPerServerId.put(left, newCluster);
                clustersPerServerId.put(right, newCluster);
                return;
            }
            // Только один имеет кластер
            if (leftCluster == null || rightCluster == null) {
                Set<Integer> nonEmptyCluster = leftCluster == null ? rightCluster : leftCluster;
                int noClusterServer = leftCluster == null ? left : right;
                nonEmptyCluster.add(noClusterServer);
                clustersPerServerId.put(noClusterServer, nonEmptyCluster);
                return;
            }
            // Оба в одном кластере
            if (leftCluster == rightCluster) {
                return;
            }

            // В разных кластерах
            {
                Set<Integer> biggerCluster = leftCluster.size() > rightCluster.size() ? leftCluster : rightCluster;
                Set<Integer> smallerCluster = biggerCluster == leftCluster ? rightCluster : leftCluster;

                biggerCluster.addAll(smallerCluster);
                clustersPerServerId.put(
                        smallerCluster == leftCluster ?
                                left : right,
                        biggerCluster
                );
            }
        }

        Set<Integer> getCluster(int serverId) {
            return clustersPerServerId.getOrDefault(serverId, Collections.emptySet());
        }
    }

    private static class Logger {
        private final Writer writer;
        private final boolean enabled;
        private Long timeCaptureStart;

        private Logger(Writer writer, boolean enabled) {
            this.writer = writer;
            this.enabled = enabled;
        }

        public void captureStartTime() {
            if (!enabled) {
                return;
            }
            this.timeCaptureStart = System.currentTimeMillis();
        }

        public void logEndTime() throws IOException {
            if (!enabled) {
                return;
            }
            log("Time taken: " + (System.currentTimeMillis() - timeCaptureStart) / 1000.0 + " secs");
        }

        public void logMemoryTaken(String prefix) throws IOException {
            if (!enabled) {
                return;
            }
            log(
                    prefix +
                            " . Memory utilization: " + ((float) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())) / 1024 / 1024 +
                            " MB"
            );
        }

        private void log(String msg) throws IOException {
            writer.writeLine(msg);
        }
    }

    public static void main(String[] args) throws IOException {
        Reader reader = new ReaderConsole();
        //Reader reader = new ReaderFile("resources/task_e.txt");
        //Reader reader = new ReaderFile("resources/task_e_simple.txt");
        //Reader reader = new ReaderFile("resources/task_e_super_simple.txt");
        Writer writer = new Writer();
        Logger logger = new Logger(writer, false);
        logger.captureStartTime();

        RelationsHolder relationsHolder = new RelationsHolder();
        int relationsCount = reader.readNextInt();
        for (int i = 0; i < relationsCount; ++i) {
            relationsHolder.put(
                    reader.readNextInt(),
                    reader.readNextInt()
            );
        }
        logger.logMemoryTaken("After filling relations");

        int filesToDetectCount = reader.readNextInt();
        for (int i = 0; i < filesToDetectCount; ++i) {
            int serverToUploadFile = reader.readNextInt();
            int amountOfServersContainingFile = reader.readNextInt();
            Set<Integer> cluster = relationsHolder.getCluster(serverToUploadFile);
            if (cluster.size() == 0) {
                writer.writeLine("0");
                continue;
            }
            List<Integer> serversContainingFile = IntStream.range(0, amountOfServersContainingFile)
                    .map((idx) -> {
                        try {
                            return reader.readNextInt();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }).
                    filter(
                            cluster::contains
                    ).boxed().collect(Collectors.toList());

            writer.writeLine(
                    serversContainingFile.size() + " " +
                            serversContainingFile.stream().map(Object::toString)
                                    .collect(Collectors.joining(" "))
            );
        }

        logger.logMemoryTaken("Finally");
        logger.logEndTime();

        writer.flush();
    }
}
