package ch.kos.goat.controllers;

import ch.kos.goat.dto.moment.MomentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.kos.goat.dto.moment.MomentRequest;
import ch.kos.goat.services.MomentService;

import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/moments")
@AllArgsConstructor
@CrossOrigin
public class MomentController {

    private final MomentService momentService;

    @PostMapping
    public ResponseEntity<MomentResponse> createMoment(@RequestBody MomentRequest request) {
        return ResponseEntity.ok(momentService.createMoment(request));
    }

    @GetMapping
    public ResponseEntity<List<MomentResponse>> getMoments() {
        return ResponseEntity.ok(momentService.getMoments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MomentResponse> getMoment(@PathVariable Long id) {
        return ResponseEntity.ok(momentService.getMoment(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MomentResponse> updateMoment(@RequestBody MomentRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(momentService.updateMoment(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoment(@PathVariable Long id) {
        momentService.deleteMoment(id);
        return ResponseEntity.ok().build();
    }
}
