package com.blp.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blp.user.entity.SysMenu;
import com.blp.user.entity.SysRole;
import com.blp.user.mapper.SysMenuMapper;
import com.blp.user.mapper.SysRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {

    private final SysMenuMapper sysMenuMapper;

    public List<Map<String, Object>> getRoleMenuTree(Long roleId) {
        List<SysMenu> allMenus = sysMenuMapper.selectList(null);
        List<SysMenu> checkedMenus = sysMenuMapper.selectMenusByRoleId(roleId);
        Set<Long> checkedIds = checkedMenus.stream().map(SysMenu::getId).collect(Collectors.toSet());

        return buildTree(allMenus, 0L, checkedIds);
    }

    private List<Map<String, Object>> buildTree(List<SysMenu> menus, Long parentId, Set<Long> checkedIds) {
        return menus.stream()
            .filter(m -> m.getParentId().equals(parentId))
            .sorted(Comparator.comparingInt(SysMenu::getSort))
            .map(m -> {
                Map<String, Object> node = new HashMap<>();
                node.put("id", m.getId());
                node.put("label", m.getName());
                node.put("checked", checkedIds.contains(m.getId()));
                node.put("type", m.getType());
                List<Map<String, Object>> children = buildTree(menus, m.getId(), checkedIds);
                if (!children.isEmpty()) node.put("children", children);
                return node;
            }).collect(Collectors.toList());
    }

    @Transactional
    public void saveRoleMenus(Long roleId, List<Long> menuIds) {
        sysMenuMapper.deleteRoleMenus(roleId);
        if (menuIds != null && !menuIds.isEmpty()) {
            sysMenuMapper.insertRoleMenus(roleId, menuIds);
        }
    }
}
