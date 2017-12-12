/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

xdescribe('wcHeader', function() {
    'use strict';

    var $scope;
    var $window;
    var element;
    var User;
    var UserModel;
    var $compile, $rootScope, $httpBackend, $controller, $templateCache;

    var testUserContext = {
        'hsaId': 'eva',
        'namn': 'Eva Holgersson',
        'lakare': true,
        'forskrivarkod': '2481632',
        'authenticationScheme': 'urn:inera:webcert:fake',
        'vardgivare': [
            {
                'id': 'vastmanland', 'namn': 'Landstinget Västmanland', 'vardenheter': [
                {
                    'id': 'centrum-vast', 'namn': 'Vårdcentrum i Väst', 'arbetsplatskod': '0000000', 'mottagningar': [
                    {'id': 'akuten', 'namn': 'Akuten', 'arbetsplatskod': '0000000'},
                    {'id': 'dialys', 'namn': 'Dialys', 'arbetsplatskod': '0000000'}
                ]
                }
            ]
            },
            {
                'id': 'ostergotland', 'namn': 'Landstinget Östergötland', 'vardenheter': [
                {
                    'id': 'linkoping',
                    'namn': 'Linköpings Universitetssjukhus',
                    'arbetsplatskod': '0000000',
                    'mottagningar': [
                        {'id': 'lkpg-akuten', 'namn': 'Akuten', 'arbetsplatskod': '0000000'},
                        {'id': 'lkpg-ogon', 'namn': 'Ögonmottagningen', 'arbetsplatskod': '0000000'}
                    ]
                }
            ]
            }
        ],
        'specialiseringar': ['Kirurgi', 'Oftalmologi'],
        'titel': 'Leg. Ögonläkare',
        'legitimeradeYrkesgrupper': ['Läkare'],
        'valdVardenhet': {
            'id': 'centrum-vast', 'namn': 'Vårdcentrum i Väst', 'arbetsplatskod': '0000000', 'mottagningar': [
                {'id': 'akuten', 'namn': 'Akuten', 'arbetsplatskod': '0000000'},
                {'id': 'dialys', 'namn': 'Dialys', 'arbetsplatskod': '0000000'}
            ]
        },
        'valdVardgivare': {
            'id': 'vastmanland', 'namn': 'Landstinget Västmanland', 'vardenheter': [
                {
                    'id': 'centrum-vast', 'namn': 'Vårdcentrum i Väst', 'arbetsplatskod': '0000000', 'mottagningar': [
                    {'id': 'akuten', 'namn': 'Akuten', 'arbetsplatskod': '0000000'},
                    {'id': 'dialys', 'namn': 'Dialys', 'arbetsplatskod': '0000000'}
                ]
                }
            ]
        },
        'authorities': { 'NAVIGERING':{}, 'ATKOMST_ANDRA_ENHETER': {} },
        'roles' : {'LAKARE': {'name':'Läkare', 'desc': 'Läkare'}},
        'features': ['hanteraFragor', 'hanteraFragor.fk7263'],
        'totaltAntalVardenheter': 6,
        'origin': 'NORMAL'
    };

    var testStatResponse = {
        'fragaSvarValdEnhet': 12,
        'fragaSvarAndraEnheter': 2,
        'intygAndraEnheter': 2,
        'intygValdEnhet': 10,
        'vardgivare': [{
            'namn': 'Landstinget Västmanland', 'id': 'vastmanland', 'vardenheter': [
                {'namn': 'Vårdcentrum i Väst', 'id': 'centrum-vast', 'fragaSvar': 11, 'intyg': 0},
                {'namn': 'Vårdcentrum i Väst - Akuten', 'id': 'akuten', 'fragaSvar': 0, 'intyg': 0},
                {'namn': 'Vårdcentrum i Väst - Dialys', 'id': 'dialys', 'fragaSvar': 1, 'intyg': 0}]
            },
            {
                'namn': 'Landstinget Östergötland', 'id': 'ostergotland', 'vardenheter': [
                {'namn': 'Linköpings Universitetssjukhus', 'id': 'linkoping', 'fragaSvar': 0, 'intyg': 0},
                {'namn': 'Linköpings Universitetssjukhus - Akuten', 'id': 'lkpg-akuten', 'fragaSvar': 0, 'intyg': 0},
                {
                    'namn': 'Linköpings Universitetssjukhus - Ögonmottagningen',
                    'id': 'lkpg-ogon',
                    'fragaSvar': 0,
                    'intyg': 0
                }]
            }
        ]
    };

    function generateHeader($scope) {
        // The header directive will call the statService and expect a response which will be used for tests
        $httpBackend.expectGET('/moduleapi/stat/').respond(200, testStatResponse);
        element = $compile($templateCache.get('/web/webjars/common/webcert/components/headers/wcHeader.partial.html'))($scope);
        $controller('common.wcHeaderController', {
            $scope: $scope,
            $element: element
        });
        $httpBackend.flush();
        $scope.$digest();
    }

    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('common', function($provide) {

        var featureService = {
            testDjupintegration: false,
            features: {
                HANTERA_FRAGOR: 'hanteraFragor',
                HANTERA_INTYGSUTKAST: 'hanteraIntygsutkast',
                FORNYA_INTYG: 'fornyaIntyg',
                MAKULERA_INTYG: 'makuleraIntyg',
                SKICKA_INTYG: 'skickaIntyg'
            },
            isFeatureActive: function(feature) {
                return true;
            }
        };
        $provide.value('common.featureService', featureService); // jasmine.createSpyObj('common.featureService',
                                                                    // ['isFeatureActive'])
    }));
    beforeEach(angular.mock.module(function($provide) {
        var testConfig = {
            PP_HOST: 'localhost:8090',
            DASHBOARD_URL: '/web/dashboard'
        };
        $provide.constant('moduleConfig', testConfig);
        $window = {location:null};
        $provide.value('$window', $window);
    }));

    beforeEach(angular.mock.inject(['$compile', '$rootScope', '$controller', '$httpBackend', '$templateCache',
        'common.User','common.UserModel',
        function(_$compile_, _$rootScope_, _$controller_, _$httpBackend_, _$templateCache_, _User_, _UserModel_) {
            $scope = _$rootScope_.$new();
            User = _User_;
            UserModel = _UserModel_;
            UserModel.setUser(testUserContext);

            $compile = _$compile_;
            $httpBackend = _$httpBackend_;
            $rootScope = _$rootScope_;
            $controller = _$controller_;
            $templateCache = _$templateCache_;

            // Instruct jasmine to let the real broadcast be called so that scope.stat will be filled by the broadcast
            // from statService
            spyOn($rootScope, '$broadcast').and.callThrough();

            generateHeader($scope);
        }])
    );

    describe('header info and links', function() {

        it('should show the webcert logo with a link to the start page', function() {
            var logoLink = element.find('#webcertLogoLink');
            var href = logoLink.attr('href');
            expect(href).toBe('/web/start');
        });

        it('should show the current date, the name of the selected vardgivare and the selected vardenhet for non private practitioner', function() {
            // TODO: reactivate (the date format is wrong under gradle)
            //var locationDateText = element.find('#wc-header-location-date');
            //var today = moment().format('YYYY-MM-DD');
            // expect(locationDateText.html()).toBe(today);

            var locationCareGiverText = element.find('#wc-header-location-care-giver');
            expect(locationCareGiverText.html()).toBe(' - ' + User.getUser().valdVardgivare.namn);

            var locationCareUnitText = element.find('#wc-header-location-care-unit');
            expect(locationCareUnitText.html()).toBe(' - ' + User.getUser().valdVardenhet.namn);
        });

        it('should show a byt vardenhet link if there are more than 1 vardenhet to choose from', function() {
            User.getUser().totaltAntalVardenheter = 6;
            $scope.$digest();
            var careUnitSelectionLink = element.find('#wc-care-unit-clinic-selector');
            expect(careUnitSelectionLink.hasClass('ng-hide')).toBe(false);
        });

        it('should show how many unhandled issues are present on other vardenheter', function() {
            var unhandledIssuesSpan = element.find('#otherLocations');
            expect(unhandledIssuesSpan.hasClass('ng-hide')).toBe(false);
            var html = unhandledIssuesSpan.html();
            expect(html).toContain('4');
        });

        it('should show name and role of the logged in user', function() {
            var role = element.find('#logged-in-role').html();
            var name = element.find('.logged-in').html();

            expect(role).toContain('Läkare');
            expect(name).toBe('Eva Holgersson');
        });
    });

    describe('header info and links (for a private practitioner)', function() {
        beforeEach(function() {
            testUserContext.roles = {'PRIVATLAKARE': {'name':'PrivatLäkare', 'desc': 'PrivatLäkare'}};
            generateHeader($scope);
        });

         it('should only show the current date and the name of the selected vardgivare for a private practitioner', function() {
            // TODO: reactivate (the date format is wrong under gradle)
            // var locationDateText = element.find('#wc-header-location-date');
            // var today = moment().format('YYYY-MM-DD');
            // expect(locationDateText.html()).toBe(today);

            var locationCareGiverText = element.find('#wc-header-location-care-giver');
            expect(locationCareGiverText.html()).toBe(' - ' + User.getUser().valdVardgivare.namn);

            var locationCareUnitText = element.find('#wc-header-location-care-unit');
            expect(locationCareUnitText.length).toBe(0);
        });

        it('should show name and role of the logged in user', function() {
            var role = element.find('#logged-in-role').html();
            var name = element.find('.logged-in').html();

            expect(role).toContain('PrivatLäkare');
            expect(name).toBe('Eva Holgersson');
        });

        it('should show link to PP', function() {
            var editLink = element.find('#editUserLink');
            expect(editLink.length).toBe(1);
        });
    });

    // Menu

    describe('info and links', function() {

        it('should show a logout button if not in djupintegration mode', function() {
            var link = element.find('#logoutLink');
            expect(link.length).toBe(1);

            $(link).click();
            expect($window.location).toBe('/logout');
        });

        it('should generate a menu with choices fit for a doctor', function() {
            var menuItems = element.find('#huvudmeny .nav LI');
            expect(menuItems.length).toBe(4);
            expect($(menuItems[0]).find('A').attr('href')).toBe('/web/dashboard#/create/index');
            expect($(menuItems[1]).find('A').attr('href')).toBe('/web/dashboard#/unhandled-qa');
            expect($(menuItems[2]).find('A').attr('href')).toBe('/web/dashboard#/unsigned');
            expect($(menuItems[3]).find('A').attr('href')).toBe('/web/dashboard#/webcert/about');
        });

        it('should generate a menu with choices fit for an administrator', function() {

            var administratorUserContext = angular.copy(testUserContext);
            administratorUserContext.roles = {
                VARDADMINISTRATOR: {name: 'VARDADMINISTRATOR', desc: 'Vårdadministratör'}
            };
            UserModel.setUser(administratorUserContext);
            generateHeader($scope);

            var menuItems = element.find('#huvudmeny .nav LI');
            expect(menuItems.length).toBe(4);
            expect($(menuItems[0]).find('A').attr('href')).toBe('/web/dashboard#/unhandled-qa');
            expect($(menuItems[1]).find('A').attr('href')).toBe('/web/dashboard#/unsigned');
            expect($(menuItems[2]).find('A').attr('href')).toBe('/web/dashboard#/create/index');
            expect($(menuItems[3]).find('A').attr('href')).toBe('/web/dashboard#/webcert/about');
        });

        it('should bubbles showing number of unhandled questions/answers and utkast on vardenhet', function() {
            var unsignedCerts = element.find('#stat-unitstat-unsigned-certs-count');
            var unhandledQs = element.find('#stat-unitstat-unhandled-question-count');

            expect(unsignedCerts.html()).toBe('10');
            expect(unhandledQs.html()).toBe('12');
        });
    });

    // Djupintegration

    describe('djupintegration gui changes', function() {

        beforeEach(function() {
            testUserContext.authorities = {};
            testUserContext.requestOrigin = 'DJUPINTEGRATION';
            generateHeader($scope);
        });

        it('should hide elements of the header if coming from a djupintegrerat journalsystem', function() {
            var hiddenElement = element.find('#huvudmeny');
            expect(hiddenElement.length).toBe(0);

            hiddenElement = element.find('#webcertLogoLink');
            expect(hiddenElement.length).toBe(0);

            hiddenElement = element.find('#logoutLink');
            expect(hiddenElement.length).toBe(0);
        });

        it('should show a link to Om Webcert if in djupintegration mode', function() {
            var about = element.find('#aboutLink');
            expect(about.length).toBe(1);
        });
    });

});
