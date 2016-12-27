(function() {
    'use strict';

    angular
        .module('mapApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('h-descriptor', {
            parent: 'entity',
            url: '/h-descriptor',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mapApp.hDescriptor.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/h-descriptor/h-descriptors.html',
                    controller: 'HDescriptorController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hDescriptor');
                    $translatePartialLoader.addPart('hDescriptorType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('h-descriptor-detail', {
            parent: 'entity',
            url: '/h-descriptor/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mapApp.hDescriptor.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/h-descriptor/h-descriptor-detail.html',
                    controller: 'HDescriptorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hDescriptor');
                    $translatePartialLoader.addPart('hDescriptorType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'HDescriptor', function($stateParams, HDescriptor) {
                    return HDescriptor.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'h-descriptor',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('h-descriptor-detail.edit', {
            parent: 'h-descriptor-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/h-descriptor/h-descriptor-dialog.html',
                    controller: 'HDescriptorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HDescriptor', function(HDescriptor) {
                            return HDescriptor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('h-descriptor.new', {
            parent: 'h-descriptor',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/h-descriptor/h-descriptor-dialog.html',
                    controller: 'HDescriptorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                type: null,
                                decscriptorBody: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('h-descriptor', null, { reload: 'h-descriptor' });
                }, function() {
                    $state.go('h-descriptor');
                });
            }]
        })
        .state('h-descriptor.edit', {
            parent: 'h-descriptor',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/h-descriptor/h-descriptor-dialog.html',
                    controller: 'HDescriptorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HDescriptor', function(HDescriptor) {
                            return HDescriptor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('h-descriptor', null, { reload: 'h-descriptor' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('h-descriptor.delete', {
            parent: 'h-descriptor',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/h-descriptor/h-descriptor-delete-dialog.html',
                    controller: 'HDescriptorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HDescriptor', function(HDescriptor) {
                            return HDescriptor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('h-descriptor', null, { reload: 'h-descriptor' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
