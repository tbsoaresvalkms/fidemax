import { IConsumerTransaction } from 'app/shared/model//consumer-transaction.model';
import { IRedemptionTransaction } from 'app/shared/model//redemption-transaction.model';

export interface IPortfolio {
    id?: number;
    balance?: number;
    consumerTransactions?: IConsumerTransaction[];
    redemptionTransactions?: IRedemptionTransaction[];
    customerId?: number;
}

export class Portfolio implements IPortfolio {
    constructor(
        public id?: number,
        public balance?: number,
        public consumerTransactions?: IConsumerTransaction[],
        public redemptionTransactions?: IRedemptionTransaction[],
        public customerId?: number
    ) {}
}
