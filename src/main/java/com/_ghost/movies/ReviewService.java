package com._ghost.movies;


import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.security.Key;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Review createReview(String reviewBody, String imdbId) {
        // Create and insert the review
        Review review = reviewRepository.insert(new Review(reviewBody));

        // Update the movie to include the new review
        UpdateResult result = mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviewIds").value(review.getId())) // Assuming Review has a getId() method
                .first();

        // Optionally, you might want to handle the result of the update operation here

        return review;
    }
}