'use strict';

describe('Controller Tests', function() {

    describe('Program Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProgram, MockKegiatan;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProgram = jasmine.createSpy('MockProgram');
            MockKegiatan = jasmine.createSpy('MockKegiatan');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Program': MockProgram,
                'Kegiatan': MockKegiatan
            };
            createController = function() {
                $injector.get('$controller')("ProgramDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'raBbvetApp:programUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
