package com.example.userservice.controller;

import com.example.userservice.models.UserReviewDto;
import com.example.userservice.models.entities.UserInfo;
import com.example.userservice.models.entities.UserReview;
import com.example.userservice.services.UserInfoService;
import com.example.userservice.services.UserReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserInfoService userInfoService;
    private final UserReviewService userReviewService;
    @GetMapping("/infos")
    public List<UserInfo> getAllMediaUrl(){
        return userInfoService.getAllUserInfo();
    }
    @GetMapping("/info")
    public UserInfo getUserInfoByCode(JwtAuthenticationToken authentication){
        return userInfoService.getUserInfoByCode(authentication.getName());
    }
    @PostMapping("/info/add")
    public ResponseEntity<?> addUserInfo(JwtAuthenticationToken authentication, @RequestBody UserInfo userInfo){
        try{
            userInfo.setCode(authentication.getName());
            return ResponseEntity.ok(userInfoService.addUserInfo(userInfo));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error creating user: "+ e.getMessage());
        }
    }
    @GetMapping("/review/{code}")
    public ResponseEntity<Map<String,Object>> getUserReviews(@PathVariable String code,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "name") String sortParam,
                                              @RequestParam(defaultValue = "0") int sortDirection){
        Page<UserReviewDto> userReviewDtoPage;
        if (sortDirection == 0) {
            userReviewDtoPage = userReviewService.getUserReviewByProductCode(code,PageRequest.of(page, size, Sort.by(sortParam)));
        } else {
            userReviewDtoPage = userReviewService.getUserReviewByProductCode(code,PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortParam)));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("products", userReviewDtoPage.getContent());
        response.put("currentPage", userReviewDtoPage.getNumber());
        response.put("totalItems", userReviewDtoPage.getTotalElements());
        response.put("totalPages", userReviewDtoPage.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/review/add")
    public ResponseEntity<?> addUserReview(JwtAuthenticationToken authentication, @RequestBody UserReview userReview){
        try{
            userReview.setUserCode(authentication.getName());
            return ResponseEntity.ok(userReviewService.addUserReview(userReview));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error creating review: "+ e.getMessage());
        }
    }
}
