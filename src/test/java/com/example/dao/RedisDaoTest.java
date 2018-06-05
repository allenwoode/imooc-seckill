package com.example.dao;

import com.example.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class RedisDaoTest {

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testRedisSeckill() {
        long seckillId = 1002;
        // get and put seckill
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            // get from mysql db
            seckill = seckillDao.queryById(seckillId);
            if (seckill != null) {
                String result = redisDao.putSeckill(seckill);
                System.out.println(result);
            }
        } else {
            System.out.println(seckill);
        }
    }

}