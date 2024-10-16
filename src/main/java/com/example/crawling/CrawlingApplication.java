package com.example.crawling;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrawlingApplication {

	private List<String> visitedLinks = new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(CrawlingApplication.class, args);

		System.out.println("Application start");
		
		Arrays.stream(args).forEach(it ->{
			System.out.println(it);
		});

		String startUrl = args[0];
		int depth;

		try {
			depth = Integer.parseInt(args[1]);

			CrawlingApplication crawler = new CrawlingApplication();
			crawler.crawl(startUrl, depth, 0);

		} catch (NumberFormatException e) {
			System.out.println("depth는 숫자만 가능");
		}

		//System.out.println(startUrl);

		System.exit(0);
	}

	public void crawl(String url, int maxDepth, int currentDepth) {
		if (currentDepth > maxDepth || visitedLinks.contains(url)) {
			return;
		}

		visitedLinks.add(url);
		System.out.println(getIndentation(currentDepth) + url);

		try {
			Document document = Jsoup.connect(url).get();
			Elements links = document.select("li a[href]");

			for(Element link : links){
				String nextLink = link.absUrl("href");
				if (nextLink.startsWith("http")) {
					System.out.println(getIndentation(currentDepth + 1) + nextLink); // Print the link with indentation
					crawl(nextLink, maxDepth, currentDepth + 1);
				}
			}
		} catch (Exception e) {
			System.out.println(getIndentation(currentDepth) + "예외");
		}
	}

    private String getIndentation(int depth) {
        return " ".repeat(depth * 4); // 깊이에 맞춰 공백 추가
    }

	

}


