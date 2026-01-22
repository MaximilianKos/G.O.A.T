import {inject, Injectable, signal} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {CreateMomentRequest} from "../Model/request/CreateMomentRequest";
import {MomentResponse} from "../Model/response/MomentResponse";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class MomentService {

  private readonly http = inject(HttpClient);

  private readonly _allMoments = signal<MomentResponse[]>([]);
  readonly allMoments = this._allMoments.asReadonly();

  saveMoment(req: CreateMomentRequest, file?: File): Observable<MomentResponse> {
    const formData = new FormData();

    formData.append('data', new Blob([JSON.stringify(req)], {
      type: 'application/json'
    }));

    if (file) {
      formData.append('file', file);
    }

    return this.http.post<MomentResponse>(`${environment.backendUrl}moments`, formData).pipe(
      tap(savedMoment => {
        this._allMoments.update(list => [savedMoment, ...list]);
      })
    );
  }

  updateMoment(id: number, req: CreateMomentRequest, file?: File): Observable<MomentResponse> {
    const formData = new FormData();
    formData.append('data', new Blob([JSON.stringify(req)], {type: 'application/json'}));

    if (file) {
      formData.append('file', file);
    }

    return this.http.put<MomentResponse>(`${environment.backendUrl}moments/${id}`, formData).pipe(
      tap(updatedMoment => {
        this._allMoments.update(list => list.map(m => m.id === id ? updatedMoment : m));
      })
    );
  }

  getMoments(): Observable<MomentResponse[]> {
    return this.http.get<MomentResponse[]>(`${environment.backendUrl}moments`).pipe(
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
