import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketWindowComponent } from './ticket-window.component';

describe('TicketWindowComponent', () => {
  let component: TicketWindowComponent;
  let fixture: ComponentFixture<TicketWindowComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TicketWindowComponent]
    });
    fixture = TestBed.createComponent(TicketWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
