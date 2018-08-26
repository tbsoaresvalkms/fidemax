/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FidemaxTestModule } from '../../../test.module';
import { ItemProductDetailComponent } from 'app/entities/item-product/item-product-detail.component';
import { ItemProduct } from 'app/shared/model/item-product.model';

describe('Component Tests', () => {
    describe('ItemProduct Management Detail Component', () => {
        let comp: ItemProductDetailComponent;
        let fixture: ComponentFixture<ItemProductDetailComponent>;
        const route = ({ data: of({ itemProduct: new ItemProduct(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FidemaxTestModule],
                declarations: [ItemProductDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ItemProductDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ItemProductDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.itemProduct).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
