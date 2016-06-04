'use strict';

describe('Controller Tests', function() {

    describe('Suboutput Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSuboutput, MockOutput, MockKomponen;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSuboutput = jasmine.createSpy('MockSuboutput');
            MockOutput = jasmine.createSpy('MockOutput');
            MockKomponen = jasmine.createSpy('MockKomponen');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Suboutput': MockSuboutput,
                'Output': MockOutput,
                'Komponen': MockKomponen
            };
            createController = function() {
                $injector.get('$controller')("SuboutputDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'raBbvetApp:suboutputUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
