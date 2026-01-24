import {inject, Injectable, signal} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {CreateMomentRequestDto} from "../model/request/create-moment-request.dto";
import {MomentResponseDto} from "../model/response/moment-response.dto";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class MomentService {

  private readonly http = inject(HttpClient);

  private readonly _allMoments = signal<MomentResponseDto[]>([]);
  readonly allMoments = this._allMoments.asReadonly();

  saveMoment(req: CreateMomentRequestDto, file?: File): Observable<MomentResponseDto> {
    const formData = new FormData();

    formData.append('data', new Blob([JSON.stringify(req)], {
      type: 'application/json'
    }));

    if (file) {
      formData.append('file', file);
    }

    return this.http.post<MomentResponseDto>(`${environment.backendUrl}moments`, formData).pipe(
      tap(savedMoment => {
        this._allMoments.update(list => [savedMoment, ...list]);
      })
    );
  }

  updateMoment(id: number, req: CreateMomentRequestDto, file?: File): Observable<MomentResponseDto> {
    const formData = new FormData();
    formData.append('data', new Blob([JSON.stringify(req)], {type: 'application/json'}));

    if (file) {
      formData.append('file', file);
    }

    return this.http.put<MomentResponseDto>(`${environment.backendUrl}moments/${id}`, formData).pipe(
      tap(updatedMoment => {
        this._allMoments.update(list => list.map(m => m.id === id ? updatedMoment : m));
      })
    );
  }

  getMoments(): Observable<MomentResponseDto[]> {
    return this.http.get<MomentResponseDto[]>(`${environment.backendUrl}moments`).pipe(
      tap(fetchedMoments => {
        this._allMoments.set(fetchedMoments);
      })
    );
  }

  deleteMoment(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.backendUrl}moments/${id}`).pipe(
      tap(() => {
        this._allMoments.update(moments => moments.filter(m => m.id !== id));
      })
    );
  }
}
