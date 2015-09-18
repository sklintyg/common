describe("wcFmbHelpDisplay", function () {
    'use strict';

    beforeEach(angular.mock.module('common'));

    var element, outerScope, innerScope;

    beforeEach(inject(function ($rootScope, $compile) {
        element = angular.element('<wc-fmb-help-display diagnosis-code="fmb.diagnosKod" diagnosis-description="fmb.diagnosBeskrivning" help-text-contents="fmb.formData.FORM"></wc-fmb-help-display>');
        outerScope = $rootScope;
        $compile(element)(outerScope);

        outerScope.$digest(); //digest the outerscope before the innerScope is called

        innerScope = element.isolateScope(); //This will get the isolate scope
    }));

    function setUpFMBData(data) {
        outerScope.$apply(function () {
            outerScope.fmb = data;
        });
    }

    describe('correct headlines', function () {
        beforeEach(function() {
            setUpFMBData({
                formData: [],
                diagnosKod: 'J22',
                diagnosBeskrivning: 'En slags diagnos'
            });
        });

        it('should have a headline set', function() {
            //Expects
            expect(element.find('h2').text()).toEqual('J22 - En slags diagnos');
        });
    });

    /*
    describe('template produces the right html', function () {
        //beforeEach(function() {
        //    setUpExchangeableViews(basicSetOfExchangeableViews);
        //});

        it('anchors have the right href, class and description', function () {
            //Here I used jquery to find classes and what not since jqLite is really limited (ng 1.2)
            //Be sure to include jquery before angular in your karma config

            //Find the one that is supposed to be active
            expect(element.find('.active').attr('href')).toEqual(basicSetOfExchangeableViews[0].state);
            expect(element.find('.active').text().trim()).toEqual(basicSetOfExchangeableViews[0].description);

            //Find the other one that is not active
            expect(element.find('a').not('.active').attr('href')).toEqual(basicSetOfExchangeableViews[1].state);
            expect(element.find('a').not('.active').text().trim()).toEqual(basicSetOfExchangeableViews[1].description);
        });

    });
    */
});

