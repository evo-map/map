(function() {
    'use strict';

    angular
        .module('mapApp')
        .controller('HDescriptorController', HDescriptorController);

    HDescriptorController.$inject = ['$scope', '$state', 'HDescriptor'];

    function HDescriptorController ($scope, $state, HDescriptor) {
        var vm = this;

        vm.hDescriptors = [];

        loadAll();

        function loadAll() {
            HDescriptor.query(function(result) {
                vm.hDescriptors = result;
                vm.searchQuery = null;
            });
        }
    }
})();
