package com.company.service;

import com.company.dto.filterDTO.ProfileFilterDTO;
import com.company.exception.ItemNotFoundException;
import com.company.repository.ProfileRepository;
import com.company.dto.ProfileDTO;
import com.company.entity.ProfileEntity;
import com.company.repository.custom.ProfileCustomRepository;
import com.company.spec.ProfileSpecification;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.company.spec.ProfileSpecification.*;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileCustomRepository profileCustomRepository;

    public ProfileDTO create(ProfileDTO dto) {
        dto.setCreatedDate(LocalDateTime.now());

        ProfileEntity entity = new ProfileEntity();
        String password = DigestUtils.md5Hex(dto.getPassword());
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setLogin(dto.getLogin());
        entity.setPassword(password);
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRole());
        entity.setCreatedDate(dto.getCreatedDate());

        profileRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public ProfileEntity get(Integer id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Profile not found"));
    }

    public ProfileDTO getById(Integer id){
        ProfileEntity entity = profileRepository
                .findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Profile Not Found with this ID"));

        return ToDTO(entity);
    }

    public List<ProfileDTO> getAll(){
        return profileRepository.findAll().stream()
                .map(this::ToDTO).collect(Collectors.toList());
    }

    public ProfileDTO update(ProfileDTO dto){
        ProfileDTO old = getById(dto.getId());

        if (dto.getName() != null && dto.getName() != old.getName()){
            profileRepository.updateNameById(dto.getId(), dto.getName());
        }

        if (dto.getSurname() != null && dto.getSurname() != old.getSurname()){
            profileRepository.updateSurnameById(dto.getId(), dto.getSurname());
        }

        if (dto.getEmail() != null && dto.getEmail() != old.getEmail()){
            profileRepository.updateEmailById(dto.getId(), dto.getEmail());
        }

        if (dto.getLogin() != null && dto.getLogin() != old.getLogin()){
            profileRepository.updateLoginById(dto.getId(), dto.getLogin());
        }

        if (dto.getPassword() != null && dto.getPassword() != old.getPassword()){
            String password = DigestUtils.md5Hex(dto.getPassword());
            profileRepository.updatePasswordById(dto.getId(), password);
        }

        if (dto.getRole() != null && dto.getRole() != old.getRole()){
            profileRepository.updateRoleById(dto.getId(), dto.getRole());
        }

        return getById(dto.getId());

    }

    public ProfileDTO deleteById(Integer id){
        ProfileDTO dto = getById(id);
        profileRepository.deleteById(id);
        return dto;
    }

    public PageImpl<ProfileDTO> filter(int page, int size, ProfileFilterDTO dto){
        Pageable pageable = PageRequest.of(page, size);
        Page<ProfileEntity> entityPage = profileCustomRepository.filter(dto, pageable);
        List<ProfileDTO> dtoList = entityPage.getContent().stream()
                .map(this::ToDTO).collect(Collectors.toList());

        return new PageImpl<ProfileDTO>(dtoList, pageable, entityPage.getTotalElements());
    }

    public Page<ProfileDTO> filterSpec(int page, int size, ProfileFilterDTO dto){
        String[] split = dto.getOrderBy().getQuery().split(" ");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(split[1]), split[0]));
        Specification<ProfileEntity> spec = idIsNotNull();
        spec = equal(spec, "id", dto.getId());
        spec = likeDouble(spec, "name", dto.getName());
        spec = likeDouble(spec, "surname", dto.getSurname());
        spec = equal(spec, "email", dto.getEmail());
        spec = equal(spec, "role", dto.getRole());
        spec = equal(spec, "status", dto.getStatus());
        spec = fromDate(spec, dto.getFromDate());
        spec = toDate(spec, dto.getToDate());


        Page<ProfileEntity> entityPage = profileRepository.findAll(spec, pageable);
        List<ProfileDTO> dtoList = entityPage.getContent().stream()
                .map(this::ToDTO).collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }


    public ProfileDTO ToDTO(ProfileEntity entity){

        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setLogin(entity.getLogin());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

}
