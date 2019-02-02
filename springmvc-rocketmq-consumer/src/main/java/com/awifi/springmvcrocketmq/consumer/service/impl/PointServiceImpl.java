package com.awifi.springmvcrocketmq.consumer.service.impl;

import com.awifi.springmvcrocketmq.consumer.entity.UserInfo;
import com.awifi.springmvcrocketmq.consumer.mapper.UserInfoMapper;
import com.awifi.springmvcrocketmq.consumer.service.PointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zhuiqiuf 加积分操作
 */
@Service
@Slf4j
public class PointServiceImpl implements PointService {



    @Autowired
    private UserInfoMapper userInfoMapper;

    @Transactional
    public int addPoint(UserInfo userInfo){
        log.info("receive msg and addPoint start");
        int num= userInfoMapper.addPoint(userInfo);

        return num;
    }
}
