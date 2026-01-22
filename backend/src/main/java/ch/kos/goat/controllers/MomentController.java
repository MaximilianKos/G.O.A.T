package ch.kos.goat.controllers;

import ch.kos.goat.dto.moment.MomentResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ch.kos.goat.dto.moment.MomentRequest;
import ch.kos.goat.services.MomentService;

import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/moments")
@AllArgsConstructor
@CrossOrigin
public class MomentController {

    private final MomentService momentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MomentResponse> createMoment(
            @RequestPart("data") MomentRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return ResponseEntity.ok(momentService.createMoment(request, file));
    }

    @GetMapping
    public ResponseEntity<List<MomentResponse>> getMoments() {
        return ResponseEntity.ok(momentService.getMoments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MomentResponse> getMoment(@PathVariable Long id) {
        return ResponseEntity.ok(momentService.getMoment(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MomentResponse> updateMoment(
            @RequestPart("data") MomentRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(momentService.updateMoment(request, file, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoment(@PathVariable Long id) {
        momentService.deleteMoment(id);
        return ResponseEntity.ok().build();
    }
}
