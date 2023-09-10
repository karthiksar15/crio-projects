package com.crio.starter.repositoryImplementation;

import java.util.List;
import com.crio.starter.data.MemeEntity;
import com.crio.starter.repository.MemeRepository;
import com.mongodb.internal.client.model.FindOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class MemeRepoImpl implements IMemeRepo {

    @Autowired
    private MemeRepository memeRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<MemeEntity> getMemes() {
        // TODO Auto-generated method stub
        return memeRepository.findAll();
    }

    @Override
    public MemeEntity saveMeme(MemeEntity entity) {
        // TODO Auto-generated method stub
        return memeRepository.save(entity);
    }
    @Override
    public MemeEntity findMemeById(String id)
    {
        return memeRepository.findById(id).isPresent()?memeRepository.findById(id).get():null;
    }

    @Override
    public MemeEntity findMemesWithFields(MemeEntity entity)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(entity.getName()).and("caption").is(entity.getCaption()).and("url").is(entity.getUrl()));
        MemeEntity memeEntity= mongoTemplate.findOne(query,MemeEntity.class);
        return memeEntity;
    }
    

    
}
