package com.blp.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blp.common.result.ApiResult;
import com.blp.common.result.PageResult;
import com.blp.user.entity.SysUser;
import com.blp.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;

    @Operation(summary = "分页查询用户")
    @GetMapping("/page")
    public ApiResult<PageResult<SysUser>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        IPage<SysUser> result = sysUserService.pageUsers(page, size, keyword);
        return ApiResult.ok(PageResult.of(result.getTotal(), page, size, result.getRecords()));
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public ApiResult<Void> create(@RequestBody SysUser user) {
        sysUserService.createUser(user);
        return ApiResult.ok();
    }

    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    public ApiResult<Void> update(@PathVariable Long id, @RequestBody SysUser user) {
        user.setId(id);
        sysUserService.updateUser(user);
        return ApiResult.ok();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        sysUserService.removeById(id);
        return ApiResult.ok();
    }
}
