package com.microservices.mediaservice.services.impl;

import com.microservices.mediaservice.models.entities.Media;
import com.microservices.mediaservice.repositories.MediaRepository;
import com.microservices.mediaservice.services.IMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements IMediaService {
    private final MediaRepository mediaRepository;

    private final KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public List<Media> getAllMediaUrl() {
        return mediaRepository.findAll();
    }

    @Override
    public List<Media> getMediaByCode(String code) {
        return mediaRepository.findByCode(code);
    }

    @Override
    public List<Media> getMediaByType(String type) {
        return mediaRepository.findByType(type);
    }

    @Override
    public Media addMediaUrl(Media media) {
        if (media.getType().equals("product")){
            sendToKafka(media.getCode());
        }
        return mediaRepository.save(media);
    }

    private void  sendToKafka(String message){
        kafkaTemplate.send("product",message);
        kafkaTemplate.flush();
    }
}
