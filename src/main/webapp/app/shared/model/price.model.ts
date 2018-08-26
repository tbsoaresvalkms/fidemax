import { Moment } from 'moment';

export interface IPrice {
    id?: number;
    date?: Moment;
    unitPrice?: number;
    productId?: number;
}

export class Price implements IPrice {
    constructor(public id?: number, public date?: Moment, public unitPrice?: number, public productId?: number) {}
}
