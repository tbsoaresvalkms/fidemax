/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FidemaxTestModule } from '../../../test.module';
import { ItemProductUpdateComponent } from 'app/entities/item-product/item-product-update.component';
import { ItemProductService } from 'app/entities/item-product/item-product.service';
import { ItemProduct } from 'app/shared/model/item-product.model';

describe('Component Tests', () => {
    describe('ItemProduct Management Update Component', () => {
        let comp: ItemProductUpdateComponent;
        let fixture: ComponentFixture<ItemProductUpdateComponent>;
        let service: ItemProductService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FidemaxTestModule],
                declarations: [ItemProductUpdateComponent]
            })
                .overrideTemplate(ItemProductUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ItemProductUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ItemProductService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ItemProduct(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.itemProduct = entity;
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
                    const entity = new ItemProduct();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.itemProduct = entity;
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
