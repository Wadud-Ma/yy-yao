package com.yy.yao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 原文表 - 存储《周易》等典籍的原始文本
 *
 * 示例: 《周易·屯卦》
 * id: 101
 * title: 《周易·屯卦》
 * content: 象曰：屯，逐也。来遇阻也...
 */
@Entity
@Table(name = "classic_text", indexes = {
        @Index(name = "idx_classic_title", columnList = "title"),
        @Index(name = "idx_classic_chapter", columnList = "chapter"),
        @Index(name = "idx_classic_dynasty", columnList = "dynasty")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassicText {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 标题 (如: 《周易·屯卦》)
     */
    @Column(nullable = false, length = 100)
    private String title;

    /**
     * 章节 (如: 屯卦、乾卦)
     */
    @Column(length = 50)
    private String chapter;

    /**
     * 原文内容
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * 作者
     */
    @Column(length = 50)
    private String author;

    /**
     * 朝代
     */
    @Column(length = 20)
    private String dynasty;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
