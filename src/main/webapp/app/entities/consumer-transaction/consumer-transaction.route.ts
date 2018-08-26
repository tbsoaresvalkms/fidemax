import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ConsumerTransaction } from 'app/shared/model/consumer-transaction.model';
import { ConsumerTransactionService } from './consumer-transaction.service';
import { ConsumerTransactionComponent } from './consumer-transaction.component';
import { ConsumerTransactionDetailComponent } from './consumer-transaction-detail.component';
import { ConsumerTransactionUpdateComponent } from './consumer-transaction-update.component';
import { ConsumerTransactionDeletePopupComponent } from './consumer-transaction-delete-dialog.component';
import { IConsumerTransaction } from 'app/shared/model/consumer-transaction.model';

@Injectable({ providedIn: 'root' })
export class ConsumerTransactionResolve implements Resolve<IConsumerTransaction> {
    constructor(private service: ConsumerTransactionService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((consumerTransaction: HttpResponse<ConsumerTransaction>) => consumerTransaction.body));
        }
        return of(new ConsumerTransaction());
    }
}

export const consumerTransactionRoute: Routes = [
    {
        path: 'consumer-transaction',
        component: ConsumerTransactionComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ConsumerTransactions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'consumer-transaction/:id/view',
        component: ConsumerTransactionDetailComponent,
        resolve: {
            consumerTransaction: ConsumerTransactionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ConsumerTransactions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'consumer-transaction/new',
        component: ConsumerTransactionUpdateComponent,
        resolve: {
            consumerTransaction: ConsumerTransactionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ConsumerTransactions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'consumer-transaction/:id/edit',
        component: ConsumerTransactionUpdateComponent,
        resolve: {
            consumerTransaction: ConsumerTransactionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ConsumerTransactions'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const consumerTransactionPopupRoute: Routes = [
    {
        path: 'consumer-transaction/:id/delete',
        component: ConsumerTransactionDeletePopupComponent,
        resolve: {
            consumerTransaction: ConsumerTransactionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ConsumerTransactions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
