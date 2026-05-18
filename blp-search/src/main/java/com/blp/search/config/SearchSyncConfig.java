package com.blp.search.config;

import com.blp.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchSyncConfig implements ApplicationRunner {

    private final SearchService searchService;

    @Override
    public void run(ApplicationArguments args) {
        try {
            searchService.fullSync();
        } catch (Exception e) {
            log.warn("Initial ES sync failed on startup: {}", e.getMessage());
        }
    }
}
