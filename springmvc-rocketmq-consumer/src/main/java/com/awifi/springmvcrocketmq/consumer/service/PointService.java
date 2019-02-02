package com.awifi.springmvcrocketmq.consumer.service;


import com.awifi.springmvcrocketmq.consumer.entity.UserInfo;

public interface PointService {
    int addPoint(UserInfo userInfo);
}
