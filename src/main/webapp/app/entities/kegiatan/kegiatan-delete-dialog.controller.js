(function() {
    'use strict';

    angular
        .module('raBbvetApp')
        .controller('KegiatanDeleteController',KegiatanDeleteController);

    KegiatanDeleteController.$inject = ['$uibModalInstance', 'entity', 'Kegiatan'];

    function KegiatanDeleteController($uibModalInstance, entity, Kegiatan) {
        var vm = this;

        vm.kegiatan = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Kegiatan.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
