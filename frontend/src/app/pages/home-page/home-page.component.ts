import {Component, computed, inject, OnInit, signal} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {MomentResponse} from "../../Model/response/MomentResponse";
import {MomentService} from "../../services/moment.service";
import {TagService} from "../../services/tag.service";
import {TagResponse} from "../../Model/response/TagResponse";
import {CreateTagRequest} from "../../Model/request/CreateTagRequest";
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
    DatePipe,
    NgForOf,
    NgIf
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
  selectedTag = signal<TagResponse | null>(null);

  isModalOpen = false;
  isSaving = false;
  editingMoment: MomentResponse | null = null;

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

  openModal(moment?: MomentResponse) {
    this.isModalOpen = true;
    this.saveMomentForm.reset({type: 'VIDEO'});
    this.selectedFile = null;

    if (moment) {
      this.editingMoment = moment;
      const tagsString = moment.tags ? moment.tags.map(t => t.name).join(', ') : '';
      this.saveMomentForm.patchValue({ ...moment, tags: tagsString });
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
        this.saveMomentForm.patchValue({ type: 'IMAGE' });
      } else if (file.type.startsWith('video/')) {
        this.saveMomentForm.patchValue({ type: 'VIDEO' });
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
    if (index > -1) { tags.splice(index, 1); } else { tags.push(tagName); }
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
      error: (err) => { console.error('Save/Update failed:', err); this.isSaving = false; }
    });
  }

  deleteMoment(id: number) {
    if (confirm('Delete this moment?')) {
      this.momentService.deleteMoment(id).subscribe({
        next: () => console.log('Moment deleted successfully'),
        error: (err) => console.error('Delete failed', err)
      });
    }
  }

  private resolveTagsToIds(names: string[]): Observable<number[]> {
    if (names.length === 0) return of([]);
    const availableTags = this.tagService.availableTags();
    const tasks: Observable<TagResponse>[] = [];
    names.forEach(name => {
      const existing = availableTags.find(t => t.name.toLowerCase() === name);
      if (existing) { tasks.push(of(existing)); }
      else {
        const newTagReq: CreateTagRequest = { name: name, color: '#6366f1' };
        tasks.push(this.tagService.createTag(newTagReq));
      }
    });
    return forkJoin(tasks).pipe(map(tags => tags.map(t => t.id)));
  }

  toggleTagFilter(tag: TagResponse) {
    this.selectedTag.update(curr => curr?.name === tag.name ? null : tag);
  }

  getTypeIcon(type: string): string {
    switch (type) {
      case 'VIDEO': return '🎥';
      case 'IMAGE': return '🖼️';
      case 'AUDIO': return '🎵';
      case 'ARTICLE': return '📄';
      default: return '📎';
    }
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

  getFileUrl(path: string | null): string {
    if (!path) return '';
    if (path.startsWith('http')) return path;
    return `http://localhost:8080/${path}`;
  }
}
