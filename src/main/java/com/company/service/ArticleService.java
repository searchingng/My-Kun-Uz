package com.company.service;

import com.company.dto.ArticleDTO;
import com.company.dto.filterDTO.ArticleFilterDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ArticleStatus;
import com.company.exception.BadRequestException;
import com.company.repository.custom.ArticleCustomRepository;
import com.company.repository.ArticleRepository;
import com.company.spec.ArticleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ArticleCustomRepository articleCustomRepository;

    public ArticleDTO create(ArticleDTO dto, Integer userId) {
        ProfileEntity profileEntity = profileService.get(userId);

        if (dto.getTitle() == null || dto.getTitle().isEmpty())
            throw new BadRequestException("Title is empty");

        if (dto.getContent() == null || dto.getContent().isEmpty())
            throw new BadRequestException("Content is empty");

        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setProfile(profileEntity);
        entity.setStatus(dto.getStatus());

        articleRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public List<ArticleDTO> getAll(){
        return articleRepository.findAll().stream()
                .map(this::ToDto).collect(Collectors.toList());
    }

    public ArticleDTO getById(Integer id){
        return articleRepository.findById(id).map(this::ToDto)
                .orElseThrow(() -> new RuntimeException("Bunday article NOT EXIST"));
    }

    public String update(ArticleDTO dto){
        ArticleDTO old = getById(dto.getId());

        if (dto.getTitle() != null && dto.getTitle() != old.getTitle())
            articleRepository.updateTitleById(dto.getId(), dto.getTitle());

        if (dto.getContent() != null && dto.getContent() != old.getContent())
            articleRepository.updateContentById(dto.getId(), dto.getContent());

        if (dto.getProfileId() != null && dto.getProfileId() != old.getProfileId())
            articleRepository.updateProfileIdById(dto.getId(), dto.getProfileId());

        if (dto.getPublisherId() != null && dto.getPublisherId() != old.getPublisherId())
            articleRepository.updatePublisherIdById(dto.getId(), dto.getPublisherId());

        if (dto.getPublishedDate() != null)
            articleRepository.updatePublishedDateById(dto.getId(), dto.getPublishedDate());

        return "Succesfully";

    }

    public ArticleDTO deleteById(Integer id){
        ArticleDTO dto = getById(id);
        articleRepository.deleteById(id);
        return dto;
    }


    public ArticleDTO ToDto(ArticleEntity entity){
        ArticleDTO dto = new ArticleDTO();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setProfileId(entity.getProfile().getId());
        if (entity.getPublisher() !=null)
            dto.setPublisherId(entity.getPublisher().getId());
        dto.setCreatedDate(entity.getPublishedDate());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setStatus(entity.getStatus());

        return dto;
    }

    public PageImpl<ArticleDTO> filter(Pageable pageable, ArticleFilterDTO dto){
        PageImpl<ArticleEntity> entityPage = articleCustomRepository.filter(pageable, dto);

        List<ArticleDTO> dtoList = entityPage.getContent().stream()
                .map(this::ToDto).collect(Collectors.toList());

        return new PageImpl<>(dtoList, entityPage.getPageable(), entityPage.getTotalElements());
    }

    public Page<ArticleDTO> filterSpecification(int page, int size, ArticleFilterDTO dto){
        Pageable pageable = PageRequest.of(page, size);

        Specification<ArticleEntity> spec = null;
        if (dto.getStatus() != null) {
            spec = Specification.where(ArticleSpecification.status(dto.getStatus()));
        } else {
            spec = Specification.where(ArticleSpecification.status(ArticleStatus.PUBLISHED));
        }
        spec = ArticleSpecification.equal(spec, "id", dto.getId());
        spec = ArticleSpecification.equal(spec, "profile", profileService.get(dto.getProfileId()));
        spec = ArticleSpecification.like(spec, "title", dto.getTitle());
        spec = ArticleSpecification.greaterThanOrEqualTo(spec, "createdDate", dto.getFromDate());
        spec = ArticleSpecification.lessThanOrEqualTo(spec, "createdDate", dto.getToDate());

        Page<ArticleEntity> entitiyPage = articleRepository.findAll(spec, pageable);
        List<ArticleDTO> content = entitiyPage.getContent().stream()
                .map(this::ToDto).collect(Collectors.toList());

        return new PageImpl<ArticleDTO>(content, pageable, entitiyPage.getTotalElements());
    }

    public ArticleEntity get(Integer id){
        return articleRepository.getById(id);
    }
}
