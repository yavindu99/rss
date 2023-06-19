package com.yavindu.rss.service;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.yavindu.rss.FeedRepository;
import com.yavindu.rss.model.Feed;
import com.yavindu.rss.model.FeedEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RSSService {

    private FeedRepository feedRepository;

    @Value("${rss.read.url}")
    private String rssUrl;

    @Scheduled(cron = "${rss.read.at}")
    public void read() {
        log.info("Retrieving entries");

        try {
            List<FeedEntry> feedEntries= feedRead();
            log.info("Retrieved entries {}", feedEntries.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<FeedEntry> feedRead() throws IOException, FeedException {
        try (XmlReader reader = new XmlReader(new URL(rssUrl))) {
            SyndFeed syndFeed = new SyndFeedInput().build(reader);

            Feed feed = Feed.builder()
                    .title(syndFeed.getTitle())
                    .build();

            List<FeedEntry> entries = syndFeed.getEntries().stream().map(syndEntry -> FeedEntry.builder()
                    .title(syndEntry.getTitle())
                    .postedOn(syndEntry.getPublishedDate())
                    .feed(feed)
                    .build()).collect(Collectors.toList());



            return feedRepository.saveAll(entries);
        }
    }

    public List<FeedEntry> listLatest(int limit) {
        log.info("List latest {}", limit);

        return feedRepository.findByOrderByPostedOnDescLimit(limit);
    }

    public Page<FeedEntry> list(Pageable pageable) {
        log.info("List all {}", pageable);

        return feedRepository.findAll(pageable);
    }

}
