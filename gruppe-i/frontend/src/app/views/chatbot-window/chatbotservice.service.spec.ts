import { TestBed } from '@angular/core/testing';

import {ChatbotServiceService} from "./chatbotservice.service";

describe('ChatbotserviceService', () => {
  let service: ChatbotServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChatbotServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
