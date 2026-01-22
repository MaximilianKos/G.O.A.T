package ch.kos.goat.dto.og;

public record OgResponse(
        String inputUrl,
        String finalUrl,
        int httpStatus,
        OgMetadata og,
        String error
) {}