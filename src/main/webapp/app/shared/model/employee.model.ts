export const enum Office {
    MANAGER = 'MANAGER',
    SUPPORT = 'SUPPORT'
}

export interface IEmployee {
    id?: number;
    office?: Office;
    userId?: number;
    consumerTransactionId?: number;
    companyId?: number;
}

export class Employee implements IEmployee {
    constructor(
        public id?: number,
        public office?: Office,
        public userId?: number,
        public consumerTransactionId?: number,
        public companyId?: number
    ) {}
}
