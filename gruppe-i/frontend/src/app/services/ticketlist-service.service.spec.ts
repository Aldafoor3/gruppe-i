import { TestBed } from '@angular/core/testing';
import {TicketlistServiceService} from "./ticketlist-service.service";

describe('TicketlistServiceService', () => {
  let service: TicketlistServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TicketlistServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
//
