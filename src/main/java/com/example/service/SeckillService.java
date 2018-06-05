package com.example.service;

import com.example.dto.Exposer;
import com.example.dto.SeckillExecution;
import com.example.entity.Seckill;
import com.example.exception.CloseKillException;
import com.example.exception.RepeatKillException;
import com.example.exception.SeckillException;

import java.util.List;

/**
 * 面向使用者设计接口，要领有3个
 * 1：方法粒度不要太细；2：参数简单直观；3：返回类型
 */
public interface SeckillService {

    List<Seckill> getSeckillList();

    /**
     *
     * @param seckillId
     * @return
     */
    Seckill getSeckillById(long seckillId);

    /**
     * 返回秒杀超链接并且保证随机性和唯一性防止客户端作弊
     * @param seckillId
     * @return
     */
    Exposer exposerSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作，返回秒杀封装数据包括秒杀明细，同时抛出运行时异常便于数据库回滚，防止数据库操作不同步
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws CloseKillException
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, CloseKillException;
}
