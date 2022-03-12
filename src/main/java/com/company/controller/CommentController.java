package com.company.controller;

import com.company.dto.CommentDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.dto.filterDTO.CommentFilterDTO;
import com.company.exception.BadRequestException;
import com.company.service.CommentService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    private ResponseEntity create(@RequestBody CommentDTO dto,
                                  HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);

        return ResponseEntity.ok(commentService.create(dto, jwtDTO.getId()));
    }

    @GetMapping("/{id}")
    public CommentDTO getById(@PathVariable("id") Integer id,
                              HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);

        return commentService.getById(id);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody CommentDTO dto,
                                 HttpServletRequest request){

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        if (!jwtDTO.getId().equals(dto.getProfileId())){
            throw new BadRequestException("You have not written thi Comment!!!");
        }
        return ResponseEntity.ok(commentService.update(dto));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id,
                           HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        if (!jwtDTO.getId().equals(commentService.getById(id).getProfileId())){
            throw new BadRequestException("You have not written thi Comment!!!");
        }
        commentService.deleteById(id);
    }

    @GetMapping("/pid/{pid}")
    public ResponseEntity getByPid(@PathVariable Integer pid){
        return ResponseEntity.ok(commentService.getByPid(pid));
    }

    @GetMapping("/aid/{aid}")
    public ResponseEntity getByAid(@RequestParam("page") int page,
                                   @RequestParam("size") int size,
                                   @PathVariable Integer aid){
        return ResponseEntity.ok(commentService.getByAid(page, size, aid));
    }

    @GetMapping
    public ResponseEntity getAll(@RequestParam("page") int page,
                                   @RequestParam("size") int size){
        return ResponseEntity.ok(commentService.getAll(page, size));
    }

    @PostMapping("/filter")
    public ResponseEntity filter(@RequestParam("page") int page,
                                 @RequestParam("size") int size,
                                 @RequestBody CommentFilterDTO dto){
        return ResponseEntity.ok(commentService.filterSpec(page, size, dto));
    }

}
