package com.crio.starter.repositoryImplementation;

import java.util.List;
import com.crio.starter.data.MemeEntity;

public interface IMemeRepo {

    public List<MemeEntity> getMemes();

    public MemeEntity saveMeme(MemeEntity entity);

    public MemeEntity findMemeById(String id);

    public MemeEntity findMemesWithFields(MemeEntity entity);

}
