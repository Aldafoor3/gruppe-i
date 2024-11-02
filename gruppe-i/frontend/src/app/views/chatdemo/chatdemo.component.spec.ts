import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatdemoComponent } from './chatdemo.component';

describe('ChatdemoComponent', () => {
  let component: ChatdemoComponent;
  let fixture: ComponentFixture<ChatdemoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChatdemoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChatdemoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
