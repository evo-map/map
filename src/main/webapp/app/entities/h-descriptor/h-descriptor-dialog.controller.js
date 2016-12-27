(function() {
    'use strict';

    angular
        .module('mapApp')
        .controller('HDescriptorDialogController', HDescriptorDialogController);

    HDescriptorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HDescriptor', 'HType'];

    function HDescriptorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HDescriptor, HType) {
        var vm = this;

        vm.hDescriptor = entity;
        vm.clear = clear;
        vm.save = save;
        vm.htypes = HType.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.hDescriptor.id !== null) {
                HDescriptor.update(vm.hDescriptor, onSaveSuccess, onSaveError);
            } else {
                HDescriptor.save(vm.hDescriptor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mapApp:hDescriptorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
