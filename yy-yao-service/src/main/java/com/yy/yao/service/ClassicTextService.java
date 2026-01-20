package com.yy.yao.service;

import com.yy.yao.dto.ClassicTextDetailDTO;
import com.yy.yao.entity.Annotation;
import com.yy.yao.entity.ClassicText;
import com.yy.yao.entity.Translation;
import com.yy.yao.repository.AnnotationRepository;
import com.yy.yao.repository.ClassicTextRepository;
import com.yy.yao.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 经文查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClassicTextService {

    private final ClassicTextRepository classicTextRepository;
    private final AnnotationRepository annotationRepository;
    private final TranslationRepository translationRepository;

    /**
     * 根据ID查询经文详情(包含注释和翻译)
     */
    @Transactional(readOnly = true)
    public Optional<ClassicTextDetailDTO> getTextDetail(Long textId) {
        Optional<ClassicText> textOpt = classicTextRepository.findById(textId);
        if (textOpt.isEmpty()) {
            return Optional.empty();
        }

        ClassicText text = textOpt.get();
        List<Annotation> annotations = annotationRepository.findByTextIdOrderByMarker(textId);
        List<Translation> translations = translationRepository.findByTextId(textId);

        ClassicTextDetailDTO dto = convertToDetailDTO(text, annotations, translations);
        return Optional.of(dto);
    }

    /**
     * 根据章节查询经文列表
     */
    @Transactional(readOnly = true)
    public List<ClassicTextDetailDTO> getTextsByChapter(String chapter) {
        List<ClassicText> texts = classicTextRepository.findByChapter(chapter);
        return texts.stream()
                .map(text -> {
                    List<Annotation> annotations = annotationRepository.findByTextIdOrderByMarker(text.getId());
                    List<Translation> translations = translationRepository.findByTextId(text.getId());
                    return convertToDetailDTO(text, annotations, translations);
                })
                .collect(Collectors.toList());
    }

    /**
     * 搜索经文(全文搜索)
     */
    @Transactional(readOnly = true)
    public List<ClassicText> searchTexts(String keyword) {
        return classicTextRepository.fullTextSearch(keyword);
    }

    /**
     * 分页搜索经文
     */
    @Transactional(readOnly = true)
    public Page<ClassicText> searchTextsPage(String keyword, Pageable pageable) {
        return classicTextRepository.fullTextSearchPage(keyword, pageable);
    }

    /**
     * 根据标记查询注释
     */
    @Transactional(readOnly = true)
    public Optional<Annotation> getAnnotationByMarker(Long textId, String marker) {
        return annotationRepository.findByTextIdAndMarker(textId, marker);
    }

    /**
     * 获取所有章节列表
     */
    @Transactional(readOnly = true)
    public List<String> getAllChapters() {
        return classicTextRepository.findAllChapters();
    }

    /**
     * 保存经文及其注释和翻译
     */
    @Transactional
    public ClassicText saveTextWithAnnotationsAndTranslations(
            ClassicText text,
            List<Annotation> annotations,
            List<Translation> translations) {

        // 保存原文
        ClassicText savedText = classicTextRepository.save(text);

        // 保存注释
        if (annotations != null && !annotations.isEmpty()) {
            annotations.forEach(annotation -> annotation.setTextId(savedText.getId()));
            annotationRepository.saveAll(annotations);
        }

        // 保存翻译
        if (translations != null && !translations.isEmpty()) {
            translations.forEach(translation -> translation.setTextId(savedText.getId()));
            translationRepository.saveAll(translations);
        }

        log.info("保存经文成功: id={}, title={}", savedText.getId(), savedText.getTitle());
        return savedText;
    }

    /**
     * 转换为详情DTO
     */
    private ClassicTextDetailDTO convertToDetailDTO(
            ClassicText text,
            List<Annotation> annotations,
            List<Translation> translations) {

        List<ClassicTextDetailDTO.AnnotationDTO> annotationDTOs = annotations.stream()
                .map(a -> ClassicTextDetailDTO.AnnotationDTO.builder()
                        .id(a.getId())
                        .marker(a.getMarker())
                        .content(a.getContent())
                        .type(a.getType())
                        .build())
                .collect(Collectors.toList());

        List<ClassicTextDetailDTO.TranslationDTO> translationDTOs = translations.stream()
                .map(t -> ClassicTextDetailDTO.TranslationDTO.builder()
                        .id(t.getId())
                        .content(t.getContent())
                        .translator(t.getTranslator())
                        .build())
                .collect(Collectors.toList());

        return ClassicTextDetailDTO.builder()
                .id(text.getId())
                .title(text.getTitle())
                .chapter(text.getChapter())
                .content(text.getContent())
                .author(text.getAuthor())
                .dynasty(text.getDynasty())
                .annotations(annotationDTOs)
                .translations(translationDTOs)
                .build();
    }
}
