(function() {
    'use strict';

    angular
        .module('raBbvetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('kegiatan', {
            parent: 'entity',
            url: '/kegiatan',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Kegiatans'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/kegiatan/kegiatans.html',
                    controller: 'KegiatanController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('kegiatan-detail', {
            parent: 'entity',
            url: '/kegiatan/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Kegiatan'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/kegiatan/kegiatan-detail.html',
                    controller: 'KegiatanDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Kegiatan', function($stateParams, Kegiatan) {
                    return Kegiatan.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('kegiatan.new', {
            parent: 'kegiatan',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/kegiatan/kegiatan-dialog.html',
                    controller: 'KegiatanDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                kode: null,
                                judul: null,
                                createdDate: null,
                                createdBy: null,
                                updatedDate: null,
                                updatedBy: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('kegiatan', null, { reload: true });
                }, function() {
                    $state.go('kegiatan');
                });
            }]
        })
        .state('kegiatan.edit', {
            parent: 'kegiatan',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/kegiatan/kegiatan-dialog.html',
                    controller: 'KegiatanDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Kegiatan', function(Kegiatan) {
                            return Kegiatan.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('kegiatan', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('kegiatan.delete', {
            parent: 'kegiatan',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/kegiatan/kegiatan-delete-dialog.html',
                    controller: 'KegiatanDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Kegiatan', function(Kegiatan) {
                            return Kegiatan.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('kegiatan', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
