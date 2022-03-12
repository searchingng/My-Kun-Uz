package com.company.spec;

import com.company.entity.ArticleEntity;
import com.company.enums.ArticleStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ArticleSpecification {

    public static Specification<ArticleEntity> status(ArticleStatus status) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status));
    }

    public static Specification<ArticleEntity> title(String title) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("title"), title);
        });
    }

    public static Specification<ArticleEntity> equal(String field, Integer id) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(field), id);
        });
    }

    public static Specification<ArticleEntity> equal(Specification<ArticleEntity> spec, String field, Object o) {
        if (o != null){
            return spec.and((root, query, criteriaBuilder) -> {
                return criteriaBuilder.equal(root.get(field), o);
            });
        }
        return spec;
    }

    public static Specification<ArticleEntity> like(Specification<ArticleEntity> spec, String field, String value) {
        if (value != null){
            return spec.and((root, query, criteriaBuilder) -> {
                return criteriaBuilder.like(root.get(field), "%" + value + "%");
            });
        }
        return spec;
    }

    public static Specification<ArticleEntity> greaterThanOrEqualTo(
            Specification<ArticleEntity> spec,
            String field,
            LocalDate date) {
        if (date != null){
            return spec.and(
                    (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get(field),
                        LocalDateTime.of(date, LocalTime.MIN))
            );
        }
        return spec;
    }

    public static Specification<ArticleEntity> lessThanOrEqualTo(
            Specification<ArticleEntity> spec,
            String field,
            LocalDate date) {
        if (date != null){
            return spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get(field),
                            LocalDateTime.of(date, LocalTime.MAX))
            );
        }
        return spec;
    }

}
