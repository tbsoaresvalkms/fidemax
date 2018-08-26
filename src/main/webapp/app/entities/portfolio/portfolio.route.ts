import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Portfolio } from 'app/shared/model/portfolio.model';
import { PortfolioService } from './portfolio.service';
import { PortfolioComponent } from './portfolio.component';
import { PortfolioDetailComponent } from './portfolio-detail.component';
import { PortfolioUpdateComponent } from './portfolio-update.component';
import { PortfolioDeletePopupComponent } from './portfolio-delete-dialog.component';
import { IPortfolio } from 'app/shared/model/portfolio.model';

@Injectable({ providedIn: 'root' })
export class PortfolioResolve implements Resolve<IPortfolio> {
    constructor(private service: PortfolioService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((portfolio: HttpResponse<Portfolio>) => portfolio.body));
        }
        return of(new Portfolio());
    }
}

export const portfolioRoute: Routes = [
    {
        path: 'portfolio',
        component: PortfolioComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Portfolios'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'portfolio/:id/view',
        component: PortfolioDetailComponent,
        resolve: {
            portfolio: PortfolioResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Portfolios'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'portfolio/new',
        component: PortfolioUpdateComponent,
        resolve: {
            portfolio: PortfolioResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Portfolios'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'portfolio/:id/edit',
        component: PortfolioUpdateComponent,
        resolve: {
            portfolio: PortfolioResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Portfolios'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const portfolioPopupRoute: Routes = [
    {
        path: 'portfolio/:id/delete',
        component: PortfolioDeletePopupComponent,
        resolve: {
            portfolio: PortfolioResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Portfolios'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
