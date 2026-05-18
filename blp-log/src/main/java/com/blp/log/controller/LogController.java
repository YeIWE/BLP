package com.blp.log.controller;

import com.blp.common.result.ApiResult;
import com.blp.common.result.PageResult;
import com.blp.log.entity.UserLog;
import com.blp.log.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "日志管理")
@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @Operation(summary = "分页查询日志")
    @GetMapping("/page")
    public ApiResult<PageResult<UserLog>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long userId) {
        Page<UserLog> result = logService.pageLogs(page, size, userId);
        return ApiResult.ok(PageResult.of(result.getTotalElements(), page, size, result.getContent()));
    }
}
