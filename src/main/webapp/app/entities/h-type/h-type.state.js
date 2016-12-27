(function() {
    'use strict';

    angular
        .module('mapApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('h-type', {
            parent: 'entity',
            url: '/h-type',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mapApp.hType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/h-type/h-types.html',
                    controller: 'HTypeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('h-type-detail', {
            parent: 'entity',
            url: '/h-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mapApp.hType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/h-type/h-type-detail.html',
                    controller: 'HTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'HType', function($stateParams, HType) {
                    return HType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'h-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('h-type-detail.edit', {
            parent: 'h-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/h-type/h-type-dialog.html',
                    controller: 'HTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HType', function(HType) {
                            return HType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('h-type.new', {
            parent: 'h-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/h-type/h-type-dialog.html',
                    controller: 'HTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('h-type', null, { reload: 'h-type' });
                }, function() {
                    $state.go('h-type');
                });
            }]
        })
        .state('h-type.edit', {
            parent: 'h-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/h-type/h-type-dialog.html',
                    controller: 'HTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HType', function(HType) {
                            return HType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('h-type', null, { reload: 'h-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('h-type.delete', {
            parent: 'h-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/h-type/h-type-delete-dialog.html',
                    controller: 'HTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HType', function(HType) {
                            return HType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('h-type', null, { reload: 'h-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
