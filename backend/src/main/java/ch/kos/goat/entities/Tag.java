package ch.kos.goat.entities;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "tags")
public class Tag extends DateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private String color;

  @ManyToMany(mappedBy = "tags")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @JsonIgnore
  private Set<Moment> moments;

  private String icon;
}