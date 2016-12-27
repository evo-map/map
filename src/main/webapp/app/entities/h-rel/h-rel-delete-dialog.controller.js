(function() {
    'use strict';

    angular
        .module('mapApp')
        .controller('HRelDeleteController',HRelDeleteController);

    HRelDeleteController.$inject = ['$uibModalInstance', 'entity', 'HRel'];

    function HRelDeleteController($uibModalInstance, entity, HRel) {
        var vm = this;

        vm.hRel = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HRel.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
