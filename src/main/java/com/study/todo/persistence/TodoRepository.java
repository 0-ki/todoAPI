package com.study.todo.persistence;

import com.study.todo.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {
    TodoEntity findByTitleLike(String title);

    TodoEntity findByTitle(String title);
//    @Query("select * from Todo t where t.userId = ?1")
//    List<TodoEntity> findByUserId( String userId);

//    @Query("select * from Todo t where t.title like %?1%")
//    TodoEntity findByTitle(String title);
}
