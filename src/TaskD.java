import java.io.*;

public class TaskD {

    private static class Reader {

        private final  BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        public int readIntLine() throws IOException {
            return Integer.parseInt(reader.readLine());
        }
    }

    private static class Writer {

        private final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        public void writeIntLine(int val) throws IOException {
            writer.write(String.valueOf(val));
            writer.newLine();
            writer.flush();
        }


        public void writeLine(String line) throws IOException {
            writer.write(line);
            writer.flush();
        }
    }

    private static int guessCommit(Reader reader, Writer writer, int commitStart, int commitEnd) throws IOException {
        if (commitStart == commitEnd) {
            return commitStart;
        }
        int commitToCheck = commitStart + (commitEnd - commitStart) / 2;
        writer.writeIntLine(commitToCheck);
        boolean testFailed = reader.readIntLine() == 0;
        if (testFailed) {
            return guessCommit(reader, writer, commitStart, commitToCheck);
        } else {
            return guessCommit(reader, writer, commitToCheck + 1, commitEnd);
        }
    }

    public static void main(String[] args) throws IOException {
        Reader reader = new Reader();
        Writer writer = new Writer();

        int totalCommitsCount = reader.readIntLine();
        writer.writeLine(
                "! " + guessCommit(
                        reader,
                        writer,
                        1,
                        totalCommitsCount
                )
        );
    }
}
