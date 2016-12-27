(function() {
    'use strict';

    angular
        .module('mapApp')
        .controller('HTypeDialogController', HTypeDialogController);

    HTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HType', 'HRel', 'HVisualizer', 'HDescriptor'];

    function HTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HType, HRel, HVisualizer, HDescriptor) {
        var vm = this;

        vm.hType = entity;
        vm.clear = clear;
        vm.save = save;
        vm.hrels = HRel.query();
        vm.hvisualizers = HVisualizer.query();
        vm.hdescriptors = HDescriptor.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.hType.id !== null) {
                HType.update(vm.hType, onSaveSuccess, onSaveError);
            } else {
                HType.save(vm.hType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mapApp:hTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
