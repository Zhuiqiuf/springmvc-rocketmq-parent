package com.awifi.springmvcrocketmq.consumer.service.impl;


import com.awifi.springmvcrocketmq.consumer.entity.StocksInfo;
import com.awifi.springmvcrocketmq.consumer.mapper.StocksInfoMapper;
import com.awifi.springmvcrocketmq.consumer.service.StocksInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zhuiqiuf 减库存
 */
@Service
@Slf4j
public class StocksInfoServiceImpl implements StocksInfoService {


    @Autowired
    private StocksInfoMapper stocksInfoMapper;

    @Transactional
    public int reduceStock(StocksInfo stocksInfo){
        log.info("receive msg and reduceStock start");
        int num=stocksInfoMapper.updateStocks(stocksInfo);
        return  num;
    }
}
