import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IItemProduct } from 'app/shared/model/item-product.model';
import { ItemProductService } from './item-product.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';
import { IRedemptionTransaction } from 'app/shared/model/redemption-transaction.model';
import { RedemptionTransactionService } from 'app/entities/redemption-transaction';

@Component({
    selector: 'jhi-item-product-update',
    templateUrl: './item-product-update.component.html'
})
export class ItemProductUpdateComponent implements OnInit {
    private _itemProduct: IItemProduct;
    isSaving: boolean;

    products: IProduct[];

    redemptiontransactions: IRedemptionTransaction[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private itemProductService: ItemProductService,
        private productService: ProductService,
        private redemptionTransactionService: RedemptionTransactionService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ itemProduct }) => {
            this.itemProduct = itemProduct;
        });
        this.productService.query({ filter: 'itemproduct-is-null' }).subscribe(
            (res: HttpResponse<IProduct[]>) => {
                if (!this.itemProduct.productId) {
                    this.products = res.body;
                } else {
                    this.productService.find(this.itemProduct.productId).subscribe(
                        (subRes: HttpResponse<IProduct>) => {
                            this.products = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.redemptionTransactionService.query().subscribe(
            (res: HttpResponse<IRedemptionTransaction[]>) => {
                this.redemptiontransactions = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.itemProduct.id !== undefined) {
            this.subscribeToSaveResponse(this.itemProductService.update(this.itemProduct));
        } else {
            this.subscribeToSaveResponse(this.itemProductService.create(this.itemProduct));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IItemProduct>>) {
        result.subscribe((res: HttpResponse<IItemProduct>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackProductById(index: number, item: IProduct) {
        return item.id;
    }

    trackRedemptionTransactionById(index: number, item: IRedemptionTransaction) {
        return item.id;
    }
    get itemProduct() {
        return this._itemProduct;
    }

    set itemProduct(itemProduct: IItemProduct) {
        this._itemProduct = itemProduct;
    }
}
