(function() {
    'use strict';

    angular
        .module('raBbvetApp')
        .controller('SuboutputDialogController', SuboutputDialogController);

    SuboutputDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Suboutput', 'Output', 'Komponen'];

    function SuboutputDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Suboutput, Output, Komponen) {
        var vm = this;

        vm.suboutput = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.outputs = Output.query();
        vm.komponens = Komponen.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.suboutput.id !== null) {
                Suboutput.update(vm.suboutput, onSaveSuccess, onSaveError);
            } else {
                Suboutput.save(vm.suboutput, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('raBbvetApp:suboutputUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
