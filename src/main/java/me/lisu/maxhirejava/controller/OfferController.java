package me.lisu.maxhirejava.controller;

import lombok.extern.slf4j.Slf4j;
import me.lisu.maxhirejava.model.Offer;
import me.lisu.maxhirejava.record.OfferInfo;
import me.lisu.maxhirejava.record.OfferRequest;
import me.lisu.maxhirejava.record.UserInfo;
import me.lisu.maxhirejava.record.MessageResponse;
import me.lisu.maxhirejava.repository.OfferRepository;
import me.lisu.maxhirejava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/")
@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials = "true"
)
public class OfferController {

    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private UserRepository userRepository;

    public record OfferResponse(List<OfferInfo> offers, int totalPages) {}
    public record OfferByIdResponse(List<OfferInfo> message) {}
    public record OfferByUserResponse(List<OfferInfo> message) {}

    @GetMapping("/offers")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OfferResponse> getAllOffers(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.DESC, "updated"));

        Page<Offer> offerPage = offerRepository.findAll(pageable);

        List<OfferInfo> response = offerPage.getContent().stream()
                .map(offer -> {
                    UserInfo userInfo = new UserInfo(offer.getUser());
                    return new OfferInfo(offer, userInfo);
                })
                .toList();

        return ResponseEntity.ok(new OfferResponse(response, offerPage.getTotalPages() - 1));
    }

    @GetMapping("/offer/getOffer/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOfferById(@PathVariable("id") String id) {
        Optional<Offer> offer = offerRepository.findById(id);

        if (offer.isEmpty()) {
            return ResponseEntity.status(404).body(new MessageResponse("Nie znaleziono oferty"));
        }

        UserInfo userInfo = new UserInfo(offer.get().getUser());

        OfferInfo offerInfo = new OfferInfo(offer.get(), userInfo);

        return ResponseEntity.ok(new OfferByIdResponse(List.of(offerInfo)));
    }

    @GetMapping("/offer/user/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOfferByUser(@PathVariable("id") String id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(404).body(new MessageResponse("Nie znaleziono użytkownika"));
        }

        List<Offer> offers = offerRepository.findByUserId(id);

        if (offers.isEmpty()) {
            return ResponseEntity.ok(new OfferByUserResponse(List.of()));
        }

        List<OfferInfo> response = offers.stream()
                .map(offer -> {
                    UserInfo userInfo = new UserInfo(offer.getUser());
                    return new OfferInfo(offer, userInfo);
                })
                .toList();

        return ResponseEntity.ok(new OfferByUserResponse(response));
    }
}
