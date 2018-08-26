import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRedemptionTransaction } from 'app/shared/model/redemption-transaction.model';
import { RedemptionTransactionService } from './redemption-transaction.service';

@Component({
    selector: 'jhi-redemption-transaction-delete-dialog',
    templateUrl: './redemption-transaction-delete-dialog.component.html'
})
export class RedemptionTransactionDeleteDialogComponent {
    redemptionTransaction: IRedemptionTransaction;

    constructor(
        private redemptionTransactionService: RedemptionTransactionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.redemptionTransactionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'redemptionTransactionListModification',
                content: 'Deleted an redemptionTransaction'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-redemption-transaction-delete-popup',
    template: ''
})
export class RedemptionTransactionDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ redemptionTransaction }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RedemptionTransactionDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.redemptionTransaction = redemptionTransaction;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
