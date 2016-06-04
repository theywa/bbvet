(function() {
    'use strict';

    angular
        .module('raBbvetApp')
        .controller('KegiatanDialogController', KegiatanDialogController);

    KegiatanDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Kegiatan', 'Program', 'Output'];

    function KegiatanDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Kegiatan, Program, Output) {
        var vm = this;

        vm.kegiatan = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.programs = Program.query();
        vm.outputs = Output.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.kegiatan.id !== null) {
                Kegiatan.update(vm.kegiatan, onSaveSuccess, onSaveError);
            } else {
                Kegiatan.save(vm.kegiatan, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('raBbvetApp:kegiatanUpdate', result);
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
