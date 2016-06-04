(function() {
    'use strict';

    angular
        .module('raBbvetApp')
        .controller('SuboutputController', SuboutputController);

    SuboutputController.$inject = ['$scope', '$state', 'Suboutput'];

    function SuboutputController ($scope, $state, Suboutput) {
        var vm = this;
        
        vm.suboutputs = [];

        loadAll();

        function loadAll() {
            Suboutput.query(function(result) {
                vm.suboutputs = result;
            });
        }
    }
})();
