package com.example.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.entity.Seckill;

public interface SeckillDao {
	
	/**
	 * 减库存
	 * @param seckillId
	 * @param killTime
	 * @return
	 */
	int reduceNumber(@Param(value="seckillId") long seckillId, @Param(value="killTime") Date killTime);
	
	/**
	 * 根据ID查询秒杀库存
	 * @param seckillId
	 * @return
	 */
	Seckill queryById(long seckillId);
	
	/**
	 * 根据偏移量查询秒杀库存
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Seckill> queryAll(@Param(value="offset") int offset, @Param(value="limit") int limit);

}
