(function() {
    'use strict';

    angular
        .module('raBbvetApp')
        .controller('KegiatanController', KegiatanController);

    KegiatanController.$inject = ['$scope', '$state', 'Kegiatan'];

    function KegiatanController ($scope, $state, Kegiatan) {
        var vm = this;
        
        vm.kegiatans = [];

        loadAll();

        function loadAll() {
            Kegiatan.query(function(result) {
                vm.kegiatans = result;
            });
        }
    }
})();
