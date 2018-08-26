import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConsumerTransaction } from 'app/shared/model/consumer-transaction.model';

@Component({
    selector: 'jhi-consumer-transaction-detail',
    templateUrl: './consumer-transaction-detail.component.html'
})
export class ConsumerTransactionDetailComponent implements OnInit {
    consumerTransaction: IConsumerTransaction;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ consumerTransaction }) => {
            this.consumerTransaction = consumerTransaction;
        });
    }

    previousState() {
        window.history.back();
    }
}
