(function() {
    'use strict';

    angular
        .module('mapApp')
        .controller('HVisualizerDialogController', HVisualizerDialogController);

    HVisualizerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HVisualizer', 'HType'];

    function HVisualizerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HVisualizer, HType) {
        var vm = this;

        vm.hVisualizer = entity;
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
            if (vm.hVisualizer.id !== null) {
                HVisualizer.update(vm.hVisualizer, onSaveSuccess, onSaveError);
            } else {
                HVisualizer.save(vm.hVisualizer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mapApp:hVisualizerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
