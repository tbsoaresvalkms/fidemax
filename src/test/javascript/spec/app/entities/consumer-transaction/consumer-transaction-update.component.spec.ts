/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FidemaxTestModule } from '../../../test.module';
import { ConsumerTransactionUpdateComponent } from 'app/entities/consumer-transaction/consumer-transaction-update.component';
import { ConsumerTransactionService } from 'app/entities/consumer-transaction/consumer-transaction.service';
import { ConsumerTransaction } from 'app/shared/model/consumer-transaction.model';

describe('Component Tests', () => {
    describe('ConsumerTransaction Management Update Component', () => {
        let comp: ConsumerTransactionUpdateComponent;
        let fixture: ComponentFixture<ConsumerTransactionUpdateComponent>;
        let service: ConsumerTransactionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FidemaxTestModule],
                declarations: [ConsumerTransactionUpdateComponent]
            })
                .overrideTemplate(ConsumerTransactionUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ConsumerTransactionUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConsumerTransactionService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ConsumerTransaction(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.consumerTransaction = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ConsumerTransaction();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.consumerTransaction = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
