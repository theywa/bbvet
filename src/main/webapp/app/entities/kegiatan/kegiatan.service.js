(function() {
    'use strict';
    angular
        .module('raBbvetApp')
        .factory('Kegiatan', Kegiatan);

    Kegiatan.$inject = ['$resource', 'DateUtils'];

    function Kegiatan ($resource, DateUtils) {
        var resourceUrl =  'api/kegiatans/:id';

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
