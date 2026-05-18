CREATE DATABASE IF NOT EXISTS blp_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE blp_db;

-- RBAC 权限体系
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20),
    avatar VARCHAR(255),
    status TINYINT DEFAULT 1 COMMENT '1=正常 0=禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200),
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

CREATE TABLE sys_menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT DEFAULT 0,
    name VARCHAR(50) NOT NULL,
    path VARCHAR(200),
    component VARCHAR(200),
    icon VARCHAR(50),
    type TINYINT COMMENT '0=目录 1=菜单 2=按钮',
    permission VARCHAR(200) COMMENT '权限标识',
    sort INT DEFAULT 0,
    visible TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

CREATE TABLE sys_role_menu (
    role_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 商品域
CREATE TABLE pms_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT DEFAULT 0,
    name VARCHAR(50) NOT NULL,
    icon VARCHAR(255),
    level INT DEFAULT 1,
    sort INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

CREATE TABLE pms_brand (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    logo VARCHAR(255),
    description VARCHAR(500),
    sort INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='品牌表';

CREATE TABLE pms_product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    category_id BIGINT NOT NULL,
    brand_id BIGINT,
    description TEXT,
    main_image VARCHAR(255),
    images VARCHAR(2000),
    price DECIMAL(10,2) NOT NULL,
    stock INT DEFAULT 0,
    unit VARCHAR(20),
    status TINYINT DEFAULT 1 COMMENT '0=下架 1=上架',
    sort INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category (category_id),
    INDEX idx_brand (brand_id),
    INDEX idx_price (price)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 初始化数据
-- BCrypt 密码: admin123
INSERT INTO sys_user (username, password, nickname) VALUES
('admin', '$2b$10$7sQWO/2kcqhcgwkWmU7aLe1opboaTUmyGrw3LT6JM18jSNpsJOwZG', '超级管理员');

INSERT INTO sys_role (name, code, description) VALUES
('超级管理员', 'ADMIN', '系统最高权限'),
('普通用户', 'USER', '基础用户权限');

INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

INSERT INTO sys_menu (id, parent_id, name, type, path, component, icon, permission, sort) VALUES
(1, 0, '系统管理', 0, '/sys', '', 'Setting', '', 1),
(2, 1, '用户管理', 1, '/sys/user', 'sys/user/index', 'User', 'sys:user:list', 1),
(3, 2, '用户查询', 2, '', '', '', 'sys:user:query', 1),
(4, 2, '用户新增', 2, '', '', '', 'sys:user:add', 2),
(5, 2, '用户编辑', 2, '', '', '', 'sys:user:edit', 3),
(6, 2, '用户删除', 2, '', '', '', 'sys:user:delete', 4),
(7, 1, '角色管理', 1, '/sys/role', 'sys/role/index', 'UserFilled', 'sys:role:list', 2),
(8, 1, '菜单管理', 1, '/sys/menu', 'sys/menu/index', 'Menu', 'sys:menu:list', 3),
(9, 0, '商品管理', 0, '/pms', '', 'Goods', '', 2),
(10, 9, '商品列表', 1, '/pms/product', 'pms/product/index', 'Goods', 'pms:product:list', 1),
(11, 9, '分类管理', 1, '/pms/category', 'pms/category/index', 'Operation', 'pms:category:list', 2),
(12, 9, '品牌管理', 1, '/pms/brand', 'pms/brand/index', 'Medal', 'pms:brand:list', 3);

INSERT INTO sys_role_menu (role_id, menu_id) SELECT 1, id FROM sys_menu;
