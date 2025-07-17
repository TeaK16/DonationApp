package finki.ukim.mk.diplomska.service;

import finki.ukim.mk.diplomska.model.DonationCategory;
import finki.ukim.mk.diplomska.model.Post;
import finki.ukim.mk.diplomska.model.PostCategory;
import finki.ukim.mk.diplomska.model.dto.PostDto;
import finki.ukim.mk.diplomska.model.exception.PostNotFoundException;
import finki.ukim.mk.diplomska.model.projections.MostPopularDonationCategories;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PostService {
    Post findById(UUID uuid) throws PostNotFoundException;
    List<Post> findAllPosts();
    Post editPost(UUID uuid, PostDto postDto) throws PostNotFoundException;
    Post deletePost(UUID uuid) throws PostNotFoundException;
    Post like(UUID uuid) throws PostNotFoundException;
    Post addNewPost(PostDto postDto, MultipartFile image) throws IOException;
    List<Post> findPostsByUser(UUID uuid);
    List<Post> findPostsByDonationCategory(DonationCategory donationCategory);
    List<Post> findPostsByPostCategory(PostCategory postCategory);

    List<Post>findPostsByPostCategoryAndDonationCategory(PostCategory postCategory, DonationCategory donationCategory);
    List<Post> findMostPopularPosts();
    List<MostPopularDonationCategories> findMostPopularDonationCatgories();


}
