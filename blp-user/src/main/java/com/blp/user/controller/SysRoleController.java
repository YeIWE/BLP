package com.blp.user.controller;

import com.blp.common.result.ApiResult;
import com.blp.user.entity.SysRole;
import com.blp.user.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/user/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService sysRoleService;

    @Operation(summary = "角色列表")
    @GetMapping
    public ApiResult<List<SysRole>> list() {
        return ApiResult.ok(sysRoleService.list());
    }

    @Operation(summary = "新增角色")
    @PostMapping
    public ApiResult<Void> create(@RequestBody SysRole role) {
        sysRoleService.save(role);
        return ApiResult.ok();
    }

    @Operation(summary = "更新角色")
    @PutMapping("/{id}")
    public ApiResult<Void> update(@PathVariable Long id, @RequestBody SysRole role) {
        role.setId(id);
        sysRoleService.updateById(role);
        return ApiResult.ok();
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        sysRoleService.removeById(id);
        return ApiResult.ok();
    }
}
