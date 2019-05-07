package com.ledinhtuyenbkdn.travelnow.repository;

import com.ledinhtuyenbkdn.travelnow.model.Place;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends Neo4jRepository<Place, Long> {

    @Query("match (p:Place) \n" +
            "where lower(p.namePlace) contains lower({0})\n" +
            "with p\n" +
            "order by p.createdAt desc\n" +
            "OPTIONAL MATCH (p:Place)-[r1:IS_IN]->(province:Province)\n" +
            "OPTIONAL MATCH (p:Place)-[r2:IS_BELONGS_TO]->(category:Category) \n" +
            "OPTIONAL MATCH (p:Place)-[r3:HAS_IMAGE]->(image:Image)\n" +
            "return r1,r2,r3,p,province,category, image;")
    List<Place> findAllByNamePlaceContains(String s);

    @Query("match (p:Place)-[r1:IS_IN]->(province:Province), (p:Place)-[r2:IS_BELONGS_TO]->(category:Category), (p:Place)-[r3:HAS_IMAGE]->(image:Image) where ID(category)={0} return r1,r2,r3,p,province,category;")
    List<Place> findAllByCategoryId(Long id);

    @Query("match (p:Place)-[r1:IS_IN]->(province:Province), (p:Place)-[r2:IS_BELONGS_TO]->(category:Category), (p:Place)-[r3:HAS_IMAGE]->(image:Image) where ID(province)={0} return r1,r2,r3,p,province,category;")
    List<Place> findAllByProvinceId(Long id);

    @Query("match (p:Place)-[r2:IS_BELONGS_TO]->(category:Category)\n" +
            "where lower(p.namePlace) contains lower({0}) and ID(category)={1}\n" +
            "with p, r2, category\n" +
            "order by p.createdAt desc\n" +
            "OPTIONAL MATCH (p:Place)-[r1:IS_IN]->(province:Province)\n" +
            "OPTIONAL MATCH (p:Place)-[r3:HAS_IMAGE]->(image:Image)  \n" +
            "return r1,r2,r3,p,province,category, image;")
    List<Place> findAllByNamePlaceContainsAndCategoryId(String s, Long id);

    @Query("match (p:Place)-[r1:IS_IN]->(province:Province)\n" +
            "where lower(p.namePlace) contains lower({0}) and ID(province)={1}\n" +
            "with p, r1, province\n" +
            "order by p.createdAt desc\n" +
            "OPTIONAL MATCH (p:Place)-[r2:IS_BELONGS_TO]->(category:Category)\n" +
            "OPTIONAL MATCH (p:Place)-[r3:HAS_IMAGE]->(image:Image)  \n" +
            "return r1,r2,r3,p,province,category, image;")
    List<Place> findAllByNamePlaceContainsAndProvinceId(String s, Long id);

    @Query("match (p:Place)-[r1:IS_IN]->(province:Province), \n" +
            "(p:Place)-[r2:IS_BELONGS_TO]->(category:Category)\n" +
            "where lower(p.namePlace) contains lower(\"\") and ID(province)=279 and ID(category)=0\n" +
            "with p, r1, r2, province, category\n" +
            "order by p.createdAt desc\n" +
            "OPTIONAL MATCH (p:Place)-[r3:HAS_IMAGE]->(image:Image) \n" +
            "return r1,r2,r3,p,province,category, image;")
    List<Place> findAllByNamePlaceContainsAndProvinceIdAndCategoryId(String s, Long provinceId, Long categoryId);

    @Query("match (p:Place), (p1:Place) where ID(p1)={0} and ID(p) <> ID(p1)\n" +
            "with p, p1 \n" +
            "order by ((p1.latitude-p.latitude)*(p1.latitude-p.latitude) + (p1.longitude-p.longitude) * (p1.longitude-p.longitude)) \n" +
            "limit 8 \n" +
            "OPTIONAL MATCH (p:Place)-[r1:IS_IN]->(province:Province)\n" +
            "OPTIONAL MATCH (p:Place)-[r2:IS_BELONGS_TO]->(category:Category)\n" +
            "OPTIONAL MATCH (p:Place)-[r3:HAS_IMAGE]->(image:Image) \n" +
            "return r1,r2,r3,p,province,category, image;")
    List<Place> findSuggestPlaces(Long id);
}
