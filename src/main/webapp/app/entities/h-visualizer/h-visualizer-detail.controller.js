(function() {
    'use strict';

    angular
        .module('mapApp')
        .controller('HVisualizerDetailController', HVisualizerDetailController);

    HVisualizerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HVisualizer', 'HType'];

    function HVisualizerDetailController($scope, $rootScope, $stateParams, previousState, entity, HVisualizer, HType) {
        var vm = this;

        vm.hVisualizer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mapApp:hVisualizerUpdate', function(event, result) {
            vm.hVisualizer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
