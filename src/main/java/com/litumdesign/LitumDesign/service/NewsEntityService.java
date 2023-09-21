package com.litumdesign.LitumDesign.service;

import com.litumdesign.LitumDesign.Entity.NewsEntity;
import com.litumdesign.LitumDesign.Entity.ProductEntity;
import com.litumdesign.LitumDesign.repository.NewsEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLDataException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class NewsEntityService {

    private final NewsEntityRepository newsEntityRepository;

    @Transactional
    public boolean addNewNews(NewsEntity newsEntity){
       try {
           newsEntityRepository.save(newsEntity);
       }catch (Exception e){
           e.printStackTrace();
       }
        return true;
    }

    public Page<NewsEntity> getAllNews (Pageable pageable){
        Page<NewsEntity> news = newsEntityRepository.findAllByOrderByCreatedAt(pageable);

        return news;
    }


    public NewsEntity getParticularNews(Long newsId){

        return newsEntityRepository.findById(newsId).orElseThrow(() -> new NullPointerException("News not found"));
    }
}
