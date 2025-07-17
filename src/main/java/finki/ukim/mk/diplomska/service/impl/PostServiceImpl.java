package finki.ukim.mk.diplomska.service.impl;

import finki.ukim.mk.diplomska.model.ApplicationUser;
import finki.ukim.mk.diplomska.model.DonationCategory;
import finki.ukim.mk.diplomska.model.Post;
import finki.ukim.mk.diplomska.model.PostCategory;
import finki.ukim.mk.diplomska.model.dto.PostDto;
import finki.ukim.mk.diplomska.model.exception.PostNotFoundException;
import finki.ukim.mk.diplomska.model.exception.UserNotFoundException;
import finki.ukim.mk.diplomska.model.projections.MostPopularDonationCategories;
import finki.ukim.mk.diplomska.repository.PostRepository;
import finki.ukim.mk.diplomska.service.PostService;
import finki.ukim.mk.diplomska.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final  PostRepository postRepository;

    private final UserService userService;

    public PostServiceImpl(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    public Post findById(UUID uuid) throws PostNotFoundException {
        return postRepository.findById(uuid).orElseThrow(()-> new PostNotFoundException(uuid));
    }

    @Override
    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }



    @Override
    public Post addNewPost(PostDto postDto, MultipartFile image) throws IOException {
        Post post = new Post();
        ApplicationUser user = new ApplicationUser();

        if (postDto == null) throw new IllegalArgumentException("Post data cannot be null");

        try {
            user = userService.findById(postDto.getUserUUID());
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }


//        if(postDto.getMoneyDonationLimit()==0.0)
//        {
//            post = new Post(postDto.getPostTitle(),postDto.getDescription(), postDto.getPostCategory(), postDto.getDonationCategory(), user);
//        }else {
//            post =  new Post(postDto.getPostTitle(), postDto.getDescription(),postDto.getMoneyDonationLimit(), postDto.getPostCategory(), postDto.getDonationCategory(), user);
//        }

        post =  new Post(postDto.getPostTitle(), postDto.getDescription(),postDto.isDonateMoney(),postDto.getMoneyDonationLimit(), postDto.getPostCategory(), postDto.getDonationCategory(), user);


        if(image != null && !image.isEmpty()) {
            post.setImageName(image.getOriginalFilename());
            post.setImageType(image.getContentType());
            post.setImageData(image.getBytes());
        }

        return  postRepository.save(post);
    }

    @Override
    public Post editPost(UUID uuid, PostDto postDto) throws PostNotFoundException {
        Post post = findById(uuid);
        ApplicationUser user = new ApplicationUser();

        if (postDto == null) throw new IllegalArgumentException("Post data cannot be null");

        try {
            user = userService.findById(postDto.getUserUUID());
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }


        post.setPostTitle(postDto.getPostTitle());
        post.setDescription(postDto.getDescription());
        post.setPostCategory(postDto.getPostCategory());
        post.setDonationCategory(postDto.getDonationCategory());
        post.setUser(user);
        postRepository.save(post);
        return post;
    }

    @Override
    public Post deletePost(UUID uuid) throws PostNotFoundException {
        Post post = findById(uuid);
        postRepository.delete(post);
        return post;

    }

    @Override
    public Post like(UUID uuid) throws PostNotFoundException {
        Post post = findById(uuid);
        Integer numLikes = post.getLikes() + 1;
        post.setLikes(numLikes);
        postRepository.save(post);
        return post;
    }

    @Override
    public List<Post> findPostsByUser(UUID uuid) {
        try {
            ApplicationUser appUser = userService.findById(uuid);
            return postRepository.findByUser(appUser);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Post> findPostsByDonationCategory(DonationCategory donationCategory) {
        return postRepository.findByDonationCategory(donationCategory);
    }

    @Override
    public List<Post> findPostsByPostCategory(PostCategory postCategory) {
        return postRepository.findByPostCategory(postCategory);
    }

    @Override
    public List<Post> findPostsByPostCategoryAndDonationCategory(PostCategory postCategory, DonationCategory donationCategory) {
        return postRepository.findByPostCategoryAndDonationCategory(postCategory,donationCategory);
    }
    @Override
    public List<Post> findMostPopularPosts() {
        return  postRepository.findMostPopularPosts();
    }

    @Override
    public List<MostPopularDonationCategories> findMostPopularDonationCatgories() {
        return postRepository.findMostPopularDonationCategories();
    }
}
