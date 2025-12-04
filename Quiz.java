//Ayan Bhatt
//Rahul Joshi
// 12/4/2025
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Quiz {

    private static final String ALICE_URL = "https://www.gutenberg.org/cache/epub/11/pg11.txt";
    private static final String SHERLOCK_URL = "https://www.gutenberg.org/cache/epub/1661/pg1661.txt";

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Map<String, Integer>> aliceTask = new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() {
                return processBook(ALICE_URL);
            }
        };

        Callable<Map<String, Integer>> sherlockTask = new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() {
                return processBook(SHERLOCK_URL);
            }
        };

        Future<Map<String, Integer>> aliceFuture = executor.submit(aliceTask);
        Future<Map<String, Integer>> sherlockFuture = executor.submit(sherlockTask);

        try {
            Map<String, Integer> aliceWordCount = aliceFuture.get();
            Map<String, Integer> sherlockWordCount = sherlockFuture.get();

            long end = System.currentTimeMillis();

            System.out.println("Alice word count: " + totalWords(aliceWordCount));
            System.out.println("Sherlock word count: " + totalWords(sherlockWordCount));
            System.out.println("Total time (ms): " + (end - start));

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    private static Map<String, Integer> processBook(String urlString) {
        Map<String, Integer> wordMap = new HashMap<>();

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(new URL(urlString).openStream()))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String modifiedLine = modifyText(line);
                String[] words = modifiedLine.split("\\s+");

                for (String w : words) {
                    if (w.isEmpty()) continue;
                    wordMap.put(w, wordMap.getOrDefault(w, 0) + 1);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return wordMap;
    }

    private static String modifyText(String text) {
        return text.toUpperCase().replaceAll("(?m)^\\s*$", "");
    }

    private static int totalWords(Map<String, Integer> map) {
        int sum = 0;
        for (int count : map.values()) {
            sum += count;
        }
        return sum;
    }
}
