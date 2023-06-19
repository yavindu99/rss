package com.yavindu.rss;

import com.yavindu.rss.model.Feed;
import com.yavindu.rss.model.FeedEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<FeedEntry, Integer> {

    List<FeedEntry> findByOrderByPostedOnDescLimit(int limit);

}
