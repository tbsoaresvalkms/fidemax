import { Moment } from 'moment';

export interface IConsumerTransaction {
    id?: number;
    date?: Moment;
    balance?: number;
    employeeId?: number;
    portfolioId?: number;
    companyId?: number;
}

export class ConsumerTransaction implements IConsumerTransaction {
    constructor(
        public id?: number,
        public date?: Moment,
        public balance?: number,
        public employeeId?: number,
        public portfolioId?: number,
        public companyId?: number
    ) {}
}
