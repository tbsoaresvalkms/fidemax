import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FidemaxSharedModule } from 'app/shared';
import {
    RedemptionTransactionComponent,
    RedemptionTransactionDetailComponent,
    RedemptionTransactionUpdateComponent,
    RedemptionTransactionDeletePopupComponent,
    RedemptionTransactionDeleteDialogComponent,
    redemptionTransactionRoute,
    redemptionTransactionPopupRoute
} from './';

const ENTITY_STATES = [...redemptionTransactionRoute, ...redemptionTransactionPopupRoute];

@NgModule({
    imports: [FidemaxSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RedemptionTransactionComponent,
        RedemptionTransactionDetailComponent,
        RedemptionTransactionUpdateComponent,
        RedemptionTransactionDeleteDialogComponent,
        RedemptionTransactionDeletePopupComponent
    ],
    entryComponents: [
        RedemptionTransactionComponent,
        RedemptionTransactionUpdateComponent,
        RedemptionTransactionDeleteDialogComponent,
        RedemptionTransactionDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FidemaxRedemptionTransactionModule {}
