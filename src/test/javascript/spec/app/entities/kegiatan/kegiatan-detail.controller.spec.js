'use strict';

describe('Controller Tests', function() {

    describe('Kegiatan Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockKegiatan, MockProgram, MockOutput;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockKegiatan = jasmine.createSpy('MockKegiatan');
            MockProgram = jasmine.createSpy('MockProgram');
            MockOutput = jasmine.createSpy('MockOutput');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Kegiatan': MockKegiatan,
                'Program': MockProgram,
                'Output': MockOutput
            };
            createController = function() {
                $injector.get('$controller')("KegiatanDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'raBbvetApp:kegiatanUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
