import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IConsumerTransaction } from 'app/shared/model/consumer-transaction.model';

type EntityResponseType = HttpResponse<IConsumerTransaction>;
type EntityArrayResponseType = HttpResponse<IConsumerTransaction[]>;

@Injectable({ providedIn: 'root' })
export class ConsumerTransactionService {
    private resourceUrl = SERVER_API_URL + 'api/consumer-transactions';

    constructor(private http: HttpClient) {}

    create(consumerTransaction: IConsumerTransaction): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(consumerTransaction);
        return this.http
            .post<IConsumerTransaction>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(consumerTransaction: IConsumerTransaction): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(consumerTransaction);
        return this.http
            .put<IConsumerTransaction>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IConsumerTransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IConsumerTransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(consumerTransaction: IConsumerTransaction): IConsumerTransaction {
        const copy: IConsumerTransaction = Object.assign({}, consumerTransaction, {
            date:
                consumerTransaction.date != null && consumerTransaction.date.isValid() ? consumerTransaction.date.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((consumerTransaction: IConsumerTransaction) => {
            consumerTransaction.date = consumerTransaction.date != null ? moment(consumerTransaction.date) : null;
        });
        return res;
    }
}
