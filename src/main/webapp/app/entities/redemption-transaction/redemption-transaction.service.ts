import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRedemptionTransaction } from 'app/shared/model/redemption-transaction.model';

type EntityResponseType = HttpResponse<IRedemptionTransaction>;
type EntityArrayResponseType = HttpResponse<IRedemptionTransaction[]>;

@Injectable({ providedIn: 'root' })
export class RedemptionTransactionService {
    private resourceUrl = SERVER_API_URL + 'api/redemption-transactions';

    constructor(private http: HttpClient) {}

    create(redemptionTransaction: IRedemptionTransaction): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(redemptionTransaction);
        return this.http
            .post<IRedemptionTransaction>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(redemptionTransaction: IRedemptionTransaction): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(redemptionTransaction);
        return this.http
            .put<IRedemptionTransaction>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IRedemptionTransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRedemptionTransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(redemptionTransaction: IRedemptionTransaction): IRedemptionTransaction {
        const copy: IRedemptionTransaction = Object.assign({}, redemptionTransaction, {
            date:
                redemptionTransaction.date != null && redemptionTransaction.date.isValid()
                    ? redemptionTransaction.date.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((redemptionTransaction: IRedemptionTransaction) => {
            redemptionTransaction.date = redemptionTransaction.date != null ? moment(redemptionTransaction.date) : null;
        });
        return res;
    }
}
