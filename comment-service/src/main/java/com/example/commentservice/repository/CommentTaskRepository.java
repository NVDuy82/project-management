package com.example.commentservice.repository;

import com.example.commentservice.enity.CommentTask;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentTaskRepository extends MongoRepository<CommentTask, String> {
    List<CommentTask> findAllByTaskId(String taskId);
    List<CommentTask> findAllByAuthorId(String authorId);
}
