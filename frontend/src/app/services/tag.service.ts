import {inject, Injectable, signal} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {environment} from "../../environments/environment";
import {CreateTagRequestDto} from "../model/request/create-tag-request.dto";
import {TagResponseDto} from "../model/response/tag-response.dto";

@Injectable({
  providedIn: 'root'
})
export class TagService {
  private readonly http = inject(HttpClient);

  private readonly _availableTags = signal<TagResponseDto[]>([]);
  readonly availableTags = this._availableTags.asReadonly();

  getAllTags(): Observable<TagResponseDto[]> {
    return this.http.get<TagResponseDto[]>(`${environment.backendUrl}tags`).pipe(
      tap(tags => this._availableTags.set(tags))
    );
  }

  createTag(req: CreateTagRequestDto): Observable<TagResponseDto> {
    return this.http.post<TagResponseDto>(`${environment.backendUrl}tags`, req).pipe(
      tap(newTag => {
        this._availableTags.update(tags => [...tags, newTag]);
      })
    );
  }
}
