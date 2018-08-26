export interface IItemProduct {
    id?: number;
    count?: number;
    unitPrice?: number;
    productId?: number;
    redemptionTransactionId?: number;
}

export class ItemProduct implements IItemProduct {
    constructor(
        public id?: number,
        public count?: number,
        public unitPrice?: number,
        public productId?: number,
        public redemptionTransactionId?: number
    ) {}
}
