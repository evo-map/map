(function() {
    'use strict';

    angular
        .module('mapApp')
        .controller('HRelDetailController', HRelDetailController);

    HRelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HRel', 'HType'];

    function HRelDetailController($scope, $rootScope, $stateParams, previousState, entity, HRel, HType) {
        var vm = this;

        vm.hRel = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mapApp:hRelUpdate', function(event, result) {
            vm.hRel = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
