package com.blp.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blp.user.entity.SysMenu;
import com.blp.user.mapper.SysMenuMapper;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysMenuService extends ServiceImpl<SysMenuMapper, SysMenu> {

    public List<SysMenu> getUserMenus(Long userId) {
        List<SysMenu> allMenus = baseMapper.selectMenusByUserId(userId);
        return buildTree(allMenus, 0L);
    }

    public List<SysMenu> getAllMenus() {
        List<SysMenu> allMenus = list();
        return buildTree(allMenus, 0L);
    }

    private List<SysMenu> buildTree(List<SysMenu> menus, Long parentId) {
        return menus.stream()
                .filter(m -> m.getParentId().equals(parentId))
                .peek(m -> m.setChildren(buildTree(menus, m.getId())))
                .sorted(Comparator.comparingInt(SysMenu::getSort))
                .collect(Collectors.toList());
    }
}
