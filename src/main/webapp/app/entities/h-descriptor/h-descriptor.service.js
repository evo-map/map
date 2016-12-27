(function() {
    'use strict';
    angular
        .module('mapApp')
        .factory('HDescriptor', HDescriptor);

    HDescriptor.$inject = ['$resource'];

    function HDescriptor ($resource) {
        var resourceUrl =  'api/h-descriptors/:id';

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
