package com.yy.yao.repository;

import com.yy.yao.entity.HexagramInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 卦象基础信息Repository
 */
@Repository
public interface HexagramInfoRepository extends JpaRepository<HexagramInfo, Long> {

    /**
     * 根据卦序号查询
     */
    Optional<HexagramInfo> findByNumber(Integer number);

    /**
     * 根据卦名查询
     */
    Optional<HexagramInfo> findByName(String name);

    /**
     * 根据二进制表示查询
     */
    Optional<HexagramInfo> findByBinaryCode(String binaryCode);

    /**
     * 根据上下卦查询
     */
    List<HexagramInfo> findByUpperTrigramAndLowerTrigram(String upperTrigram, String lowerTrigram);

    /**
     * 根据上卦查询
     */
    List<HexagramInfo> findByUpperTrigram(String upperTrigram);

    /**
     * 根据下卦查询
     */
    List<HexagramInfo> findByLowerTrigram(String lowerTrigram);

    /**
     * 检查卦序号是否存在
     */
    boolean existsByNumber(Integer number);

    /**
     * 获取所有卦象,按序号排序
     */
    @Query("SELECT h FROM HexagramInfo h ORDER BY h.number")
    List<HexagramInfo> findAllOrderByNumber();
}
