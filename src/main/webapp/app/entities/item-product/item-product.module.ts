import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FidemaxSharedModule } from 'app/shared';
import {
    ItemProductComponent,
    ItemProductDetailComponent,
    ItemProductUpdateComponent,
    ItemProductDeletePopupComponent,
    ItemProductDeleteDialogComponent,
    itemProductRoute,
    itemProductPopupRoute
} from './';

const ENTITY_STATES = [...itemProductRoute, ...itemProductPopupRoute];

@NgModule({
    imports: [FidemaxSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ItemProductComponent,
        ItemProductDetailComponent,
        ItemProductUpdateComponent,
        ItemProductDeleteDialogComponent,
        ItemProductDeletePopupComponent
    ],
    entryComponents: [ItemProductComponent, ItemProductUpdateComponent, ItemProductDeleteDialogComponent, ItemProductDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FidemaxItemProductModule {}
