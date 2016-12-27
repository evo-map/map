(function() {
    'use strict';

    angular
        .module('mapApp')
        .controller('HRelDialogController', HRelDialogController);

    HRelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HRel', 'HType'];

    function HRelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HRel, HType) {
        var vm = this;

        vm.hRel = entity;
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
            if (vm.hRel.id !== null) {
                HRel.update(vm.hRel, onSaveSuccess, onSaveError);
            } else {
                HRel.save(vm.hRel, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mapApp:hRelUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
