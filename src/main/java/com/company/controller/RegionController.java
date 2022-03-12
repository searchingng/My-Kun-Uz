package com.company.controller;

import com.company.dto.ProfileJwtDTO;
import com.company.dto.RegionDTO;
import com.company.enums.Role;
import com.company.service.ProfileService;
import com.company.service.RegionService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/region")
public class RegionController {

    @Autowired
    private RegionService regionService;
    @Autowired
    private ProfileService profileService;

    @PostMapping
    public ResponseEntity create(@RequestBody RegionDTO dto,
                                 HttpServletRequest request) {

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);


        RegionDTO response = regionService.create(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping
        public ResponseEntity update(@RequestBody RegionDTO dto,
                                  HttpServletRequest request) {

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);


        return ResponseEntity.ok(regionService.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Integer id,
                                     HttpServletRequest request){

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);


        regionService.deleteById(id);
        return ResponseEntity.ok("Success...");
    }

    @GetMapping
    public ResponseEntity getAll(HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);


        return ResponseEntity.ok(regionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Integer id,
                                  HttpServletRequest request){

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);


        return ResponseEntity.ok(regionService.getById(id));
    }

}
