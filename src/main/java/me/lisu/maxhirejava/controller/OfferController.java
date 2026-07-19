package me.lisu.maxhirejava.controller;

import me.lisu.maxhirejava.model.Offer;
import me.lisu.maxhirejava.record.OfferInfo;
import me.lisu.maxhirejava.record.UserInfo;
import me.lisu.maxhirejava.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials = "true"
)
public class OfferController {

    @Autowired
    private OfferRepository offerRepository;

    public record OfferResponse(List<OfferInfo> offers, int totalPages) {}
    public record OfferByIdResponse(List<OfferInfo> message) {}
    public record OfferByUserResponse(List<OfferInfo> message) {}

    @GetMapping("/offers")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllOffers() {
        List<Offer> offers = offerRepository.findAll();

        List<OfferInfo> response = offers.stream()
                .map(offer -> {
                    UserInfo userInfo = new UserInfo(offer.getUser());
                    return new OfferInfo(offer, userInfo);
                })
                .toList();

        final int MAX_PER_PAGE = 7;
        int totalPages = offers.size() / MAX_PER_PAGE;

        return ResponseEntity.ok(new OfferResponse(response, totalPages));
    }

    @GetMapping("/offer/getOffer/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOfferById(@PathVariable("id") String id) {
        Optional<Offer> offer = offerRepository.findById(id);

        UserInfo userInfo = new UserInfo(offer.get().getUser());

        OfferInfo offerInfo = new OfferInfo(offer.get(), userInfo);

        return ResponseEntity.ok(new OfferByIdResponse(List.of(offerInfo)));
    }

    @GetMapping("/offer/user/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOfferByUser(@PathVariable("id") String id) {
        List<Offer> offers = offerRepository.findByUserId(id);

        List<OfferInfo> response = offers.stream()
                .map(offer -> {
                    UserInfo userInfo = new UserInfo(offer.getUser());
                    return new OfferInfo(offer, userInfo);
                })
                .toList();

        return ResponseEntity.ok(new OfferByUserResponse(response));
    }
}
