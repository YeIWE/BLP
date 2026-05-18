package com.blp.file.controller;

import com.blp.common.result.ApiResult;
import com.blp.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "文件管理")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public ApiResult<String> upload(@RequestParam("file") MultipartFile file) throws Exception {
        return ApiResult.ok(fileService.upload(file));
    }

    @Operation(summary = "获取文件下载链接")
    @GetMapping("/{objectName}")
    public ApiResult<String> getUrl(@PathVariable String objectName) throws Exception {
        return ApiResult.ok(fileService.getPresignedUrl(objectName));
    }

    @Operation(summary = "删除文件")
    @DeleteMapping("/{objectName}")
    public ApiResult<Void> delete(@PathVariable String objectName) throws Exception {
        fileService.delete(objectName);
        return ApiResult.ok();
    }
}
