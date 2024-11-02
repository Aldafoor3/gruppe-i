import { TestBed } from '@angular/core/testing';

import { GeoDataMapService } from './geo-data-map.service';

describe('GeoDataMapService', () => {
  let service: GeoDataMapService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GeoDataMapService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
