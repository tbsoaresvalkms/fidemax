import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IRedemptionTransaction } from 'app/shared/model/redemption-transaction.model';
import { RedemptionTransactionService } from './redemption-transaction.service';
import { IItemProduct } from 'app/shared/model/item-product.model';
import { ItemProductService } from 'app/entities/item-product';
import { IPortfolio } from 'app/shared/model/portfolio.model';
import { PortfolioService } from 'app/entities/portfolio';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company';

@Component({
    selector: 'jhi-redemption-transaction-update',
    templateUrl: './redemption-transaction-update.component.html'
})
export class RedemptionTransactionUpdateComponent implements OnInit {
    private _redemptionTransaction: IRedemptionTransaction;
    isSaving: boolean;

    itemproducts: IItemProduct[];

    portfolios: IPortfolio[];

    companies: ICompany[];
    dateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private redemptionTransactionService: RedemptionTransactionService,
        private itemProductService: ItemProductService,
        private portfolioService: PortfolioService,
        private companyService: CompanyService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ redemptionTransaction }) => {
            this.redemptionTransaction = redemptionTransaction;
        });
        this.itemProductService.query({ filter: 'redemptiontransaction-is-null' }).subscribe(
            (res: HttpResponse<IItemProduct[]>) => {
                if (!this.redemptionTransaction.itemProductId) {
                    this.itemproducts = res.body;
                } else {
                    this.itemProductService.find(this.redemptionTransaction.itemProductId).subscribe(
                        (subRes: HttpResponse<IItemProduct>) => {
                            this.itemproducts = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.portfolioService.query().subscribe(
            (res: HttpResponse<IPortfolio[]>) => {
                this.portfolios = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.companyService.query().subscribe(
            (res: HttpResponse<ICompany[]>) => {
                this.companies = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.redemptionTransaction.id !== undefined) {
            this.subscribeToSaveResponse(this.redemptionTransactionService.update(this.redemptionTransaction));
        } else {
            this.subscribeToSaveResponse(this.redemptionTransactionService.create(this.redemptionTransaction));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IRedemptionTransaction>>) {
        result.subscribe(
            (res: HttpResponse<IRedemptionTransaction>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackItemProductById(index: number, item: IItemProduct) {
        return item.id;
    }

    trackPortfolioById(index: number, item: IPortfolio) {
        return item.id;
    }

    trackCompanyById(index: number, item: ICompany) {
        return item.id;
    }
    get redemptionTransaction() {
        return this._redemptionTransaction;
    }

    set redemptionTransaction(redemptionTransaction: IRedemptionTransaction) {
        this._redemptionTransaction = redemptionTransaction;
    }
}
