import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FidemaxSharedModule } from 'app/shared';
import {
    ConsumerTransactionComponent,
    ConsumerTransactionDetailComponent,
    ConsumerTransactionUpdateComponent,
    ConsumerTransactionDeletePopupComponent,
    ConsumerTransactionDeleteDialogComponent,
    consumerTransactionRoute,
    consumerTransactionPopupRoute
} from './';

const ENTITY_STATES = [...consumerTransactionRoute, ...consumerTransactionPopupRoute];

@NgModule({
    imports: [FidemaxSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ConsumerTransactionComponent,
        ConsumerTransactionDetailComponent,
        ConsumerTransactionUpdateComponent,
        ConsumerTransactionDeleteDialogComponent,
        ConsumerTransactionDeletePopupComponent
    ],
    entryComponents: [
        ConsumerTransactionComponent,
        ConsumerTransactionUpdateComponent,
        ConsumerTransactionDeleteDialogComponent,
        ConsumerTransactionDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FidemaxConsumerTransactionModule {}
