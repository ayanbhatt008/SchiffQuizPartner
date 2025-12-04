//Ayan Bhatt
//Rahul Joshi
// 12/4/2025

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class Quiz {
	public static void main(String[] args) {
		String book1Url = "https://www.gutenberg.org/cache/epub/11/pg11.txt";   // alic wonderland
		String book2Url = "https://www.gutenberg.org/cache/epub/1661/pg1661.txt"; // sherlock holmes
		long start = System.currentTimeMillis();
		try {

			HttpClient client = HttpClient.newHttpClient();


			String book1 = downloadText(client, book1Url);
			String book2 = downloadText(client, book2Url);

			String modified1 = modifyText(book1);
		            String modified2 = modifyText(book2);


			Files.writeString(Path.of("book1_modified.txt"), modified1);
			Files.writeString(Path.of("book2_modified.txt"), modified2);

			int wc1 = countWords(book1);
			int wc2 = countWords(book2);

			System.out.println("Book 1 word count: " + wc1);
System.out.println("Book 2 word count: " + wc2);

			System.out.println("time: "  + (System.currentTimeMillis() - start) + " ms");

		} catch (Exception e) {
			e.printStackTrace();
        }
	}

	private static String downloadText(HttpClient client, String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


	private static String modifyText(String text) {
        // Example "modification": uppercase + remove blank lines
        return text.toUpperCase().replaceAll("(?m)^\\s*$", "");
    }

	private static int countWords(String text) {
	    if (text == null || text.isEmpty()) return 0;
	    return text.trim().split("\\s+").length;
	}

}