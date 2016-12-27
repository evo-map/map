(function() {
    'use strict';

    angular
        .module('mapApp')
        .controller('HDescriptorDeleteController',HDescriptorDeleteController);

    HDescriptorDeleteController.$inject = ['$uibModalInstance', 'entity', 'HDescriptor'];

    function HDescriptorDeleteController($uibModalInstance, entity, HDescriptor) {
        var vm = this;

        vm.hDescriptor = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HDescriptor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
