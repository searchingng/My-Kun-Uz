package com.company.controller;

import com.company.dto.ArticleDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.dto.filterDTO.ArticleFilterDTO;
import com.company.enums.Role;
import com.company.service.ArticleService;
import com.company.service.ProfileService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ProfileService profileService;

    @PostMapping
    public ResponseEntity create(@RequestBody ArticleDTO dto,
                                             HttpServletRequest request) {


        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.MODERATOR_ROLE);
        ArticleDTO response = articleService.create(dto, jwtDTO.getId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/publish")
    public ResponseEntity publish(@RequestBody ArticleDTO dto,
                                  HttpServletRequest request) {

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.PUBLISHER_ROLE);

        return ResponseEntity.ok(articleService.update(dto));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable("id") Integer id,
                                     HttpServletRequest request){

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.MODERATOR_ROLE);

        articleService.deleteById(id);
        return ResponseEntity.ok("Success...");
    }

    @GetMapping
    public ResponseEntity getAll(HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        return ResponseEntity.ok(articleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Integer id,
                                  HttpServletRequest request){

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        return ResponseEntity.ok(articleService.getById(id));
    }

    @PostMapping("/filter")
    public ResponseEntity filter(@RequestParam("page") int page,
                                 @RequestParam("size") int size,
                                 @RequestBody ArticleFilterDTO dto){

        return ResponseEntity.ok(articleService.filterSpecification(page, size, dto));
    }

}
