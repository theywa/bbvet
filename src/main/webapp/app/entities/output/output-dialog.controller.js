(function() {
    'use strict';

    angular
        .module('raBbvetApp')
        .controller('OutputDialogController', OutputDialogController);

    OutputDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Output', 'Kegiatan', 'Komponen', 'Suboutput'];

    function OutputDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Output, Kegiatan, Komponen, Suboutput) {
        var vm = this;

        vm.output = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.kegiatans = Kegiatan.query();
        vm.komponens = Komponen.query();
        vm.suboutputs = Suboutput.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.output.id !== null) {
                Output.update(vm.output, onSaveSuccess, onSaveError);
            } else {
                Output.save(vm.output, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('raBbvetApp:outputUpdate', result);
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
