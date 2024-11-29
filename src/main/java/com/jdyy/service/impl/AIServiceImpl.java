package com.jdyy.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdyy.commons.util.Result;
import com.jdyy.controller.MusicController;
import com.jdyy.entity.Music;
import com.jdyy.mapper.MusicMapper;
import com.jdyy.service.AIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class AIServiceImpl implements AIService {
    @Resource
    MusicMapper musicMapper;

    @Value("${flask.server.transcribe-url}")
    private String transcribeUrl;

    @Value("${flask.server.note-url}")
    private String noteUrl;

    @Override
    public Result getTranscribe(Integer musicId) {
        String message = "";
        try {
            // 1. 获取音乐文件
            Music music = musicMapper.getOneMusic(musicId);
            if (music == null) {
                throw new IllegalArgumentException("音乐ID不存在");
            }

            // 构造音乐文件路径
            String relativePath = MusicController.class.getClassLoader().getResource("").getPath();
            relativePath = URLDecoder.decode(relativePath, StandardCharsets.UTF_8);
            String filePath = relativePath + "static/" + music.getUrl();

            File audioFile = new File(filePath);
            if (!audioFile.exists()) {
                throw new IllegalArgumentException("音频文件不存在: " + filePath);
            }

            // 2. 准备 HTTP 请求
            RestTemplate restTemplate = new RestTemplate();
            String flaskServerUrl = transcribeUrl;

            // 使用 Spring 的 HttpEntity 封装请求
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 创建 MultiValueMap 用于传递文件
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(audioFile));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 3. 发送 POST 请求
            ResponseEntity<String> response = restTemplate.exchange(
                    flaskServerUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            // 4. 处理 Flask 服务器的响应
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("音频发送成功，Flask 服务器返回: " + response.getBody());
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> result = objectMapper.readValue(response.getBody(), Map.class);

                // 自动解码 Unicode 转义
                message = (String) result.get("message");
                System.out.println("解析后的消息: " + message);
            } else {
                System.err.println("Flask 服务器返回错误: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("转发音频文件失败", e.getMessage());
        }
        return Result.success(message);
    }

    @Override
    public Result generateNote(Integer musicId) {
        String message = "";
        try {
            // 1. 获取音乐文件
            Music music = musicMapper.getOneMusic(musicId);
            if (music == null) {
                throw new IllegalArgumentException("音乐ID不存在");
            }

            // 构造音乐文件路径
            String relativePath = MusicController.class.getClassLoader().getResource("").getPath();
            relativePath = URLDecoder.decode(relativePath, StandardCharsets.UTF_8);
            String filePath = relativePath + "static/" + music.getUrl();

            File audioFile = new File(filePath);
            if (!audioFile.exists()) {
                throw new IllegalArgumentException("音频文件不存在: " + filePath);
            }

            // 2. 准备 HTTP 请求
            RestTemplate restTemplate = new RestTemplate();
            String flaskServerUrl = noteUrl;

            // 使用 Spring 的 HttpEntity 封装请求
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 创建 MultiValueMap 用于传递文件
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(audioFile));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 3. 发送 POST 请求
            ResponseEntity<String> response = restTemplate.exchange(
                    flaskServerUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            // 4. 处理 Flask 服务器的响应
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("音频发送成功，Flask 服务器返回: " + response.getBody());
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> result = objectMapper.readValue(response.getBody(), Map.class);

                // 自动解码 Unicode 转义
                message = (String) result.get("message");
                System.out.println("解析后的消息: " + message);
            } else {
                System.err.println("Flask 服务器返回错误: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("转发音频文件失败", e.getMessage());
        }
        return Result.success(message);
    }
}
