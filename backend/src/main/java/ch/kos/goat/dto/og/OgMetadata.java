package ch.kos.goat.dto.og;

public record OgMetadata(
        String title,
        String description,
        String type,
        String image,
        String url
) {}
