(function() {
    'use strict';

    angular
        .module('mapApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('h-visualizer', {
            parent: 'entity',
            url: '/h-visualizer',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mapApp.hVisualizer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/h-visualizer/h-visualizers.html',
                    controller: 'HVisualizerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hVisualizer');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('h-visualizer-detail', {
            parent: 'entity',
            url: '/h-visualizer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mapApp.hVisualizer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/h-visualizer/h-visualizer-detail.html',
                    controller: 'HVisualizerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hVisualizer');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'HVisualizer', function($stateParams, HVisualizer) {
                    return HVisualizer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'h-visualizer',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('h-visualizer-detail.edit', {
            parent: 'h-visualizer-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/h-visualizer/h-visualizer-dialog.html',
                    controller: 'HVisualizerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HVisualizer', function(HVisualizer) {
                            return HVisualizer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('h-visualizer.new', {
            parent: 'h-visualizer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/h-visualizer/h-visualizer-dialog.html',
                    controller: 'HVisualizerDialogController',
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
                    $state.go('h-visualizer', null, { reload: 'h-visualizer' });
                }, function() {
                    $state.go('h-visualizer');
                });
            }]
        })
        .state('h-visualizer.edit', {
            parent: 'h-visualizer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/h-visualizer/h-visualizer-dialog.html',
                    controller: 'HVisualizerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HVisualizer', function(HVisualizer) {
                            return HVisualizer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('h-visualizer', null, { reload: 'h-visualizer' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('h-visualizer.delete', {
            parent: 'h-visualizer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/h-visualizer/h-visualizer-delete-dialog.html',
                    controller: 'HVisualizerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HVisualizer', function(HVisualizer) {
                            return HVisualizer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('h-visualizer', null, { reload: 'h-visualizer' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
