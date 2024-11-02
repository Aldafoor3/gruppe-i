import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GeoDataMapComponent } from './geo-data-map.component';

describe('GeoDataMapComponent', () => {
  let component: GeoDataMapComponent;
  let fixture: ComponentFixture<GeoDataMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GeoDataMapComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GeoDataMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
