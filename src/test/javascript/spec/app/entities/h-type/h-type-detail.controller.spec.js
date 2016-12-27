'use strict';

describe('Controller Tests', function() {

    describe('HType Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockHType, MockHRel, MockHVisualizer, MockHDescriptor;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockHType = jasmine.createSpy('MockHType');
            MockHRel = jasmine.createSpy('MockHRel');
            MockHVisualizer = jasmine.createSpy('MockHVisualizer');
            MockHDescriptor = jasmine.createSpy('MockHDescriptor');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'HType': MockHType,
                'HRel': MockHRel,
                'HVisualizer': MockHVisualizer,
                'HDescriptor': MockHDescriptor
            };
            createController = function() {
                $injector.get('$controller')("HTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mapApp:hTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
