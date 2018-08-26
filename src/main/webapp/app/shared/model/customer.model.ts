export interface ICustomer {
    id?: number;
    cpf?: string;
    imageContentType?: string;
    image?: any;
    portfolioId?: number;
    userId?: number;
}

export class Customer implements ICustomer {
    constructor(
        public id?: number,
        public cpf?: string,
        public imageContentType?: string,
        public image?: any,
        public portfolioId?: number,
        public userId?: number
    ) {}
}
