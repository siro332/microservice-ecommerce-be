package com.example.userservice.services;

import com.example.userservice.converters.DtoConverter;
import com.example.userservice.models.UserReviewDto;
import com.example.userservice.models.entities.UserInfo;
import com.example.userservice.models.entities.UserReview;
import com.example.userservice.repositories.UserInfoRepository;
import com.example.userservice.repositories.UserReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserReviewService {
    private final UserReviewRepository userReviewRepository;
    private final UserInfoRepository userInfoRepository;

    private final DtoConverter dtoConverter;

    private final KafkaTemplate<String,String> kafkaTemplate;

    public Page<UserReviewDto> getUserReviewByProductCode(String code, Pageable pageable) {
        Page<UserReview> userReviews = userReviewRepository.findByProductCode(code,pageable);
        Page<UserReviewDto> userReviewDtos = userReviews.map(userReview -> {
            UserInfo userInfo = userInfoRepository.findByCode(userReview.getUserCode()).get();
            UserReviewDto userReviewDto = dtoConverter.userReviewEntityToDto(userReview);
            userReviewDto.setUserInfo(userInfo);
            return userReviewDto;
        });
        return userReviewDtos;
    }

    public UserReview addUserReview(UserReview userReview) {
        UserReview savedUserReview =  userReviewRepository.save(userReview);
        sendToKafka(savedUserReview.getProductCode());
        return savedUserReview;
    }

    private void sendToKafka(String message){
        kafkaTemplate.send("product",message);
        kafkaTemplate.flush();
    }

    private double calculateAvgReviewScore(String productCode){
        return userReviewRepository.findAllByProductCode(productCode).stream().mapToDouble(UserReview::getRate).average().getAsDouble();
    }
}
