import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Price } from 'app/shared/model/price.model';
import { PriceService } from './price.service';
import { PriceComponent } from './price.component';
import { PriceDetailComponent } from './price-detail.component';
import { PriceUpdateComponent } from './price-update.component';
import { PriceDeletePopupComponent } from './price-delete-dialog.component';
import { IPrice } from 'app/shared/model/price.model';

@Injectable({ providedIn: 'root' })
export class PriceResolve implements Resolve<IPrice> {
    constructor(private service: PriceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((price: HttpResponse<Price>) => price.body));
        }
        return of(new Price());
    }
}

export const priceRoute: Routes = [
    {
        path: 'price',
        component: PriceComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Prices'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'price/:id/view',
        component: PriceDetailComponent,
        resolve: {
            price: PriceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Prices'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'price/new',
        component: PriceUpdateComponent,
        resolve: {
            price: PriceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Prices'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'price/:id/edit',
        component: PriceUpdateComponent,
        resolve: {
            price: PriceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Prices'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pricePopupRoute: Routes = [
    {
        path: 'price/:id/delete',
        component: PriceDeletePopupComponent,
        resolve: {
            price: PriceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Prices'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
