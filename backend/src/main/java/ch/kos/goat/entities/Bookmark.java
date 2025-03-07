package ch.kos.goat.entities;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "bookmarks")
public class Bookmark extends DateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false)
  private String title;
  @Column(nullable = false)
  private String url;
  private String thumbnail_url;
  @Column(nullable = false)
  @Builder.Default
  private int clicks = 0;
  @Column(nullable = false)
  @Builder.Default
  private boolean archived = false;

  @ManyToMany
  @JoinTable(name = "bookmark_tags", joinColumns = @JoinColumn(name = "bookmark_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private Set<Tag> tags;
}
