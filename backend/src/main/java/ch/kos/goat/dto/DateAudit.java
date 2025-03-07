package ch.kos.goat.dto;

import java.time.Instant;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
public abstract class DateAudit {

  private Instant createdAt;
  private Instant updatedAt;
}