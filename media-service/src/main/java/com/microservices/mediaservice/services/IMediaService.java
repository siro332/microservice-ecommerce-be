package com.microservices.mediaservice.services;

import com.microservices.mediaservice.models.entities.Media;

import java.util.List;

public interface IMediaService {
    public List<Media> getAllMediaUrl();
    public List<Media> getMediaByCode(String code);
    public List<Media> getMediaByType(String type);
    public Media addMediaUrl(Media media);
}
