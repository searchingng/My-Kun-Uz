package com.company.controller;

import com.company.dto.MessageDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.enums.Role;
import com.company.service.EmailService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private EmailService emailService;


    @GetMapping("/{id}")
    public MessageDTO getById(@PathVariable("id") Integer id,
                              HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);

        return emailService.getById(id);
    }

    @GetMapping("/last")
    public MessageDTO getById(HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);

        return emailService.getLast();
    }


    @GetMapping("/today")
    public ResponseEntity getByPid(@PathVariable Integer pid,
                                   HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);
        return ResponseEntity.ok(emailService.getTodays(pid));
    }

    @GetMapping("unused")
    public ResponseEntity getUnused(@RequestParam("page") int page,
                                   @RequestParam("size") int size,
                                    HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);
        return ResponseEntity.ok(emailService.getUnused(page, size));
    }

    @GetMapping
    public ResponseEntity getAll(@RequestParam("page") int page,
                                 @RequestParam("size") int size,
                                 HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);
        return ResponseEntity.ok(emailService.getAll(page, size));
    }

}
