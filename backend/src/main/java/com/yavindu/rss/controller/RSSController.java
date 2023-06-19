package com.yavindu.rss.controller;

import com.yavindu.rss.common.SortDir;
import com.yavindu.rss.model.Feed;
import com.yavindu.rss.model.FeedEntry;
import com.yavindu.rss.service.RSSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
@Slf4j
public class RSSController {

    private final RSSService rssService;

    @GetMapping("/latest/{}")
    public ResponseEntity<List<FeedEntry>> latest(@PathVariable int limit){
        List<FeedEntry> feeds = rssService.listLatest(limit);

        return new ResponseEntity<>(feeds, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<FeedEntry>> list(@RequestParam("page") int page,
                                           @RequestParam("size") int size,
                                           @RequestParam("sort") String field,
                                           @RequestParam("direction") SortDir dir){

        Sort sort = Sort.by(field).descending();
        if(SortDir.ASC.equals(dir)){
            Sort.by(field).ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<FeedEntry> feeds = rssService.list(pageable);

        return new ResponseEntity<>(feeds, HttpStatus.OK);
    }
}
