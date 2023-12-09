package com.blog.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blog.payloads.ApiResponse;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;
import com.blog.services.FileService;
import com.blog.services.PostService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/")
public class PostController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${project.image}")
	private String path;
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto>createPost(@RequestBody PostDto postDto ,@PathVariable Integer userId,@PathVariable Integer categoryId){
	
		PostDto createPost = this.postService.createPost(postDto, userId, categoryId);
		
		return new ResponseEntity<PostDto>(createPost,HttpStatus.CREATED);
		
	}
	
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>>getPostByuser(@PathVariable Integer userId){
	
		List<PostDto> posts = this.postService.getPostByUser(userId);
		
		return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
		
	}
	
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>>getPostByCategory(@PathVariable Integer categoryId){
	 
		List<PostDto> posts = this.postService.getPostByCategory(categoryId);
		
		return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
		
	}
	
	@GetMapping("/posts")
	
	public ResponseEntity<PostResponse> getAllpost(@RequestParam(value="pageNumber",defaultValue = "1",required = false) Integer pageNumber,@RequestParam(value="pageSize",defaultValue = "5",required = false)Integer pageSize, @RequestParam(value="sortBy",defaultValue = "postId",required = false)String sortBy){
		
		PostResponse allPost = this.postService.getAllPost(pageNumber,pageSize,sortBy);
	   
		return new ResponseEntity<PostResponse>(allPost,HttpStatus.OK);
	
	}
	
@GetMapping("/posts/{postId}")
	
	public ResponseEntity<PostDto> getpostById(@PathVariable Integer postId){
		
		PostDto allPost = this.postService.getPostById(postId);
	   
		return new ResponseEntity<PostDto>(allPost,HttpStatus.OK);
	
	}


@DeleteMapping("/posts/{postId}")
   public ApiResponse deletepost(@PathVariable Integer postId) {
	this.postService.deletePost(postId);
	
	return new ApiResponse("post is successfully deleted !!",true);
}


@PutMapping("/posts/{postId}")
public ResponseEntity<PostDto> updatepost(@RequestBody PostDto postDto,@PathVariable Integer postId) {
	
	PostDto updatePost = this.postService.updatePost(postDto, postId);
	
	return new ResponseEntity<PostDto>(updatePost,HttpStatus.OK);
	
}
	
@GetMapping("/posts/search/{keywords}")
public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keywords") String keywords){
	
	List<PostDto> searchPosts = this.postService.searchPosts(keywords);
	return new ResponseEntity<List<PostDto>>(searchPosts,HttpStatus.OK);
	
}

@PostMapping("/post/image/upload/{postId}")
public ResponseEntity<PostDto> uploadPostImage( @RequestParam("image") MultipartFile image,@PathVariable Integer postId) throws IOException{
	PostDto postById = this.postService.getPostById(postId);

	String uploadImage = this.fileService.uploadImage(path, image);

          postById.setImageName(uploadImage);
          PostDto updatePost = this.postService.updatePost(postById, postId);
          
          return new ResponseEntity<PostDto>(updatePost,HttpStatus.OK);
}

@GetMapping(value="post/image/{imageName}",produces=MediaType.IMAGE_JPEG_VALUE)
public void downloadImage(@PathVariable("imageName") String imageName,HttpServletResponse response) throws IOException {
	InputStream resources=this.fileService.getResource(path, imageName);
	response.setContentType(MediaType.IMAGE_JPEG_VALUE);
	
	StreamUtils.copy(resources, response.getOutputStream());
}

}
