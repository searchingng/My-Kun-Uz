package com.company.controller;

import com.company.dto.ProfileDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.dto.filterDTO.ProfileFilterDTO;
import com.company.enums.Role;
import com.company.service.ProfileService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody ProfileDTO dto,
                                             HttpServletRequest request) {

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);


        ProfileDTO response = profileService.create(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity getAll(HttpServletRequest request){

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);


        return ResponseEntity.ok(profileService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Integer id, HttpServletRequest request){

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);


        return ResponseEntity.ok(profileService.getById(id));
    }

    @PutMapping
    public ResponseEntity update(@Valid @RequestBody ProfileDTO dto,
                                 HttpServletRequest request){

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);

        return ResponseEntity.ok(profileService.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable("id") Integer id,
                                     HttpServletRequest request){

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);


        return ResponseEntity.ok(profileService.deleteById(id));
    }

    @PostMapping("/filter")
    public ResponseEntity filter(@RequestParam("page") int page,
                                 @RequestParam("size") int size,
                                 @RequestBody ProfileFilterDTO dto){
        return ResponseEntity.ok(profileService.filterSpec(page, size, dto));
    }

}