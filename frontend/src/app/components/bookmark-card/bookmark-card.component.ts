import { Component, input, output } from '@angular/core';
import { DatePipe, NgOptimizedImage } from '@angular/common';
import { MomentResponseDto } from '../../model/response/moment-response.dto';

@Component({
  selector: 'app-bookmark-card',
  standalone: true,
  imports: [DatePipe, NgOptimizedImage],
  templateUrl: './bookmark-card.component.html',
  styles: [':host { display: block; height: 100%; }']
})
export class BookmarkCardComponent {
  moment = input.required<MomentResponseDto>();
  edit = output<MomentResponseDto>();
  remove = output<number>();

  getFileUrl(path: string | null): string {
    if (!path) return '';
    if (path.startsWith('http')) return path;
    return `http://localhost:8080/${path}`;
  }

  getTypeIcon(type: string): string {
    switch (type) {
      case 'VIDEO':
        return '🎥';
      case 'IMAGE':
        return '🖼️';
      case 'AUDIO':
        return '🎵';
      case 'ARTICLE':
        return '📄';
      default:
        return '📎';
    }
  }
}
