import {inject, Injectable, signal} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {environment} from "../../environments/environment";
import {CreateTagRequest} from "../Model/request/CreateTagRequest";
import {TagResponse} from "../Model/response/TagResponse";

@Injectable({
  providedIn: 'root'
})
export class TagService {
  private readonly http = inject(HttpClient);

  private readonly _availableTags = signal<TagResponse[]>([]);
  readonly availableTags = this._availableTags.asReadonly();

  getAllTags(): Observable<TagResponse[]> {
    return this.http.get<TagResponse[]>(`${environment.backendUrl}tags`).pipe(
      tap(tags => this._availableTags.set(tags))
    );
  }

  createTag(req: CreateTagRequest): Observable<TagResponse> {
    return this.http.post<TagResponse>(`${environment.backendUrl}tags`, req).pipe(
      tap(newTag => {
        this._availableTags.update(tags => [...tags, newTag]);
      })
    );
  }
}
