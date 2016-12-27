(function() {
    'use strict';

    angular
        .module('mapApp')
        .controller('HVisualizerController', HVisualizerController);

    HVisualizerController.$inject = ['$scope', '$state', 'HVisualizer'];

    function HVisualizerController ($scope, $state, HVisualizer) {
        var vm = this;

        vm.hVisualizers = [];

        loadAll();

        function loadAll() {
            HVisualizer.query(function(result) {
                vm.hVisualizers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
