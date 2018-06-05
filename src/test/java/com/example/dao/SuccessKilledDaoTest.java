package com.example.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.entity.SuccessKilled;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccessKilledDaoTest {

	@Resource
	private SuccessKilledDao successKilledDao;
	
	@Test
	public void testInsertSuccessKilled() throws Exception {
		long seckillId = 1001L;
		long userPhone = 18258729246L;
		int ret = successKilledDao.insertSuccessKilled(seckillId, userPhone);
		System.out.println("==================================================================");
		System.out.println("change row numbers: " + ret);
	}
	
	@Test
	public void testQueryByIdWithSeckill() throws Exception {
		long seckillId = 1001L;
		long userPhone = 18258729246L;
		SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
		
		if (successKilled != null) {
			System.out.println("==================================================================");
			System.out.println(successKilled.toString());
			System.out.println(successKilled.getSeckill().toString());
		}
	}
}
