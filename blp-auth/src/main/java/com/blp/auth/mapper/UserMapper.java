package com.blp.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<com.blp.auth.model.SecurityUser> {
    @Select("SELECT u.id, u.username, u.password, u.nickname, u.status " +
            "FROM sys_user u WHERE u.username = #{username}")
    com.blp.auth.model.SecurityUser selectByUsername(@Param("username") String username);

    @Select("SELECT r.code FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.status = 1")
    List<String> selectRolesByUserId(@Param("userId") Long userId);

    @Select("SELECT DISTINCT m.permission FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "INNER JOIN sys_user_role ur ON ur.role_id = rm.role_id " +
            "WHERE ur.user_id = #{userId} AND m.type = 2 AND m.permission IS NOT NULL")
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);
}
