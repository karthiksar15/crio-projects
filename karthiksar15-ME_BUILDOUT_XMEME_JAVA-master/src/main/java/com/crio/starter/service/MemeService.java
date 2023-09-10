package com.crio.starter.service;

import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import com.crio.starter.data.MemeEntity;
import com.crio.starter.exchange.MemeRequest;
import com.crio.starter.exchange.MemeResponseAll;
import com.crio.starter.exchange.MemeResponseDto;
import com.crio.starter.model.Meme;
import com.crio.starter.repositoryImplementation.IMemeRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemeService {

    @Autowired
    private IMemeRepo memeRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    public MemeResponseAll getMemes() {
        objectMapper.registerModule(new JavaTimeModule());
        List<MemeEntity> memeEntity = memeRepository.getMemes();
        if (!memeEntity.isEmpty()) {
            List<Meme> memeList = memeEntity.stream()
                    .map((meme) -> objectMapper.convertValue(meme, Meme.class))
                    .sorted(Comparator.comparing(Meme::getTimestamp).reversed())
                    .limit(100)
                    .collect(Collectors.toList());
            MemeResponseAll memeResponseDto = new MemeResponseAll(memeList);
            return memeResponseDto;
        } else {
            return new MemeResponseAll(new ArrayList<>());
        }

    }

    public MemeResponseDto saveMeme(MemeRequest memeRequest) {
        objectMapper.registerModule(new JavaTimeModule());
        MemeEntity memeEntity = new ObjectMapper().convertValue(memeRequest, MemeEntity.class);
        memeEntity.setTimestamp(LocalDateTime.now());
        memeEntity = memeRepository.saveMeme(memeEntity);
        MemeResponseDto memeResponseDto = objectMapper.convertValue(memeEntity,MemeResponseDto.class);
        return memeResponseDto;
    }


    public MemeResponseDto findMeme(String id) {
        objectMapper.registerModule(new JavaTimeModule());
        MemeEntity memeEntity = memeRepository.findMemeById(id);
        if(memeEntity!=null)
        {
            MemeResponseDto memeResponseDto = objectMapper.convertValue(memeEntity, MemeResponseDto.class);
            return memeResponseDto;
        }else
        {
            return null;
        }
       
    }

    public MemeResponseDto findMemeWithFields(MemeRequest memeRequest)
    {
        objectMapper.registerModule(new JavaTimeModule());
        MemeEntity memeEntity = new ObjectMapper().convertValue(memeRequest, MemeEntity.class);
        memeEntity = memeRepository.findMemesWithFields(memeEntity);
        if(memeEntity!=null)
        {
            MemeResponseDto memeResponseDto = objectMapper.convertValue(memeEntity, MemeResponseDto.class);
            return memeResponseDto;
        }else
        {
            return null;
        }
        
    }



}
