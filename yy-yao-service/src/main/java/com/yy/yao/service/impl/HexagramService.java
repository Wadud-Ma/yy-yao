package com.yy.yao.service.impl;

import com.yy.yao.service.mapper.HexagramMapper;
import com.yy.yao.model.domain.Hexagram;
import com.yy.yao.dao.repository.HexagramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 卦象服务
 * 使用 Java 21 特性优化代码
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HexagramService {

    private final HexagramRepository hexagramRepository;
    private final HexagramMapper hexagramMapper;

    /**
     * 获取所有卦象
     */
    public List<Hexagram> getAllHexagrams() {
        var entities = hexagramRepository.findAll();
        return hexagramMapper.toModelList(entities);
    }

    /**
     * 根据编号获取卦象
     */
    public Hexagram getHexagramByNumber(int number) {
        return hexagramRepository.findByNumber(number)
                .map(hexagramMapper::toModel)
                .orElse(null);
    }

    /**
     * 根据二进制表示获取卦象
     */
    public Hexagram getHexagramByBinary(String binary) {
        return hexagramRepository.findByBinary(binary)
                .map(hexagramMapper::toModel)
                .orElse(null);
    }
}
