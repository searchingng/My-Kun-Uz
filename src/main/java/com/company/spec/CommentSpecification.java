package com.company.spec;

import com.company.entity.CommentEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class CommentSpecification {

    public static Specification<CommentEntity> idIsNotNull(){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("id")));
    }

    public static Specification<CommentEntity> equal(Specification<CommentEntity> spec,
                                                     String field, Object o){
        if (!Objects.isNull(o)){
            return spec.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get(field), o)
            );
        }
        return spec;
    }

    public static Specification<CommentEntity> fromDate(Specification<CommentEntity> spec,
                                                        LocalDate date){
        if (!Objects.isNull(date)){
            return spec.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"),
                                    LocalDateTime.of(date, LocalTime.MIN))
            );
        }
        return spec;
    }

    public static Specification<CommentEntity> toDate(Specification<CommentEntity> spec,
                                                        LocalDate date){
        if (!Objects.isNull(date)){
            return spec.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"),
                                    LocalDateTime.of(date, LocalTime.MAX))
            );
        }
        return spec;
    }

}
