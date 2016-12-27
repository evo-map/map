'use strict';

describe('Controller Tests', function() {

    describe('HRel Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockHRel, MockHType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockHRel = jasmine.createSpy('MockHRel');
            MockHType = jasmine.createSpy('MockHType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'HRel': MockHRel,
                'HType': MockHType
            };
            createController = function() {
                $injector.get('$controller')("HRelDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mapApp:hRelUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
