import {Component, computed, inject, OnInit, signal} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {BookmarkCardComponent} from "../../components/bookmark-card/bookmark-card.component";
import {ConfirmationModalComponent} from "../../components/confirmation-modal/confirmation-modal.component";
import {MomentResponseDto} from "../../model/response/moment-response.dto";
import {MomentService} from "../../services/moment.service";
import {TagService} from "../../services/tag.service";
import {TagResponseDto} from "../../model/response/tag-response.dto";
import {CreateTagRequestDto} from "../../model/request/create-tag-request.dto";
import Fuse from 'fuse.js';
import {forkJoin, map, Observable, of, switchMap} from 'rxjs';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    BookmarkCardComponent,
    ConfirmationModalComponent
  ],
  templateUrl: './home-page.component.html',
})
export class HomePageComponent implements OnInit {

  private readonly fb = inject(FormBuilder);
  readonly momentService = inject(MomentService);
  readonly tagService = inject(TagService);
  private readonly http = inject(HttpClient);

  saveMomentForm!: FormGroup;
  selectedFile: File | null = null;

  isLoading = signal(true);

  searchQuery = signal('');
  selectedTag = signal<TagResponseDto | null>(null);

  isModalOpen = false;
  isSaving = false;
  editingMoment: MomentResponseDto | null = null;
  momentToDelete = signal<number | null>(null);

  private readonly fuseOptions = {
    keys: ['title', 'description', 'tags.name'],
    threshold: 0.3,
    ignoreLocation: true
  };

  filteredMoments = computed(() => {
    const moments = this.momentService.allMoments().filter(m => !m.archived);
    const query = this.searchQuery();
    const tagFilter = this.selectedTag();

    let results = moments;

    if (query.trim().length > 0) {
      const fuse = new Fuse(moments, this.fuseOptions);
      results = fuse.search(query).map(res => res.item);
    }

    if (tagFilter) {
      results = results.filter(m => m.tags && m.tags.some(t => t.name === tagFilter.name));
    }

    return results;
  });

  uniqueTags = computed(() => {
    const visibleMoments = this.momentService.allMoments().filter(m => !m.archived);
    const allTags = visibleMoments.flatMap(m => m.tags || []);
    const unique = new Map();
    for (const tag of allTags) {
      if (!unique.has(tag.name)) {
        unique.set(tag.name, tag);
      }
    }
    return Array.from(unique.values());
  });

  ngOnInit() {
    this.saveMomentForm = this.fb.group({
      title: ['', Validators.required],
      sourceUrl: ['', Validators.required],
      description: ['', Validators.required],
      type: ['VIDEO', Validators.required],
      thumbnailUrl: [''],
      tags: ['']
    });

    this.momentService.getMoments().subscribe({
      next: () => this.isLoading.set(false),
      error: () => this.isLoading.set(false)
    });

    this.tagService.getAllTags().subscribe();
  }

  openModal(moment?: MomentResponseDto) {
    this.isModalOpen = true;
    this.saveMomentForm.reset({type: 'VIDEO'});
    this.selectedFile = null;

    if (moment) {
      this.editingMoment = moment;
      const tagsString = moment.tags ? moment.tags.map(t => t.name).join(', ') : '';
      this.saveMomentForm.patchValue({...moment, tags: tagsString});
    } else {
      this.editingMoment = null;
    }
  }

  closeModal() {
    this.isModalOpen = false;
    this.saveMomentForm.reset();
    this.selectedFile = null;
    this.isSaving = false;
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      if (file.type.startsWith('image/')) {
        this.saveMomentForm.patchValue({type: 'IMAGE'});
      } else if (file.type.startsWith('video/')) {
        this.saveMomentForm.patchValue({type: 'VIDEO'});
      }
    }
  }

  isTagInInput(tagName: string): boolean {
    const current = this.saveMomentForm.get('tags')?.value as string || '';
    const tags = current.split(',').map(t => t.trim().toLowerCase());
    return tags.includes(tagName.toLowerCase());
  }

  addTag(tagName: string) {
    const currentControl = this.saveMomentForm.get('tags');
    const currentValue = currentControl?.value as string || '';
    let tags = currentValue.split(',').map(t => t.trim()).filter(t => t.length > 0);
    const index = tags.findIndex(t => t.toLowerCase() === tagName.toLowerCase());
    if (index > -1) {
      tags.splice(index, 1);
    } else {
      tags.push(tagName);
    }
    currentControl?.setValue(tags.join(', '));
    currentControl?.markAsDirty();
  }

  saveMoment(): void {
    if (this.saveMomentForm.invalid) {
      this.saveMomentForm.markAllAsTouched();
      return;
    }
    this.isSaving = true;
    const formVal = this.saveMomentForm.value;
    const rawTagNames = (formVal.tags as string || '').split(',').map(t => t.trim().toLowerCase()).filter(t => t.length > 0);

    this.resolveTagsToIds(rawTagNames).pipe(
      switchMap(tagIds => {
        const req = {
          title: formVal.title,
          sourceUrl: formVal.sourceUrl,
          description: formVal.description,
          type: formVal.type,
          thumbnailUrl: formVal.thumbnailUrl,
          tagIds: tagIds
        };
        if (this.editingMoment) {
          return this.momentService.updateMoment(this.editingMoment.id, req, this.selectedFile || undefined);
        } else {
          return this.momentService.saveMoment(req, this.selectedFile || undefined);
        }
      })
    ).subscribe({
      next: () => this.closeModal(),
      error: (err) => {
        console.error('Save/Update failed:', err);
        this.isSaving = false;
      }
    });
  }

  deleteMoment(id: number) {
    this.momentToDelete.set(id);
  }

  onConfirmDelete() {
    const id = this.momentToDelete();
    if (id) {
      this.momentService.deleteMoment(id).subscribe({
        next: () => {
          console.log('Moment deleted successfully');
          this.momentToDelete.set(null);
        },
        error: (err) => {
          console.error('Delete failed', err);
          this.momentToDelete.set(null);
        }
      });
    }
  }

  onCancelDelete() {
    this.momentToDelete.set(null);
  }

  private resolveTagsToIds(names: string[]): Observable<number[]> {
    if (names.length === 0) return of([]);
    const availableTags = this.tagService.availableTags();
    const tasks: Observable<TagResponseDto>[] = [];
    names.forEach(name => {
      const existing = availableTags.find(t => t.name.toLowerCase() === name);
      if (existing) {
        tasks.push(of(existing));
      } else {
        const newTagReq: CreateTagRequestDto = {name: name, color: '#6366f1'};
        tasks.push(this.tagService.createTag(newTagReq));
      }
    });
    return forkJoin(tasks).pipe(map(tags => tags.map(t => t.id)));
  }

  toggleTagFilter(tag: TagResponseDto) {
    this.selectedTag.update(curr => curr?.name === tag.name ? null : tag);
  }

  onUrlBlur() {
    const urlControl = this.saveMomentForm.get('sourceUrl');
    const urlValue = urlControl?.value;
    if (urlValue && urlValue.length > 4) {
      const currentTitle = this.saveMomentForm.get('title')?.value;
      if (!currentTitle) {
        this.http.get<any>(`${environment.backendUrl}metadata?url=${encodeURIComponent(urlValue)}`)
          .subscribe({
            next: (resp) => {
              if (resp.og) {
                this.saveMomentForm.patchValue({
                  title: resp.og.title || '',
                  description: resp.og.description || '',
                  thumbnailUrl: resp.og.image || ''
                });
              }
            },
            error: (e) => console.log('Could not fetch metadata', e)
          });
      }
    }
  }
}
