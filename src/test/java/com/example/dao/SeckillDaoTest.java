package com.example.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.entity.Seckill;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SeckillDaoTest {

	@Resource
	private SeckillDao seckillDao;
	
	@Test
	public void testReduceNumber() throws Exception {
		long id = 1000L;
		Date killTime = new Date();
		int ret = seckillDao.reduceNumber(id, killTime);
		System.out.println("==================================================================");
		System.out.println("change row numbers: " + ret);
	}
	
	@Test
	public void testQueryById() throws Exception {
		long id = 1000L;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println("==================================================================");
		System.out.println(seckill.toString());
	}
	
	@Test
	public void testQueryAll() throws Exception {
		List<Seckill> list = seckillDao.queryAll(0, 100);
		System.out.println("==================================================================");
		for (Seckill bean : list) {
			System.out.println(bean.toString());
		}
	}
}
