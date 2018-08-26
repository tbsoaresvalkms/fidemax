import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRedemptionTransaction } from 'app/shared/model/redemption-transaction.model';

@Component({
    selector: 'jhi-redemption-transaction-detail',
    templateUrl: './redemption-transaction-detail.component.html'
})
export class RedemptionTransactionDetailComponent implements OnInit {
    redemptionTransaction: IRedemptionTransaction;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ redemptionTransaction }) => {
            this.redemptionTransaction = redemptionTransaction;
        });
    }

    previousState() {
        window.history.back();
    }
}
