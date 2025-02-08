package com.hanjiang.etm.common.mp.support;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * 描述：mybatis工具类
 *
 * @author huxuehao
 */
public class MP {
    public MP() {
    }

    /**
     * 获取IPage
     */
    public static <T> IPage<T> getPage(PageParams query) {
        Page<T> page = new Page<>(query.getCurrent() == null ? 1 : query.getCurrent(), query.getSize() == null ? 20 : query.getSize());

        List<String> ascList = query.getAsc();
        if (ascList != null) {
            for (String asc : ascList) {
                page.addOrder(OrderItem.asc(cleanIdentifier(asc)));
            }
        }

        List<String> descList = query.getDesc();
        if (descList != null) {
            for (String desc : descList) {
                page.addOrder(OrderItem.asc(cleanIdentifier(desc)));
            }
        }

        return page;
    }

    /**
     * 获取 QueryWrapper
     * @param entity 实体
     */
    public static <T> QueryWrapper<T> getQueryWrapper(T entity) {
        if (entity == null) {
            return null;
        }
        QueryWrapper<T> qw = new QueryWrapper<>();
        ConditionBuilder.buildCondition(entity, qw);
        return qw;
    }


    private static String cleanIdentifier(@Nullable String param) {
        if (param == null) {
            return null;
        } else {
            StringBuilder paramBuilder = new StringBuilder();

            for(int i = 0; i < param.length(); ++i) {
                char c = param.charAt(i);
                if (Character.isJavaIdentifierPart(c)) {
                    paramBuilder.append(c);
                }
            }

            return paramBuilder.toString();
        }
    }
}
