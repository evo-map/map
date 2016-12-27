(function() {
    'use strict';

    angular
        .module('mapApp')
        .controller('HTypeDetailController', HTypeDetailController);

    HTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HType', 'HRel', 'HVisualizer', 'HDescriptor'];

    function HTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, HType, HRel, HVisualizer, HDescriptor) {
        var vm = this;

        vm.hType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mapApp:hTypeUpdate', function(event, result) {
            vm.hType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
