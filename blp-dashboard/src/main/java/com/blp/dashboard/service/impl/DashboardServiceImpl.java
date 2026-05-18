package com.blp.dashboard.service.impl;

import com.blp.dashboard.mapper.DashboardMapper;
import com.blp.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardMapper dashboardMapper;

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("todayOrderCount", dashboardMapper.todayOrderCount());
        stats.put("todayRevenue", dashboardMapper.todayRevenue());
        stats.put("productCount", dashboardMapper.productCount());
        stats.put("userCount", dashboardMapper.userCount());
        return stats;
    }
}
