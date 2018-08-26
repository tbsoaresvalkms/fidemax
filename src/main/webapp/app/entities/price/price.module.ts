import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FidemaxSharedModule } from 'app/shared';
import {
    PriceComponent,
    PriceDetailComponent,
    PriceUpdateComponent,
    PriceDeletePopupComponent,
    PriceDeleteDialogComponent,
    priceRoute,
    pricePopupRoute
} from './';

const ENTITY_STATES = [...priceRoute, ...pricePopupRoute];

@NgModule({
    imports: [FidemaxSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [PriceComponent, PriceDetailComponent, PriceUpdateComponent, PriceDeleteDialogComponent, PriceDeletePopupComponent],
    entryComponents: [PriceComponent, PriceUpdateComponent, PriceDeleteDialogComponent, PriceDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FidemaxPriceModule {}
