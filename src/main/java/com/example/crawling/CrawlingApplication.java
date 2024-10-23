package com.example.crawling;

import java.util.Set;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrawlingApplication
{
	//private List<String> visitedLinks = new ArrayList<>();
	private Set<String> visitedLinks =  new HashSet<String>();

	public static void main(String[] args)
	{
		CrawlingService crawlingService = new CrawlingService();
		crawlingService.doCrawling(0, 10, "https://www.khan.co.kr/");


		SpringApplication.run(CrawlingApplication.class, args);

		// System.out.println("Application start");
		
		// Arrays.stream(args).forEach(it ->{
		// 	System.out.println(it);
		// });

//		String startUrl = args[0].endsWith("/") ? args[0].substring(0, args[0].length() - 1) : args[0];
		String startUrl = "https://www.khan.co.kr/";
		int depth = 10;

		try {
			// depth = Integer.parseInt(args[1]);

			CrawlingApplication crawler = new CrawlingApplication();
			crawler.crawl(startUrl, depth, 0);

		} catch (NumberFormatException e) {
			System.out.println("depth는 숫자만 가능");
		}

		// System.out.println(startUrl);

		System.exit(0);
	}

	public void crawl(String url, int maxDepth, int currentDepth) {
		if (currentDepth > maxDepth || !visitedLinks.add(url)) {
			return;
		}

		System.out.println("현재 dept["+currentDepth+"] "+getIndentation(currentDepth) + url);

		try {
			Document document = Jsoup.connect(url).get();
			Elements links = document.select("a[href]");

			// for(Element link : links){
			// 	String nextLink = link.absUrl("href");
			// 	if (nextLink.startsWith("http")) {
			// 		crawl(nextLink, maxDepth, currentDepth + 1);
			// 	}
			// }

            for (Element link : links) {
                String nextLink = link.absUrl("href");
                // nextLink의 맨 뒤에 슬래시가 있으면 제거
                if (nextLink.endsWith("/")) {
                    nextLink = nextLink.substring(0, nextLink.length() - 1);
                }
                if (nextLink.startsWith("http")) {
                    crawl(nextLink, maxDepth, currentDepth + 1);
                }
            }

		} 
		catch (Exception e) {
			System.out.println(getIndentation(currentDepth) + "예외 : " + e.getMessage());
		}
	}

    private String getIndentation(int depth) {
        return " ".repeat(depth * 4); // 깊이에 맞춰 공백 추가
    }

	

}


