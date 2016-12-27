(function() {
    'use strict';

    angular
        .module('mapApp')
        .controller('HRelController', HRelController);

    HRelController.$inject = ['$scope', '$state', 'HRel'];

    function HRelController ($scope, $state, HRel) {
        var vm = this;

        vm.hRels = [];

        loadAll();

        function loadAll() {
            HRel.query(function(result) {
                vm.hRels = result;
                vm.searchQuery = null;
            });
        }
    }
})();
