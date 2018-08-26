import { NgModule } from '@angular/core';

import { FidemaxSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [FidemaxSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [FidemaxSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class FidemaxSharedCommonModule {}
