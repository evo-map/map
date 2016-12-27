(function() {
    'use strict';

    angular
        .module('mapApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('h-rel', {
            parent: 'entity',
            url: '/h-rel',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mapApp.hRel.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/h-rel/h-rels.html',
                    controller: 'HRelController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hRel');
                    $translatePartialLoader.addPart('relationshipSemantic');
                    $translatePartialLoader.addPart('deletionSemantic');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('h-rel-detail', {
            parent: 'entity',
            url: '/h-rel/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mapApp.hRel.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/h-rel/h-rel-detail.html',
                    controller: 'HRelDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hRel');
                    $translatePartialLoader.addPart('relationshipSemantic');
                    $translatePartialLoader.addPart('deletionSemantic');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'HRel', function($stateParams, HRel) {
                    return HRel.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'h-rel',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('h-rel-detail.edit', {
            parent: 'h-rel-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/h-rel/h-rel-dialog.html',
                    controller: 'HRelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HRel', function(HRel) {
                            return HRel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('h-rel.new', {
            parent: 'h-rel',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/h-rel/h-rel-dialog.html',
                    controller: 'HRelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                relationshipSemantic: null,
                                deletionSemantic: null,
                                maxToCardinality: null,
                                maxFromCardinality: null,
                                requiresToHolon: null,
                                requiresFromHolon: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('h-rel', null, { reload: 'h-rel' });
                }, function() {
                    $state.go('h-rel');
                });
            }]
        })
        .state('h-rel.edit', {
            parent: 'h-rel',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/h-rel/h-rel-dialog.html',
                    controller: 'HRelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HRel', function(HRel) {
                            return HRel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('h-rel', null, { reload: 'h-rel' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('h-rel.delete', {
            parent: 'h-rel',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/h-rel/h-rel-delete-dialog.html',
                    controller: 'HRelDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HRel', function(HRel) {
                            return HRel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('h-rel', null, { reload: 'h-rel' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
