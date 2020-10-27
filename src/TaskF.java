import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class TaskF {

    private static class Reader {
        private final BufferedReader reader;

        private Reader(java.io.Reader reader) {
            this.reader = new BufferedReader(reader);
        }

        private String readLine() throws IOException {
            return reader.readLine();
        }

        private void close() throws IOException {
            reader.close();
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

    private static class ReaderConsole extends Reader {

        private ReaderConsole() {
            super(new InputStreamReader(System.in));
        }
    }

    public static void main(String[] args) throws IOException, ParseException {
        Reader reader = new ReaderConsole();
        Writer writer = new Writer();

        String urlString = reader.readLine();
        String port = reader.readLine();
        String reqParamA = reader.readLine();
        String reqParamB = reader.readLine();

        URL url = new URL(urlString + ":" + port + "/?a=" + reqParamA + "&b=" + reqParamB);
        URLConnection urlConnection = url.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        urlConnection.getInputStream()
                )
        );

        JSONArray arr = (JSONArray) (new JSONParser().parse(in));
        in.close();

        writer.writeLine(
                String.valueOf(arr.stream().mapToLong(s -> (Long) s).sum())
        );

        writer.flush();
    }

}
