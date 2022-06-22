package com.example.userservice.converters;

import com.example.userservice.models.UserReviewDto;
import com.example.userservice.models.entities.UserInfo;
import com.example.userservice.models.entities.UserReview;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DtoConverter {
    private final ModelMapper modelMapper;
    public UserReviewDto userReviewEntityToDto(UserReview userReview){
        return modelMapper.map(userReview,UserReviewDto.class);
    }
}
