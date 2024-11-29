package com.jdyy.service;

import com.jdyy.commons.util.Result;

public interface AIService {
    /**
     * 转录音频
     * @param musicId 音乐id
     * @return 转录文字
     */
    Result getTranscribe(Integer musicId);
    /**
     * 生成课堂笔记
     * @return 课堂笔记
     */
    Result generateNote(Integer musicId);
}
