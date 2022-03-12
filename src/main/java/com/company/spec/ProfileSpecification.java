package com.company.spec;

import com.company.entity.CommentEntity;
import com.company.entity.ProfileEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class ProfileSpecification {

    public static Specification<ProfileEntity> idIsNotNull(){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("id"));
    }

    public static Specification<ProfileEntity> equal(Specification<ProfileEntity> spec,
                                                     String field, Object o){
        if (!Objects.isNull(o)){
            return spec.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get(field), o)
            );
        }
        return spec;
    }

    public static Specification<ProfileEntity> fromDate(Specification<ProfileEntity> spec,
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

    public static Specification<ProfileEntity> toDate(Specification<ProfileEntity> spec,
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

    public static Specification<ProfileEntity> likeDouble(Specification<ProfileEntity> spec,
                                                     String field, String value){
        if (!Objects.isNull(value)){
            return spec.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.like(root.get(field), "%" + value + "%")
            );
        }
        return spec;
    }



}
