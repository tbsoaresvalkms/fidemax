/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FidemaxTestModule } from '../../../test.module';
import { RedemptionTransactionDeleteDialogComponent } from 'app/entities/redemption-transaction/redemption-transaction-delete-dialog.component';
import { RedemptionTransactionService } from 'app/entities/redemption-transaction/redemption-transaction.service';

describe('Component Tests', () => {
    describe('RedemptionTransaction Management Delete Component', () => {
        let comp: RedemptionTransactionDeleteDialogComponent;
        let fixture: ComponentFixture<RedemptionTransactionDeleteDialogComponent>;
        let service: RedemptionTransactionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FidemaxTestModule],
                declarations: [RedemptionTransactionDeleteDialogComponent]
            })
                .overrideTemplate(RedemptionTransactionDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RedemptionTransactionDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RedemptionTransactionService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
