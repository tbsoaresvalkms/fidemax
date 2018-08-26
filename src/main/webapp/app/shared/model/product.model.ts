import { IPrice } from 'app/shared/model//price.model';

export interface IProduct {
    id?: number;
    name?: string;
    description?: string;
    imageContentType?: string;
    image?: any;
    prices?: IPrice[];
}

export class Product implements IProduct {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public imageContentType?: string,
        public image?: any,
        public prices?: IPrice[]
    ) {}
}
