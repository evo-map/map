(function() {
    'use strict';
    angular
        .module('mapApp')
        .factory('HRel', HRel);

    HRel.$inject = ['$resource'];

    function HRel ($resource) {
        var resourceUrl =  'api/h-rels/:id';

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
