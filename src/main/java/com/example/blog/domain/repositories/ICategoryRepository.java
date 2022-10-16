package com.example.blog.domain.repositories;

import com.example.blog.domain.entities.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepository extends CrudRepository<Category, Integer> {

    @Query(nativeQuery = true,
            value = "select c.id, c.name, count(*) as postsNumber " +
                    "from categories as c left join posts as p on c.id = p.category_id " +
                    "where p.status_id = :statusId " +
                    "group by c.id")
    List<Object[]> getCategoriesAndCountPostsForEachByStatusId(@Param("statusId") int statusId);

    Optional<Category> findCategoriesByName(String name);
}
