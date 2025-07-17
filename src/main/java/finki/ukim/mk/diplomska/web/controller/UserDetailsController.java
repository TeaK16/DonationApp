package finki.ukim.mk.diplomska.web.controller;


import finki.ukim.mk.diplomska.model.ApplicationUser;
import finki.ukim.mk.diplomska.model.Post;
import finki.ukim.mk.diplomska.model.exception.UserNotFoundException;
import finki.ukim.mk.diplomska.service.PostService;
import finki.ukim.mk.diplomska.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/user/details")
public class UserDetailsController {
    private final UserService userService;
    private final PostService postService;

    public UserDetailsController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }


    @GetMapping("/{id}")
    public ApplicationUser getUserDetails(@PathVariable UUID id){
        try {
            return userService.findById(id);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/post/{id}")
    public List<Post> listPostsByUser(@PathVariable UUID id){
        return postService.findPostsByUser(id);
    }

    @GetMapping("/search")
    public List<ApplicationUser> searchUsernameList(@RequestParam String substring)
    {
        return userService.searchUsernameBySubstring(substring);
    }



}
