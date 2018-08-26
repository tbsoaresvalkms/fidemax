/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FidemaxTestModule } from '../../../test.module';
import { ItemProductDeleteDialogComponent } from 'app/entities/item-product/item-product-delete-dialog.component';
import { ItemProductService } from 'app/entities/item-product/item-product.service';

describe('Component Tests', () => {
    describe('ItemProduct Management Delete Component', () => {
        let comp: ItemProductDeleteDialogComponent;
        let fixture: ComponentFixture<ItemProductDeleteDialogComponent>;
        let service: ItemProductService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FidemaxTestModule],
                declarations: [ItemProductDeleteDialogComponent]
            })
                .overrideTemplate(ItemProductDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ItemProductDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ItemProductService);
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
