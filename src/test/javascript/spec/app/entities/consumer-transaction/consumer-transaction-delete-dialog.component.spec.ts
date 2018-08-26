/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FidemaxTestModule } from '../../../test.module';
import { ConsumerTransactionDeleteDialogComponent } from 'app/entities/consumer-transaction/consumer-transaction-delete-dialog.component';
import { ConsumerTransactionService } from 'app/entities/consumer-transaction/consumer-transaction.service';

describe('Component Tests', () => {
    describe('ConsumerTransaction Management Delete Component', () => {
        let comp: ConsumerTransactionDeleteDialogComponent;
        let fixture: ComponentFixture<ConsumerTransactionDeleteDialogComponent>;
        let service: ConsumerTransactionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FidemaxTestModule],
                declarations: [ConsumerTransactionDeleteDialogComponent]
            })
                .overrideTemplate(ConsumerTransactionDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ConsumerTransactionDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConsumerTransactionService);
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
