
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskA {
    public static void main(String[] args) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        String gems = r.readLine();
        String stones = r.readLine();

        Set<Integer> gemsSet = gems.codePoints().boxed().collect(Collectors.toSet());

        System.out.println(
                stones.codePoints().filter(gemsSet::contains).count()
        );
    }
}