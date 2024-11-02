import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemoDataSetComponent } from './demo-data-set.component';

describe('DemoDataSetComponent', () => {
  let component: DemoDataSetComponent;
  let fixture: ComponentFixture<DemoDataSetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DemoDataSetComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DemoDataSetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
