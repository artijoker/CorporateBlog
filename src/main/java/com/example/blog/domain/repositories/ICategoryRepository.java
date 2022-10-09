package com.example.blog.domain.repositories;

import com.example.blog.domain.entities.Category;
import com.example.blog.domain.entities.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICategoryRepository extends CrudRepository<Category, Integer> {

    @Query(nativeQuery = true,
            value = "select c.Id, c.Name, count(*) as postsNumber " +
                    "from categories as c left join posts as p on c.Id = p.CategoryId " +
                    "where p.StatusId = :statusId " +
                    "group by a.Id")
    List<Object[]> GetCategoriesAndCountPostsForEachCategoryByStatusId(@Param("statusId") int statusId);
}
