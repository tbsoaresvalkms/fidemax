import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IConsumerTransaction } from 'app/shared/model/consumer-transaction.model';
import { ConsumerTransactionService } from './consumer-transaction.service';
import { IEmployee } from 'app/shared/model/employee.model';
import { EmployeeService } from 'app/entities/employee';
import { IPortfolio } from 'app/shared/model/portfolio.model';
import { PortfolioService } from 'app/entities/portfolio';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company';

@Component({
    selector: 'jhi-consumer-transaction-update',
    templateUrl: './consumer-transaction-update.component.html'
})
export class ConsumerTransactionUpdateComponent implements OnInit {
    private _consumerTransaction: IConsumerTransaction;
    isSaving: boolean;

    employees: IEmployee[];

    portfolios: IPortfolio[];

    companies: ICompany[];
    dateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private consumerTransactionService: ConsumerTransactionService,
        private employeeService: EmployeeService,
        private portfolioService: PortfolioService,
        private companyService: CompanyService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ consumerTransaction }) => {
            this.consumerTransaction = consumerTransaction;
        });
        this.employeeService.query({ filter: 'consumertransaction-is-null' }).subscribe(
            (res: HttpResponse<IEmployee[]>) => {
                if (!this.consumerTransaction.employeeId) {
                    this.employees = res.body;
                } else {
                    this.employeeService.find(this.consumerTransaction.employeeId).subscribe(
                        (subRes: HttpResponse<IEmployee>) => {
                            this.employees = [subRes.body].concat(res.body);
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
        if (this.consumerTransaction.id !== undefined) {
            this.subscribeToSaveResponse(this.consumerTransactionService.update(this.consumerTransaction));
        } else {
            this.subscribeToSaveResponse(this.consumerTransactionService.create(this.consumerTransaction));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IConsumerTransaction>>) {
        result.subscribe((res: HttpResponse<IConsumerTransaction>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackEmployeeById(index: number, item: IEmployee) {
        return item.id;
    }

    trackPortfolioById(index: number, item: IPortfolio) {
        return item.id;
    }

    trackCompanyById(index: number, item: ICompany) {
        return item.id;
    }
    get consumerTransaction() {
        return this._consumerTransaction;
    }

    set consumerTransaction(consumerTransaction: IConsumerTransaction) {
        this._consumerTransaction = consumerTransaction;
    }
}
