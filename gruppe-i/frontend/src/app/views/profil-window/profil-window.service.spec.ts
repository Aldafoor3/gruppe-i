import { TestBed } from '@angular/core/testing';

import { ProfilWindowService } from './profil-window.service';

describe('ProfilWindowService', () => {
  let service: ProfilWindowService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProfilWindowService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
