package me.lisu.maxhirejava.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OfferRequest(
        @NotBlank(message = "Tytuł jest wymagany")
        @Size(max = 40, message = "Tytuł może mieć maksymalnie 40 znaków")
        String title,

        @Size(max = 40, message = "Nazwa firmy może mieć maksymalnie 40 znaków")
        String company,

        String description,

        String tech,

        String links
) {}