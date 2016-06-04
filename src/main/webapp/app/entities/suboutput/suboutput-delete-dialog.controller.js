(function() {
    'use strict';

    angular
        .module('raBbvetApp')
        .controller('SuboutputDeleteController',SuboutputDeleteController);

    SuboutputDeleteController.$inject = ['$uibModalInstance', 'entity', 'Suboutput'];

    function SuboutputDeleteController($uibModalInstance, entity, Suboutput) {
        var vm = this;

        vm.suboutput = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Suboutput.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
