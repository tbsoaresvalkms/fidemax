import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FidemaxSharedModule } from 'app/shared';
import {
    PortfolioComponent,
    PortfolioDetailComponent,
    PortfolioUpdateComponent,
    PortfolioDeletePopupComponent,
    PortfolioDeleteDialogComponent,
    portfolioRoute,
    portfolioPopupRoute
} from './';

const ENTITY_STATES = [...portfolioRoute, ...portfolioPopupRoute];

@NgModule({
    imports: [FidemaxSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PortfolioComponent,
        PortfolioDetailComponent,
        PortfolioUpdateComponent,
        PortfolioDeleteDialogComponent,
        PortfolioDeletePopupComponent
    ],
    entryComponents: [PortfolioComponent, PortfolioUpdateComponent, PortfolioDeleteDialogComponent, PortfolioDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FidemaxPortfolioModule {}
