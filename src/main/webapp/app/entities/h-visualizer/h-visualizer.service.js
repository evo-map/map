(function() {
    'use strict';
    angular
        .module('mapApp')
        .factory('HVisualizer', HVisualizer);

    HVisualizer.$inject = ['$resource'];

    function HVisualizer ($resource) {
        var resourceUrl =  'api/h-visualizers/:id';

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
