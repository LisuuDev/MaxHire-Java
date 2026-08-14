package me.lisu.maxhirejava.repository;

import me.lisu.maxhirejava.model.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, String> {

    @EntityGraph(attributePaths = "user")
    List<Offer> findByUserId(String userId);

    @EntityGraph(attributePaths = "user")
    Page<Offer> findAll(Pageable pageable);
}
