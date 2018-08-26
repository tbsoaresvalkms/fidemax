import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { RedemptionTransaction } from 'app/shared/model/redemption-transaction.model';
import { RedemptionTransactionService } from './redemption-transaction.service';
import { RedemptionTransactionComponent } from './redemption-transaction.component';
import { RedemptionTransactionDetailComponent } from './redemption-transaction-detail.component';
import { RedemptionTransactionUpdateComponent } from './redemption-transaction-update.component';
import { RedemptionTransactionDeletePopupComponent } from './redemption-transaction-delete-dialog.component';
import { IRedemptionTransaction } from 'app/shared/model/redemption-transaction.model';

@Injectable({ providedIn: 'root' })
export class RedemptionTransactionResolve implements Resolve<IRedemptionTransaction> {
    constructor(private service: RedemptionTransactionService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((redemptionTransaction: HttpResponse<RedemptionTransaction>) => redemptionTransaction.body));
        }
        return of(new RedemptionTransaction());
    }
}

export const redemptionTransactionRoute: Routes = [
    {
        path: 'redemption-transaction',
        component: RedemptionTransactionComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RedemptionTransactions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'redemption-transaction/:id/view',
        component: RedemptionTransactionDetailComponent,
        resolve: {
            redemptionTransaction: RedemptionTransactionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RedemptionTransactions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'redemption-transaction/new',
        component: RedemptionTransactionUpdateComponent,
        resolve: {
            redemptionTransaction: RedemptionTransactionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RedemptionTransactions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'redemption-transaction/:id/edit',
        component: RedemptionTransactionUpdateComponent,
        resolve: {
            redemptionTransaction: RedemptionTransactionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RedemptionTransactions'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const redemptionTransactionPopupRoute: Routes = [
    {
        path: 'redemption-transaction/:id/delete',
        component: RedemptionTransactionDeletePopupComponent,
        resolve: {
            redemptionTransaction: RedemptionTransactionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RedemptionTransactions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
