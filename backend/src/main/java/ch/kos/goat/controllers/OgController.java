package ch.kos.goat.controllers;

import ch.kos.goat.dto.og.OgResponse;
import ch.kos.goat.services.OgService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metadata")
@AllArgsConstructor
@CrossOrigin
public class OgController {

    private final OgService ogService;

    // Example: GET /api/metadata?url=https://example.com
    @GetMapping
    public OgResponse getMetadata(@RequestParam("url") String url) {
        return ogService.scrapeOne(url);
    }
}
