package com.example.demo.dataroom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/auth/dataroom")
public class DataroomController {
    @Autowired
    private DataroomService service;
    @Autowired
    private ReplyService rservice;

    @PostMapping("/add")
    public String add(DataroomDto dto){
        service.save(dto);
        return "redirect:/all/qalist";
    }

    @PostMapping("/reply")
    public String reply(ReplyDto dto){
        log.debug("dto="+dto);
        rservice.save(dto);
        log.debug("after dto="+dto);
        return "redirect:/all/qalist";
    }

    @ResponseBody
    @PostMapping("/update")
    public Map update(int num, String content){
        DataroomDto dto = service.get(num);
        dto.setContent(content);
        service.save(dto);
        Map map = new HashMap<>();
        map.put("flag", true);
        return map;
    }

    @GetMapping("/delete")
    public String delete(int num){
        DataroomDto dto = service.get(num);
        service.del(dto);
        Map map = new HashMap<>();
        map.put("flag", true);
        return "redirect:/all/qalist";
    }

    @RequestMapping("/detail")
    public String detail(int num, ModelMap map){
        DataroomDto dto = service.get(num);
        map.addAttribute("dataroom", dto);
        return "dataroom/detail";
    }
}
