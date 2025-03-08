package ch.kos.goat.dto.tags;

import ch.kos.goat.dto.DateAudit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class TagResponse extends DateAudit {

  private Long id;
  private String name;
  private String color;
}