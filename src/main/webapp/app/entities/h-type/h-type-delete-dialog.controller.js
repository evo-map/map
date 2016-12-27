(function() {
    'use strict';

    angular
        .module('mapApp')
        .controller('HTypeDeleteController',HTypeDeleteController);

    HTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'HType'];

    function HTypeDeleteController($uibModalInstance, entity, HType) {
        var vm = this;

        vm.hType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
