package com.blp.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blp.user.entity.SysMenu;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    @Select("SELECT DISTINCT m.* FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "INNER JOIN sys_user_role ur ON ur.role_id = rm.role_id " +
            "WHERE ur.user_id = #{userId} AND m.visible = 1 ORDER BY m.sort")
    List<SysMenu> selectMenusByUserId(Long userId);

    @Select("SELECT m.* FROM sys_menu m INNER JOIN sys_role_menu rm ON m.id = rm.menu_id WHERE rm.role_id = #{roleId}")
    List<SysMenu> selectMenusByRoleId(Long roleId);

    @Delete("DELETE FROM sys_role_menu WHERE role_id = #{roleId}")
    void deleteRoleMenus(Long roleId);

    @Insert({"<script>",
        "INSERT INTO sys_role_menu (role_id, menu_id) VALUES ",
        "<foreach collection='menuIds' item='menuId' separator=','>",
        "(#{roleId}, #{menuId})",
        "</foreach>",
        "</script>"})
    void insertRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);
}
