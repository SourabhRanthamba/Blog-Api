package com.blog.services.impl;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.Repositories.CategoryRepo;
import com.blog.Repositories.PostRepo;
import com.blog.Repositories.UserRepo;
import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exception.ResourceNotFoundException;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;
import com.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService{

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private ModelMapper modelmapper;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;

	@Override
	public PostDto createPost(PostDto postDto,Integer userId,Integer categoryId) {

		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","user id",userId));
		
		Category category=this.categoryRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("Category","category id",categoryId));

		
		Post post = this.modelmapper.map(postDto, Post.class);
		
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		
		Post post2 = this.postRepo.save(post);
		return this.modelmapper.map(post2, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {

		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id", postId));
              post.setTitle(postDto.getTitle());
		      post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		
		Post saved = this.postRepo.save(post);
		return this.modelmapper.map(saved, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {

		
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id", postId));
	      this.postRepo.delete(post);    
	}

	@Override
	public PostResponse getAllPost(Integer pageNumber,Integer pageSize,String sortBy) {

		
		Pageable p= PageRequest.of(pageNumber, pageSize,Sort.by(sortBy));
		Page<Post> pagePost =this.postRepo.findAll(p);
		List<Post> findAll=pagePost.getContent();
		List<PostDto> collect = findAll.stream().map((post)->this.modelmapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		PostResponse postresponse=new PostResponse();
		postresponse.setContent(collect);
		postresponse.setPageNumber(pagePost.getNumber());
		postresponse.setPageSize(pagePost.getSize());
		postresponse.setTotalElements(pagePost.getTotalElements());
		postresponse.setTotalpages(pagePost.getTotalPages());
		postresponse.setLastPage(pagePost.isLast());
		return postresponse ;
	}

	@Override
	public PostDto getPostById(Integer postId) {

		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Post Id", postId));
		return this.modelmapper.map(post, PostDto.class);
	}

	@Override
	public List<PostDto> getPostByCategory(Integer categoryId) {

		Category cat=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","category Id", categoryId));
		List<Post> posts = this.postRepo.findByCategory(cat);
		
		List<PostDto> map = posts.stream().map((post)->this.modelmapper.map(post,PostDto.class)).collect(Collectors.toList());
		
		return map;
	}

	@Override
	public List<PostDto> getPostByUser(Integer userId) {

		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","user id", userId));
		
		List<Post> findByuser = this.postRepo.findByuser(user);
		
		List<PostDto> collect = findByuser.stream().map((post)->this.modelmapper.map(post,PostDto.class)).collect(Collectors.toList());
		return collect;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {

		List<Post> findByTitleContaining = this.postRepo.findByTitleContaining(keyword);
		
		List<PostDto> collect = findByTitleContaining.stream().map((post)->this.modelmapper.map(post,PostDto.class)).collect(Collectors.toList());
		return collect;
	}
	
	


}
