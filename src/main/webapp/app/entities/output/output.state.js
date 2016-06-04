(function() {
    'use strict';

    angular
        .module('raBbvetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('output', {
            parent: 'entity',
            url: '/output',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Outputs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/output/outputs.html',
                    controller: 'OutputController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('output-detail', {
            parent: 'entity',
            url: '/output/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Output'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/output/output-detail.html',
                    controller: 'OutputDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Output', function($stateParams, Output) {
                    return Output.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('output.new', {
            parent: 'output',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/output/output-dialog.html',
                    controller: 'OutputDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                kode: null,
                                judul: null,
                                volume: null,
                                satuan: null,
                                createdDate: null,
                                createdBy: null,
                                updatedDate: null,
                                updatedBy: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('output', null, { reload: true });
                }, function() {
                    $state.go('output');
                });
            }]
        })
        .state('output.edit', {
            parent: 'output',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/output/output-dialog.html',
                    controller: 'OutputDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Output', function(Output) {
                            return Output.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('output', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('output.delete', {
            parent: 'output',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/output/output-delete-dialog.html',
                    controller: 'OutputDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Output', function(Output) {
                            return Output.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('output', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
