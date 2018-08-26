import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { FidemaxCustomerModule } from './customer/customer.module';
import { FidemaxPortfolioModule } from './portfolio/portfolio.module';
import { FidemaxCompanyModule } from './company/company.module';
import { FidemaxEmployeeModule } from './employee/employee.module';
import { FidemaxConsumerTransactionModule } from './consumer-transaction/consumer-transaction.module';
import { FidemaxRedemptionTransactionModule } from './redemption-transaction/redemption-transaction.module';
import { FidemaxItemProductModule } from './item-product/item-product.module';
import { FidemaxPriceModule } from './price/price.module';
import { FidemaxProductModule } from './product/product.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        FidemaxCustomerModule,
        FidemaxPortfolioModule,
        FidemaxCompanyModule,
        FidemaxEmployeeModule,
        FidemaxConsumerTransactionModule,
        FidemaxRedemptionTransactionModule,
        FidemaxItemProductModule,
        FidemaxPriceModule,
        FidemaxProductModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FidemaxEntityModule {}
