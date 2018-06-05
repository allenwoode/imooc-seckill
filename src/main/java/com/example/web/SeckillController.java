package com.example.web;

import com.example.dto.Exposer;
import com.example.dto.SeckillExecution;
import com.example.dto.SeckillResult;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.ReadOnlyBufferException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill") //模块 url: /seckill/{id}/detail
public class SeckillController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        return "list"; // /WEB-INF/jsp/list.jsp
    }

    @RequestMapping(value = "/{id}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable(value = "id") Long id, Model model) {
        if (id == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getSeckillById(id);
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    @RequestMapping(value = "/{id}/exposer", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<Exposer> expose(@PathVariable(value = "id") Long id) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exposerSeckillUrl(id);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.info("error: {}", e.getMessage());
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{id}/{md5}/execution", method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("id") Long id,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) Long killPhone) {

        if (killPhone == null) {
            return new SeckillResult<SeckillExecution>(false, "未注册");
        }
        //SeckillResult<SeckillExecution> result;
        try {
            SeckillExecution execution = seckillService.executeSeckill(id, killPhone, md5);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (CloseKillException e) {
            SeckillExecution execution = new SeckillExecution(id, SeckillStateEnum.CLOSE);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (RepeatKillException e) {
            SeckillExecution execution = new SeckillExecution(id, SeckillStateEnum.REPEAT);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (SeckillException e) {
            SeckillExecution execution = new SeckillExecution(id, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true, execution);
        }
    }

    @RequestMapping(value = "/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> now() {
        return new SeckillResult<Long>(true, new Date().getTime());
    }
}
