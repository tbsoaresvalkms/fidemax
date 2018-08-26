/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FidemaxTestModule } from '../../../test.module';
import { RedemptionTransactionUpdateComponent } from 'app/entities/redemption-transaction/redemption-transaction-update.component';
import { RedemptionTransactionService } from 'app/entities/redemption-transaction/redemption-transaction.service';
import { RedemptionTransaction } from 'app/shared/model/redemption-transaction.model';

describe('Component Tests', () => {
    describe('RedemptionTransaction Management Update Component', () => {
        let comp: RedemptionTransactionUpdateComponent;
        let fixture: ComponentFixture<RedemptionTransactionUpdateComponent>;
        let service: RedemptionTransactionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FidemaxTestModule],
                declarations: [RedemptionTransactionUpdateComponent]
            })
                .overrideTemplate(RedemptionTransactionUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RedemptionTransactionUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RedemptionTransactionService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new RedemptionTransaction(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.redemptionTransaction = entity;
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
                    const entity = new RedemptionTransaction();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.redemptionTransaction = entity;
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
