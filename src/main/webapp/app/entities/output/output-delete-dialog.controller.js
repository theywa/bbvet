(function() {
    'use strict';

    angular
        .module('raBbvetApp')
        .controller('OutputDeleteController',OutputDeleteController);

    OutputDeleteController.$inject = ['$uibModalInstance', 'entity', 'Output'];

    function OutputDeleteController($uibModalInstance, entity, Output) {
        var vm = this;

        vm.output = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Output.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
