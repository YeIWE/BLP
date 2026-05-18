package com.blp.product.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blp.product.entity.PmsCategory;
import com.blp.product.mapper.PmsCategoryMapper;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PmsCategoryService extends ServiceImpl<PmsCategoryMapper, PmsCategory> {

    public List<PmsCategory> getTree() {
        List<PmsCategory> all = list();
        return buildTree(all, 0L);
    }

    private List<PmsCategory> buildTree(List<PmsCategory> list, Long parentId) {
        return list.stream()
                .filter(c -> c.getParentId().equals(parentId))
                .peek(c -> c.setChildren(buildTree(list, c.getId())))
                .sorted(Comparator.comparingInt(PmsCategory::getSort))
                .collect(Collectors.toList());
    }
}
