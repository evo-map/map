(function() {
    'use strict';
    angular
        .module('mapApp')
        .factory('HType', HType);

    HType.$inject = ['$resource'];

    function HType ($resource) {
        var resourceUrl =  'api/h-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
