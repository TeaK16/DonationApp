package finki.ukim.mk.diplomska.web.controller;


import finki.ukim.mk.diplomska.model.DonationCategory;
import finki.ukim.mk.diplomska.model.Post;
import finki.ukim.mk.diplomska.model.PostCategory;
import finki.ukim.mk.diplomska.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/posts/search")
public class FiltersController {

    private final PostService postService;

    public FiltersController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/categories")
    public List<Post> getFilteredPosts(
            @RequestParam(required = false) PostCategory postCategory,
            @RequestParam(required = false) DonationCategory donationCategory) {
        if(postCategory!= null && donationCategory == null){
            return postService.findPostsByPostCategory(postCategory);
        }else if(postCategory == null && donationCategory != null){
            return postService.findPostsByDonationCategory(donationCategory);
        }else{
            return postService.findPostsByPostCategoryAndDonationCategory(postCategory, donationCategory);

        }

    }

    @GetMapping("/mostPopularPosts")
    public List<Post> getMostPopularPosts(){
        return postService.findMostPopularPosts();
    }
}
