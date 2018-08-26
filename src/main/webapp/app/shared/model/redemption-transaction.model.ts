import { Moment } from 'moment';

export interface IRedemptionTransaction {
    id?: number;
    date?: Moment;
    balance?: number;
    itemProductId?: number;
    portfolioId?: number;
    companyId?: number;
}

export class RedemptionTransaction implements IRedemptionTransaction {
    constructor(
        public id?: number,
        public date?: Moment,
        public balance?: number,
        public itemProductId?: number,
        public portfolioId?: number,
        public companyId?: number
    ) {}
}
