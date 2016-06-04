(function() {
    'use strict';
    angular
        .module('raBbvetApp')
        .factory('Suboutput', Suboutput);

    Suboutput.$inject = ['$resource', 'DateUtils'];

    function Suboutput ($resource, DateUtils) {
        var resourceUrl =  'api/suboutputs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                        data.updatedDate = DateUtils.convertDateTimeFromServer(data.updatedDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
