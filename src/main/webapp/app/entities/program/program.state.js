(function() {
    'use strict';

    angular
        .module('raBbvetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('program', {
            parent: 'entity',
            url: '/program',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Programs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/program/programs.html',
                    controller: 'ProgramController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('program-detail', {
            parent: 'entity',
            url: '/program/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Program'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/program/program-detail.html',
                    controller: 'ProgramDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Program', function($stateParams, Program) {
                    return Program.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('program.new', {
            parent: 'program',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program/program-dialog.html',
                    controller: 'ProgramDialogController',
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
                    $state.go('program', null, { reload: true });
                }, function() {
                    $state.go('program');
                });
            }]
        })
        .state('program.edit', {
            parent: 'program',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program/program-dialog.html',
                    controller: 'ProgramDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Program', function(Program) {
                            return Program.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('program', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('program.delete', {
            parent: 'program',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program/program-delete-dialog.html',
                    controller: 'ProgramDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Program', function(Program) {
                            return Program.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('program', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
