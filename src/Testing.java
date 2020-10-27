import java.util.ArrayList;

public class Testing {
    public static void main(String[] args)  {
        ArrayList<Object> test = new ArrayList<>();
        for (int i = 0 ; i < 1000000; ++i) {
            if (i % 1000 == 0) {
                test.ensureCapacity(i % 1000 + 1000);
            }
            test.add(i,true);
        }
        System.out.println(
                "MBytes taken : " +
                        ((float)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())) / 1024 / 1024
        );
    }
}
