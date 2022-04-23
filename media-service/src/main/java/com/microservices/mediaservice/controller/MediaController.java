package com.microservices.mediaservice.controller;

import com.microservices.mediaservice.models.entities.Media;
import com.microservices.mediaservice.services.IMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {
    private final IMediaService mediaService;
    @GetMapping("")
    public List<Media> getAllMediaUrl(){
        return mediaService.getAllMediaUrl();
    }
    @GetMapping("/{code}")
    public ResponseEntity<HashMap<Object, Object>> getMediaByCode(@PathVariable String code){
        List<Media> mediaList= mediaService.getMediaByCode(code);
        return ResponseEntity.ok().body(new HashMap<>(){{put("mediaList",mediaList);}});
    }
    @GetMapping("/type/{type}")
    public ResponseEntity<HashMap<Object, Object>> getMediaByType(@PathVariable String type){
        List<Media> mediaList= mediaService.getMediaByType(type);
        return ResponseEntity.ok().body(new HashMap<>(){{put("mediaList",mediaList);}});
    }
    @PostMapping("/add")
    public ResponseEntity<?> addMedia(@RequestBody Media media){
        try{
            return ResponseEntity.ok(mediaService.addMediaUrl(media));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error creating media: "+ e.getMessage());
        }
    }
}
