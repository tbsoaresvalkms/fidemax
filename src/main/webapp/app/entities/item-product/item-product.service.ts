import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IItemProduct } from 'app/shared/model/item-product.model';

type EntityResponseType = HttpResponse<IItemProduct>;
type EntityArrayResponseType = HttpResponse<IItemProduct[]>;

@Injectable({ providedIn: 'root' })
export class ItemProductService {
    private resourceUrl = SERVER_API_URL + 'api/item-products';

    constructor(private http: HttpClient) {}

    create(itemProduct: IItemProduct): Observable<EntityResponseType> {
        return this.http.post<IItemProduct>(this.resourceUrl, itemProduct, { observe: 'response' });
    }

    update(itemProduct: IItemProduct): Observable<EntityResponseType> {
        return this.http.put<IItemProduct>(this.resourceUrl, itemProduct, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IItemProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IItemProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
