package com.awifi.springmvcrocketmq.consumer.mapper;

import com.awifi.springmvcrocketmq.consumer.entity.PurchaseList;
import org.springframework.stereotype.Repository;

/**
 * @author Tangzy 模拟下单场景的 mybatis mapper接口
 */
@Repository
public interface PurchaseListMapper {

    /**
     * 根据主键查询规则
     * @param id
     * @return
     */
    PurchaseList selectByPrimaryKey(Long id);

    /**
     * 插入回查表数据
     * @param purchaseList
     */
    int insert(PurchaseList purchaseList);

}