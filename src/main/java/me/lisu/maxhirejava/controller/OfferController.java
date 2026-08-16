package me.lisu.maxhirejava.controller;

import jakarta.validation.Valid;
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

import java.time.Instant;
import java.util.List;
import java.util.Objects;
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
    public ResponseEntity<OfferResponse> getAllOffers(@RequestParam(defaultValue = "1") int page) {
        if (page < 1) page = 1;
        Pageable pageable = PageRequest.of(page - 1, 7, Sort.by(Sort.Direction.DESC, "updated"));

        Page<Offer> offerPage = offerRepository.findAll(pageable);

        List<OfferInfo> response = offerPage.getContent().stream()
                .map(offer -> {
                    UserInfo userInfo = new UserInfo(offer.getUser());
                    return new OfferInfo(offer, userInfo);
                })
                .toList();

        return ResponseEntity.ok(new OfferResponse(response, offerPage.getTotalPages()));
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

    @PostMapping("/addOffer")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addOffer(Authentication authentication, @Valid @RequestBody OfferRequest request) {
        Offer offer = new Offer();
        offer.setUserId(authentication.getName());
        offer.setUpdated(String.valueOf(Instant.now().getEpochSecond()));
        offer.setTitle(request.title());
        offer.setCompany(request.company());
        offer.setDescription(request.description());
        offer.setTech(request.tech());
        offer.setLinks(request.links());

        Offer savedOffer;
        try {
            savedOffer = offerRepository.save(offer);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił błąd podczas zapisywania oferty");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Oferta została dodana pomyślnie z id: " + savedOffer.getId());
    }

    @PatchMapping({"/offers/editOffer/{id}", "/admin/update/offer/{id}"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> editOffer(Authentication authentication, @PathVariable("id") String id, @Valid @RequestBody OfferRequest request) {
        Optional<Offer> offer = offerRepository.findById(id);

        if (offer.isEmpty()) {
            return ResponseEntity.status(404).body(new MessageResponse("Nie znaleziono oferty"));
        }

        String userId = authentication.getName();
        boolean isAdmin = userRepository.isAdmin(userId);
        
        if (!Objects.equals(authentication.getName(), offer.get().getUserId()) && !isAdmin) {
            return ResponseEntity.status(403).body(new MessageResponse("Brak uprawnień do edycji tej oferty"));
        }

        Offer existingOffer = offer.get();

        existingOffer.setTitle(request.title());
        existingOffer.setCompany(request.company());
        existingOffer.setDescription(request.description());
        existingOffer.setTech(request.tech());
        existingOffer.setLinks(request.links());
        existingOffer.setUpdated(String.valueOf(Instant.now().getEpochSecond()));

        Offer savedOffer;
        try {
            savedOffer = offerRepository.save(existingOffer);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Wystąpił błąd podczas zapisywania oferty"));
        }

        return ResponseEntity.ok(new MessageResponse("Oferta została zaktualizowana pomyślnie" + savedOffer.getId()));
    }

    @DeleteMapping({"/offers/deleteOffer/{id}", "/admin/remove/offer/{id}"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteOffer(Authentication authentication, @PathVariable("id") String id) {
        Optional<Offer> offer = offerRepository.findById(id);

        if (offer.isEmpty()) {
            return ResponseEntity.status(404).body(new MessageResponse("Nie znaleziono oferty"));
        }

        String userId = authentication.getName();
        boolean isAdmin = userRepository.isAdmin(userId);

        if (!Objects.equals(authentication.getName(), offer.get().getUserId()) && !isAdmin) {
            return ResponseEntity.status(403).body(new MessageResponse("Brak uprawnień do usunięcia tej oferty"));
        }

        try {
            offerRepository.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Wystąpił błąd podczas usuwania oferty"));
        }

        return ResponseEntity.ok(new MessageResponse("Oferta została usunięta pomyślnie" + id));
    }
}
