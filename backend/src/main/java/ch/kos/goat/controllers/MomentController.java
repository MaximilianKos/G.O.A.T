package ch.kos.goat.controllers;

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

@RestController
@RequestMapping("/api/moments")
@AllArgsConstructor
@CrossOrigin
public class MomentController {

    private final MomentService momentService;

  @PostMapping
  public ResponseEntity<?> createMoment(@RequestBody MomentRequest request) {
    try {
        return ResponseEntity.ok(momentService.createMoment(request));
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping
  public ResponseEntity<?> getMoments() {
    try {
        return ResponseEntity.ok(momentService.getMoments());
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateMoment(@RequestBody MomentRequest request, @PathVariable Long id) {
    try {
        return ResponseEntity.ok(momentService.updateMoment(request, id));
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteMoment(@PathVariable Long id) {
    try {
        momentService.deleteMoment(id);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

}
