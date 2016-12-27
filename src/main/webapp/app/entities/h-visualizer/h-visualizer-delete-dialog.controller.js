(function() {
    'use strict';

    angular
        .module('mapApp')
        .controller('HVisualizerDeleteController',HVisualizerDeleteController);

    HVisualizerDeleteController.$inject = ['$uibModalInstance', 'entity', 'HVisualizer'];

    function HVisualizerDeleteController($uibModalInstance, entity, HVisualizer) {
        var vm = this;

        vm.hVisualizer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HVisualizer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
