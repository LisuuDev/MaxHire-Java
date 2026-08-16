package me.lisu.maxhirejava.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.lisu.maxhirejava.model.Offer;

import java.util.Objects;
import java.util.Optional;

public record OfferInfo(
        @JsonUnwrapped
        @JsonIgnoreProperties({"id"})
        UserInfo user,

        String id,
        String title,
        String company,
        String description,
        String tech,
        String links,
        String updated
) {
    @JsonProperty("user_id")
    public String userId() {
        return Optional.ofNullable(user)
                .map(UserInfo::id)
                .orElse(null);
    }

    public OfferInfo(Offer entity, UserInfo userInfo) {
        this(
                userInfo,
                entity.getId(),
                entity.getTitle(),
                entity.getCompany(),
                entity.getDescription(),
                entity.getTech(),
                entity.getLinks(),
                entity.getUpdated()
        );
    }


}