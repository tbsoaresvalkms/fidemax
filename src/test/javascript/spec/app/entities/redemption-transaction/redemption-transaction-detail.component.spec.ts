/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FidemaxTestModule } from '../../../test.module';
import { RedemptionTransactionDetailComponent } from 'app/entities/redemption-transaction/redemption-transaction-detail.component';
import { RedemptionTransaction } from 'app/shared/model/redemption-transaction.model';

describe('Component Tests', () => {
    describe('RedemptionTransaction Management Detail Component', () => {
        let comp: RedemptionTransactionDetailComponent;
        let fixture: ComponentFixture<RedemptionTransactionDetailComponent>;
        const route = ({ data: of({ redemptionTransaction: new RedemptionTransaction(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FidemaxTestModule],
                declarations: [RedemptionTransactionDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RedemptionTransactionDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RedemptionTransactionDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.redemptionTransaction).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
