import { TestBed } from '@angular/core/testing';

import { UserDetailInfoServiceService } from './user-detail-info-service.service';

describe('UserDetailInfoServiceService', () => {
  let service: UserDetailInfoServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserDetailInfoServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
