package com.example.service;

import com.example.dto.Exposer;
import com.example.dto.SeckillExecution;
import com.example.entity.Seckill;
import com.example.entity.SuccessKilled;
import com.example.exception.CloseKillException;
import com.example.exception.RepeatKillException;
import com.example.exception.SeckillException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resources;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list=>{}", list);
    }

    @Test
    public void getSeckillById() {
        Seckill seckill = seckillService.getSeckillById(1000L);
        logger.info("seckill=>{}", seckill);
    }

    @Test
    public void exposerSeckillUrl() {
        long seckillId = 1000L;
        Exposer exposer = seckillService.exposerSeckillUrl(seckillId);
        logger.info("Exposer=>{}", exposer);
    }

    @Test
    public void executeSeckill() {
        long seckillId = 1000L;
        long userPhone = 18258729246L;
        Exposer exposer = seckillService.exposerSeckillUrl(seckillId);
        SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, exposer.getMd5());
        logger.info("Execution=>{}", execution);
    }

    @Test
    public void completeSeckillLogic() {
        long seckillId = 1001L;
        Exposer exposer = seckillService.exposerSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            long userPhone = 18258729248L; //用户电话号码必须得到验证才可以参入秒杀
            //这样看来可以利用程序开发一个机器秒杀器，完虐手动秒杀党
            try {
                SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, exposer.getMd5());
                logger.info("execution=>{}", execution);
            } catch (CloseKillException e1) {
                throw e1;
            } catch (RepeatKillException e2) {
                throw e2;
            } catch (SeckillException e) {
                throw e;
            }
        } else {
            logger.warn("exposer=>{}", exposer);
        }
    }

}