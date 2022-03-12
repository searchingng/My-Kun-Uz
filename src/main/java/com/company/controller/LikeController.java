package com.company.controller;

import com.company.dto.LikeDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.enums.LikeStatus;
import com.company.service.LikeService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping
    public ResponseEntity like(@RequestBody LikeDTO dto,
                               HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        return ResponseEntity.ok(likeService.createLike(dto, jwtDTO.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody LikeDTO dto,
                                 @PathVariable("id") Long id,
                                 HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        likeService.updateById(id, dto);
        return ResponseEntity.ok("SUCCESS");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id,
                                 HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        likeService.deleteById(id);
        return ResponseEntity.ok("SUCCESS");
    }

    @GetMapping("/count/{status}/article/{aid}")
    public ResponseEntity countArticle(@PathVariable("status") String status,
                                       @PathVariable("aid") Integer aid){
        return ResponseEntity
                .ok(likeService.countArticleLikes(aid, LikeStatus.valueOf(status)));
    }

    @GetMapping("/count/{status}/comment/{cid}")
    public ResponseEntity countComment(@PathVariable("status") String status,
                                       @PathVariable("cid") Integer cid){
        return ResponseEntity
                .ok(likeService.countCommentLikes(cid, LikeStatus.valueOf(status)));
    }

    @GetMapping("/articles")
    public ResponseEntity getArticles(HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        return ResponseEntity.ok(likeService.getLikedArticlesByPid(jwtDTO.getId()));
    }

    @GetMapping("/comments")
    public ResponseEntity getComments(HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        return ResponseEntity.ok(likeService.getLikedCommentsByPid(jwtDTO.getId()));
    }


}
