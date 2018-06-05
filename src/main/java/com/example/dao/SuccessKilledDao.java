package com.example.dao;

import org.apache.ibatis.annotations.Param;

import com.example.entity.SuccessKilled;

public interface SuccessKilledDao {

	/**
	 * 插入秒杀明细，可过滤重复
	 * @param seckillId
	 * @param userPhone
	 * @return
	 */
	int insertSuccessKilled(@Param(value="seckillId") long seckillId, @Param(value="userPhone") long userPhone);
	
	/**
	 * 根据秒杀单id和用户电话查询秒杀明细并携带秒杀库存对象
	 * @param seckillId
	 * @param userPhone
	 * @return
	 */
	SuccessKilled queryByIdWithSeckill(@Param(value="seckillId") long seckillId,
									   @Param(value="userPhone") long userPhone);
}
