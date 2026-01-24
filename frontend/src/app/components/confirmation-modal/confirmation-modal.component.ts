import { Component, ElementRef, input, output, ViewChild, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-confirmation-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './confirmation-modal.component.html'
})
export class ConfirmationModalComponent implements AfterViewInit {
  title = input<string>('Confirm Action');
  message = input.required<string>();
  confirmLabel = input<string>('Confirm');
  cancelLabel = input<string>('Cancel');

  confirmed = output<void>();
  cancelled = output<void>();

  @ViewChild('dialog') dialog!: ElementRef<HTMLDialogElement>;

  ngAfterViewInit() {
    this.dialog.nativeElement.showModal();
  }

  onConfirm() {
    this.dialog.nativeElement.close();
    this.confirmed.emit();
  }

  onCancel() {
    this.dialog.nativeElement.close();
    this.cancelled.emit();
  }

  onDialogCancel() {
    this.cancelled.emit();
  }
}
