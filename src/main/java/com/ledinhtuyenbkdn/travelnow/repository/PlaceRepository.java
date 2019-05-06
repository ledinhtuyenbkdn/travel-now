package com.ledinhtuyenbkdn.travelnow.repository;

import com.ledinhtuyenbkdn.travelnow.model.Place;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends Neo4jRepository<Place, Long> {

    List<Place> findAllByNamePlaceContains(String s);

    @Query("match (p:Place)-[r1:IS_IN]->(province:Province), (p:Place)-[r2:IS_BELONGS_TO]->(category:Category), (p:Place)-[r3:HAS_IMAGE]->(image:Image) where ID(category)={0} return r1,r2,r3,p,province,category;")
    List<Place> findAllByCategoryId(Long id);

    @Query("match (p:Place)-[r1:IS_IN]->(province:Province), (p:Place)-[r2:IS_BELONGS_TO]->(category:Category), (p:Place)-[r3:HAS_IMAGE]->(image:Image) where ID(province)={0} return r1,r2,r3,p,province,category;")
    List<Place> findAllByProvinceId(Long id);

    @Query("match (p:Place)-[r1:IS_IN]->(province:Province), (p:Place)-[r2:IS_BELONGS_TO]->(category:Category), (p:Place)-[r3:HAS_IMAGE]->(image:Image) where lower(p.namePlace) contains lower({0}) and ID(category)={1} return r1,r2,r3,p,province,category;")
    List<Place> findAllByNamePlaceContainsAndCategoryId(String s, Long id);

    @Query("match (p:Place)-[r1:IS_IN]->(province:Province), (p:Place)-[r2:IS_BELONGS_TO]->(category:Category), (p:Place)-[r3:HAS_IMAGE]->(image:Image) where lower(p.namePlace) contains lower({0}) and ID(province)={1} return r1,r2,r3,p,province,category;")
    List<Place> findAllByNamePlaceContainsAndProvinceId(String s, Long id);

    @Query("match (p:Place)-[r1:IS_IN]->(province:Province), (p:Place)-[r2:IS_BELONGS_TO]->(category:Category), (p:Place)-[r3:HAS_IMAGE]->(image:Image) where lower(p.namePlace) contains lower({0}) and ID(province)={1} and ID(category)={2} return r1,r2,r3,p,province,category;")
    List<Place> findAllByNamePlaceContainsAndProvinceIdAndCategoryId(String s, Long provinceId, Long categoryId);

    @Query("match (p:Place)-[r1:IS_IN]->(province:Province), (p:Place)-[r2:IS_BELONGS_TO]->(category:Category), (p:Place)-[r3:HAS_IMAGE]->(image:Image), (p1:Place) where ID(p1)={0} return r1,r2,r3,p,province,category order by ((p1.latitude-p.latitude)*(p1.latitude-p.latitude) + (p1.longitude-p.longitude) * (p1.longitude-p.longitude)) limit 4;")
    List<Place> findSuggestPlaces(Long id);
}
