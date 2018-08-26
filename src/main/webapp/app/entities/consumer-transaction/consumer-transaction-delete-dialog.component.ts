import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IConsumerTransaction } from 'app/shared/model/consumer-transaction.model';
import { ConsumerTransactionService } from './consumer-transaction.service';

@Component({
    selector: 'jhi-consumer-transaction-delete-dialog',
    templateUrl: './consumer-transaction-delete-dialog.component.html'
})
export class ConsumerTransactionDeleteDialogComponent {
    consumerTransaction: IConsumerTransaction;

    constructor(
        private consumerTransactionService: ConsumerTransactionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.consumerTransactionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'consumerTransactionListModification',
                content: 'Deleted an consumerTransaction'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-consumer-transaction-delete-popup',
    template: ''
})
export class ConsumerTransactionDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ consumerTransaction }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ConsumerTransactionDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.consumerTransaction = consumerTransaction;
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
