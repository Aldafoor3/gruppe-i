import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfilWindowComponent } from './profil-window.component';

describe('ProfilWindowComponent', () => {
  let component: ProfilWindowComponent;
  let fixture: ComponentFixture<ProfilWindowComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProfilWindowComponent]
    });
    fixture = TestBed.createComponent(ProfilWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
