package com.blp.user.controller;

import com.blp.common.result.ApiResult;
import com.blp.user.entity.SysMenu;
import com.blp.user.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/user/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService sysMenuService;

    @Operation(summary = "菜单树")
    @GetMapping("/tree")
    public ApiResult<List<SysMenu>> tree() {
        return ApiResult.ok(sysMenuService.getAllMenus());
    }

    @Operation(summary = "用户菜单(动态菜单)")
    @GetMapping("/user/{userId}")
    public ApiResult<List<SysMenu>> userMenus(@PathVariable Long userId) {
        return ApiResult.ok(sysMenuService.getUserMenus(userId));
    }
}
