import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IPrice } from 'app/shared/model/price.model';
import { PriceService } from './price.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';

@Component({
    selector: 'jhi-price-update',
    templateUrl: './price-update.component.html'
})
export class PriceUpdateComponent implements OnInit {
    private _price: IPrice;
    isSaving: boolean;

    products: IProduct[];
    dateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private priceService: PriceService,
        private productService: ProductService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ price }) => {
            this.price = price;
        });
        this.productService.query().subscribe(
            (res: HttpResponse<IProduct[]>) => {
                this.products = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.price.id !== undefined) {
            this.subscribeToSaveResponse(this.priceService.update(this.price));
        } else {
            this.subscribeToSaveResponse(this.priceService.create(this.price));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPrice>>) {
        result.subscribe((res: HttpResponse<IPrice>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get price() {
        return this._price;
    }

    set price(price: IPrice) {
        this._price = price;
    }
}
