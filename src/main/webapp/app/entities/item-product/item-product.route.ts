import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ItemProduct } from 'app/shared/model/item-product.model';
import { ItemProductService } from './item-product.service';
import { ItemProductComponent } from './item-product.component';
import { ItemProductDetailComponent } from './item-product-detail.component';
import { ItemProductUpdateComponent } from './item-product-update.component';
import { ItemProductDeletePopupComponent } from './item-product-delete-dialog.component';
import { IItemProduct } from 'app/shared/model/item-product.model';

@Injectable({ providedIn: 'root' })
export class ItemProductResolve implements Resolve<IItemProduct> {
    constructor(private service: ItemProductService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((itemProduct: HttpResponse<ItemProduct>) => itemProduct.body));
        }
        return of(new ItemProduct());
    }
}

export const itemProductRoute: Routes = [
    {
        path: 'item-product',
        component: ItemProductComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ItemProducts'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'item-product/:id/view',
        component: ItemProductDetailComponent,
        resolve: {
            itemProduct: ItemProductResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ItemProducts'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'item-product/new',
        component: ItemProductUpdateComponent,
        resolve: {
            itemProduct: ItemProductResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ItemProducts'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'item-product/:id/edit',
        component: ItemProductUpdateComponent,
        resolve: {
            itemProduct: ItemProductResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ItemProducts'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const itemProductPopupRoute: Routes = [
    {
        path: 'item-product/:id/delete',
        component: ItemProductDeletePopupComponent,
        resolve: {
            itemProduct: ItemProductResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ItemProducts'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
