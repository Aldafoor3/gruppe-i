import { TestBed } from '@angular/core/testing';

import { ChatdemoService } from './chatdemo.service';

describe('ChatdemoService', () => {
  let service: ChatdemoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChatdemoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
