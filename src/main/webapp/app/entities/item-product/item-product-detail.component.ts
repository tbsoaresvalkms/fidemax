import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItemProduct } from 'app/shared/model/item-product.model';

@Component({
    selector: 'jhi-item-product-detail',
    templateUrl: './item-product-detail.component.html'
})
export class ItemProductDetailComponent implements OnInit {
    itemProduct: IItemProduct;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ itemProduct }) => {
            this.itemProduct = itemProduct;
        });
    }

    previousState() {
        window.history.back();
    }
}
