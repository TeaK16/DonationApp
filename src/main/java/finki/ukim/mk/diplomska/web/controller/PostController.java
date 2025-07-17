package finki.ukim.mk.diplomska.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import  finki.ukim.mk.diplomska.model.Post;
import finki.ukim.mk.diplomska.model.dto.PostDto;
import finki.ukim.mk.diplomska.model.exception.PostNotFoundException;
import finki.ukim.mk.diplomska.model.projections.MostPopularDonationCategories;
import finki.ukim.mk.diplomska.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/posts")
public class PostController {
    private static final String UPLOAD_DIR = "src/main/resources/static/Images/";
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<Post> listAllPosts()
    {
        return postService.findAllPosts();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable UUID id){
        try {
            return postService.findById(id);
        } catch (PostNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @PutMapping("/edit/{id}")
    public  ResponseEntity<?> editPost(@PathVariable UUID id, @RequestBody PostDto postDto)
    {
        try {
            postService.editPost(id, postDto);
        } catch (PostNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>("Post edited successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable UUID id)  {
        try {
            postService.deletePost(id);
        } catch (PostNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>("Post deleted successfully.", HttpStatus.OK);
    }

    @PostMapping("/like/{id}")
    public ResponseEntity<Post> likePost(@PathVariable UUID id)
    {

        try {
            Post post = postService.like(id);
            return ResponseEntity.ok(post);
        } catch (PostNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping(value = "/add", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Post> addPost(@RequestPart(value = "postDto") String postDtoStr,
                                        @RequestPart(value = "image", required = false) MultipartFile image){


        try{
            if (image != null) {
                File directory = new File(UPLOAD_DIR);

                if (!directory.exists()) {
                    directory.mkdirs();
                }
                Path filePath = Paths.get(UPLOAD_DIR + image.getOriginalFilename());
                Files.write(filePath, image.getBytes());
            }

            PostDto postDto = new ObjectMapper().readValue(postDtoStr, PostDto.class);
            postService.addNewPost(postDto, image);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (IOException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @GetMapping("/trendingdonations")
    public ResponseEntity<List<MostPopularDonationCategories>> findMostPopularDonationCategories(){
        List<MostPopularDonationCategories> trendingDonations =  postService.findMostPopularDonationCatgories();
        return ResponseEntity.ok(trendingDonations);
    }


}
