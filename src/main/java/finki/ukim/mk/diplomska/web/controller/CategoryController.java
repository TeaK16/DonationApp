package finki.ukim.mk.diplomska.web.controller;


import finki.ukim.mk.diplomska.model.DonationCategory;
import finki.ukim.mk.diplomska.model.PostCategory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = "http://localhost:8081")
public class CategoryController {

    @GetMapping("/posttype")
    public List<PostCategory> getPostCategory(){
        return List.of(PostCategory.values());
    }


    @GetMapping("/donationtype")
    public List<DonationCategory> getDonationCategory(){
        return List.of(DonationCategory.values());
    }

}
