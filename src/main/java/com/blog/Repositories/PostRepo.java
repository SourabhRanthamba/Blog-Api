package com.blog.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;

public interface PostRepo extends JpaRepository<Post,Integer> {

	List<Post> findByuser(User user);
	List<Post> findByCategory(Category category);
	List<Post> findByTitleContaining(String title);

}
