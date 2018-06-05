package com.example.service.impl;

import com.example.dao.RedisDao;
import com.example.dao.SeckillDao;
import com.example.dao.SuccessKilledDao;
import com.example.dto.Exposer;
import com.example.dto.SeckillExecution;
import com.example.entity.Seckill;
import com.example.entity.SuccessKilled;
import com.example.enums.SeckillStateEnum;
import com.example.exception.CloseKillException;
import com.example.exception.RepeatKillException;
import com.example.exception.SeckillException;
import com.example.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

//@Component @Service @Dao
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    // md5加密的盐值，不可逆，增加破解难点系数
    private final String slat = "slat";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    public Seckill getSeckillById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exposerSeckillUrl(long seckillId) {

        // 优化的点并非 java/mysql 本地操作，而是网络延迟和GC，mysql rowlock无法及时commit/rollback导致网络阻塞

        // 缓存爆品秒杀对象，减少数据库访问，基于缓存超时优化
        Seckill seckill = redisDao.getSeckill(seckillId);

        if (seckill == null) {
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                redisDao.putSeckill(seckill);
            }
        }
        long startTime = seckill.getStartTime().getTime();
        long deadTime = seckill.getDeadTime().getTime();
        long nowTime = new Date().getTime();

        if (nowTime < startTime || nowTime> deadTime) {
            return new Exposer(false, seckillId, nowTime, startTime, deadTime);
        }

        String md5 = getMd5Str(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    @Transactional
    /**
     * 使用注解控制事务的优点：
     * 1 一致约定，风格统一
     * 2 保证事务操作逻辑快防止阻塞，比如避免rpc/http通讯延时操作，或者延时操作放到事务外
     * 3 一条修改或者读操作不需要事务
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, CloseKillException {
        if (md5 == null || !md5.equals(getMd5Str(seckillId))) {
            throw new SeckillException("md5 error");
        }
        //先写正常流程逻辑
        try {
            //秒杀成功插入明细
            int updateKillState = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (updateKillState <= 0) {
                //重复秒杀
                throw new RepeatKillException("seckill repeated.");
            } else {
                int seckillState = seckillDao.reduceNumber(seckillId, new Date());
                if (seckillState <= 0) {
                    //秒杀关闭 rollback
                    throw new CloseKillException("seckill closed.");
                } else {
                    //秒杀成功 commit
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (CloseKillException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SeckillException(e.getMessage());
        }
    }

    private String getMd5Str(long seckillId) {
        String base = seckillId + "/" + slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
