package me.lisu.maxhirejava.record;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.lisu.maxhirejava.model.Offer;

public record OfferInfo(
        @JsonUnwrapped UserInfo user,
        String id,
        String title,
        String company,
        String description,
        String tech,
        String links,
        String updated
) {
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