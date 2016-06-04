(function() {
    'use strict';

    angular
        .module('raBbvetApp')
        .controller('ProgramController', ProgramController);

    ProgramController.$inject = ['$scope', '$state', 'Program'];

    function ProgramController ($scope, $state, Program) {
        var vm = this;
        
        vm.programs = [];

        loadAll();

        function loadAll() {
            Program.query(function(result) {
                vm.programs = result;
            });
        }
    }
})();
