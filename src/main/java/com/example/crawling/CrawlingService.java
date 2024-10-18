package com.example.crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class CrawlingService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getCanonicalName());
    private final Set<String> uniqueURLSet = new ConcurrentSkipListSet<>();

    public void doCrawling(final int startCount, final int maxCount, final String url)
    {
        try
        {
            if ((uniqueURLSet.add(url)) && (startCount < maxCount))
            {
                logger.info("[Level {}]{}{}", startCount , addSpace(startCount), url);

                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select("a[href]");

                links.stream()
                        .map(link -> link.attr("abs:href"))
                        .filter(it ->  it.startsWith("http"))
                        .filter(it -> ! it.endsWith("png"))
                        .filter(it -> ! it.endsWith("jpg"))
                        .forEach(it -> this.doCrawling(startCount + 1, maxCount, it));
            }
        }
        catch (IOException e) {
            logger.error("this url {} is wrong", url, e);
        }
    }

    private String addSpace(int startCount)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        for (int i = 0; i < startCount * 4; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}