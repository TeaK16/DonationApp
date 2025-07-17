package finki.ukim.mk.diplomska.repository;

import finki.ukim.mk.diplomska.model.ApplicationUser;
import finki.ukim.mk.diplomska.model.DonationCategory;
import finki.ukim.mk.diplomska.model.Post;
import finki.ukim.mk.diplomska.model.PostCategory;
import finki.ukim.mk.diplomska.model.projections.MostPopularDonationCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByUser(ApplicationUser user);
    List<Post> findByDonationCategory(DonationCategory donationCategory);
    List<Post> findByPostCategory(PostCategory postCategory);
    List<Post> findByPostCategoryAndDonationCategory(PostCategory postCategory, DonationCategory donationCategory);
    @Query(value = "SELECT * FROM post ORDER BY likes DESC LIMIT 10", nativeQuery = true)
    List<Post> findMostPopularPosts();
    @Query(value = "SELECT donation_category, sum(likes) AS likes FROM post GROUP BY donation_category ORDER BY sum(likes) DESC", nativeQuery = true)
    List<MostPopularDonationCategories> findMostPopularDonationCategories();



}
