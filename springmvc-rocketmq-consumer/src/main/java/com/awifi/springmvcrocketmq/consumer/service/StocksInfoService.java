package com.awifi.springmvcrocketmq.consumer.service;


import com.awifi.springmvcrocketmq.consumer.entity.StocksInfo;

public interface StocksInfoService {
    int reduceStock(StocksInfo stocksInfo);
}
