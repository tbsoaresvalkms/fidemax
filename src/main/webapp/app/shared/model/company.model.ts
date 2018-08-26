import { IConsumerTransaction } from 'app/shared/model//consumer-transaction.model';
import { IRedemptionTransaction } from 'app/shared/model//redemption-transaction.model';
import { IEmployee } from 'app/shared/model//employee.model';

export interface ICompany {
    id?: number;
    name?: string;
    cnpj?: string;
    consumerTransactions?: IConsumerTransaction[];
    redemptionTransactions?: IRedemptionTransaction[];
    employees?: IEmployee[];
}

export class Company implements ICompany {
    constructor(
        public id?: number,
        public name?: string,
        public cnpj?: string,
        public consumerTransactions?: IConsumerTransaction[],
        public redemptionTransactions?: IRedemptionTransaction[],
        public employees?: IEmployee[]
    ) {}
}
