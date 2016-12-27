(function() {
    'use strict';

    angular
        .module('mapApp')
        .controller('HTypeController', HTypeController);

    HTypeController.$inject = ['$scope', '$state', 'HType'];

    function HTypeController ($scope, $state, HType) {
        var vm = this;

        vm.hTypes = [];

        loadAll();

        function loadAll() {
            HType.query(function(result) {
                vm.hTypes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
