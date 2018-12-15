package com.boilerplate.missionDatabase.mission;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "missionsRepo", path = "missionsRepo")
public interface MissionRepository extends MongoRepository<Mission, String> {
    Mission findByName(@Param("name") String missionName);
    List<Mission> findByType(@Param("type") MissionType type);
}
