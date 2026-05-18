package com.blp.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blp.user.entity.SysUser;
import com.blp.user.mapper.SysUserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    public IPage<SysUser> pageUsers(int page, int size, String keyword) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(SysUser::getUsername, keyword).or().like(SysUser::getNickname, keyword);
        }
        wrapper.orderByDesc(SysUser::getCreateTime);
        return page(new Page<>(page, size), wrapper);
    }

    public boolean createUser(SysUser user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return save(user);
    }

    public boolean updateUser(SysUser user) {
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        return updateById(user);
    }
}
