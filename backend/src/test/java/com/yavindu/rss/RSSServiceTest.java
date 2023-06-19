package com.yavindu.rss;

import com.rometools.rome.io.FeedException;
import com.yavindu.rss.model.FeedEntry;
import com.yavindu.rss.repository.FeedRepository;
import com.yavindu.rss.service.RSSService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RSSServiceTest {

    @Autowired
    private RSSService rssService;

    @MockBean
    private FeedRepository feedRepository;

    @Test
    void read() throws FeedException, IOException {

        List<FeedEntry> feedEntries = rssService.feedRead();
        Assertions.assertTrue(feedEntries.size() > 0);
    }

    @Test
    void latest() {

        List<FeedEntry> feedEntries = rssService.listLatest(10);
        Assertions.assertEquals(10, feedEntries.size());
    }

}
