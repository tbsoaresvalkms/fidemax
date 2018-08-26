/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FidemaxTestModule } from '../../../test.module';
import { ConsumerTransactionDetailComponent } from 'app/entities/consumer-transaction/consumer-transaction-detail.component';
import { ConsumerTransaction } from 'app/shared/model/consumer-transaction.model';

describe('Component Tests', () => {
    describe('ConsumerTransaction Management Detail Component', () => {
        let comp: ConsumerTransactionDetailComponent;
        let fixture: ComponentFixture<ConsumerTransactionDetailComponent>;
        const route = ({ data: of({ consumerTransaction: new ConsumerTransaction(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FidemaxTestModule],
                declarations: [ConsumerTransactionDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ConsumerTransactionDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ConsumerTransactionDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.consumerTransaction).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
