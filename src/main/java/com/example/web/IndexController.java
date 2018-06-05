package com.example.web;

import com.example.dao.SeckillDao;
import com.example.dto.SeckillResult;
import com.example.entity.Seckill;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/test")
public class IndexController {

	@Resource
	private SeckillDao seckillDao;

	@RequestMapping(value = "/json", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public SeckillResult<Seckill> index() {
		SeckillResult<Seckill> result;
		Seckill seckill = seckillDao.queryById(1001L);
		if (seckill == null) {
			result = new SeckillResult<Seckill>(false, "null");
		} else {
			result = new SeckillResult<Seckill>(true, seckill);
		}
		return result;
	}
}
