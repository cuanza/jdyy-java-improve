package com.jdyy.controller;

import com.jdyy.commons.util.Result;
import com.jdyy.service.AIService;
import com.jdyy.service.MusicService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "ai接口")
@CrossOrigin
@RestController
@RequestMapping("/ai")
public class AIController {
    @Resource
    MusicService musicService;

    @Resource
    AIService aiService;

    // 课堂笔记
    @GetMapping("/getNotes")
    public Result getNotes(Integer musicId) {
        return aiService.generateNote(musicId);
    }

    // 转录课堂录音
    @GetMapping("/getTranscribe")
    public Result generateNote(Integer musicId) {
        return aiService.getTranscribe(musicId);
    }
}
