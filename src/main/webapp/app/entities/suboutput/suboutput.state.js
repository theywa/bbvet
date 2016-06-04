(function() {
    'use strict';

    angular
        .module('raBbvetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('suboutput', {
            parent: 'entity',
            url: '/suboutput',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Suboutputs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/suboutput/suboutputs.html',
                    controller: 'SuboutputController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('suboutput-detail', {
            parent: 'entity',
            url: '/suboutput/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Suboutput'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/suboutput/suboutput-detail.html',
                    controller: 'SuboutputDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Suboutput', function($stateParams, Suboutput) {
                    return Suboutput.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('suboutput.new', {
            parent: 'suboutput',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/suboutput/suboutput-dialog.html',
                    controller: 'SuboutputDialogController',
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
                    $state.go('suboutput', null, { reload: true });
                }, function() {
                    $state.go('suboutput');
                });
            }]
        })
        .state('suboutput.edit', {
            parent: 'suboutput',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/suboutput/suboutput-dialog.html',
                    controller: 'SuboutputDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Suboutput', function(Suboutput) {
                            return Suboutput.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('suboutput', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('suboutput.delete', {
            parent: 'suboutput',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/suboutput/suboutput-delete-dialog.html',
                    controller: 'SuboutputDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Suboutput', function(Suboutput) {
                            return Suboutput.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('suboutput', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
