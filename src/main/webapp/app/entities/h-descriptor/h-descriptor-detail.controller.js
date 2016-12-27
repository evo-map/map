(function() {
    'use strict';

    angular
        .module('mapApp')
        .controller('HDescriptorDetailController', HDescriptorDetailController);

    HDescriptorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HDescriptor', 'HType'];

    function HDescriptorDetailController($scope, $rootScope, $stateParams, previousState, entity, HDescriptor, HType) {
        var vm = this;

        vm.hDescriptor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mapApp:hDescriptorUpdate', function(event, result) {
            vm.hDescriptor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
