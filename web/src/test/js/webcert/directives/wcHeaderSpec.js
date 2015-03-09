describe('wcHeader', function() {
    'use strict';

    var $scope;
    var element;
    var User;
    var statService;
    var featureService;
    var $compile, $rootScope, $httpBackend;

    var testUserContext = {'hsaId':'eva','namn':'Eva Holgersson','lakare':true,'forskrivarkod':'2481632','authenticationScheme':'urn:inera:webcert:fake','vardgivare':[
        {'id':'vastmanland','namn':'Landstinget Västmanland','vardenheter':[
            {'id':'centrum-vast','namn':'Vårdcentrum i Väst','arbetsplatskod':'0000000','mottagningar':[
                {'id':'akuten','namn':'Akuten','arbetsplatskod':'0000000'},{'id':'dialys','namn':'Dialys','arbetsplatskod':'0000000'}
            ]}
        ]
        },
        {'id':'ostergotland','namn':'Landstinget Östergötland','vardenheter':[
            {'id':'linkoping','namn':'Linköpings Universitetssjukhus','arbetsplatskod':'0000000','mottagningar':[
                {'id':'lkpg-akuten','namn':'Akuten','arbetsplatskod':'0000000'},
                {'id':'lkpg-ogon','namn':'Ögonmottagningen','arbetsplatskod':'0000000'}
            ]}
        ]
        }
    ],
        'specialiseringar':['Kirurgi','Oftalmologi'],
        'titel':'Leg. Ögonläkare',
        'legitimeradeYrkesgrupper':['Läkare'],
        'valdVardenhet':{'id':'centrum-vast','namn':'Vårdcentrum i Väst','arbetsplatskod':'0000000','mottagningar':[
            {'id':'akuten','namn':'Akuten','arbetsplatskod':'0000000'},
            {'id':'dialys','namn':'Dialys','arbetsplatskod':'0000000'}
        ]
        },
        'valdVardgivare':{'id':'vastmanland','namn':'Landstinget Västmanland','vardenheter':[
            {'id':'centrum-vast','namn':'Vårdcentrum i Väst','arbetsplatskod':'0000000','mottagningar':[
                {'id':'akuten','namn':'Akuten','arbetsplatskod':'0000000'},
                {'id':'dialys','namn':'Dialys','arbetsplatskod':'0000000'}
            ]
            }
        ]
        },
        'aktivaFunktioner':['hanteraFragor','hanteraFragor.fk7263'],
        'totaltAntalVardenheter':6
    };

    var testStatResponse = {
        'fragaSvarValdEnhet':12,
        'fragaSvarAndraEnheter':2,
        'intygAndraEnheter':2,
        'intygValdEnhet':10,
        'vardgivare':[{'namn':'Landstinget Västmanland','id':'vastmanland','vardenheter':[
            {'namn':'Vårdcentrum i Väst','id':'centrum-vast','fragaSvar':11,'intyg':0},
            {'namn':'Vårdcentrum i Väst - Akuten','id':'akuten','fragaSvar':0,'intyg':0},
            {'namn':'Vårdcentrum i Väst - Dialys','id':'dialys','fragaSvar':1,'intyg':0}]},
            {'namn':'Landstinget Östergötland','id':'ostergotland','vardenheter':[
                {'namn':'Linköpings Universitetssjukhus','id':'linkoping','fragaSvar':0,'intyg':0},
                {'namn':'Linköpings Universitetssjukhus - Akuten','id':'lkpg-akuten','fragaSvar':0,'intyg':0},
                {'namn':'Linköpings Universitetssjukhus - Ögonmottagningen','id':'lkpg-ogon','fragaSvar':0,'intyg':0}]}
        ]};

    function generateDirective($compile, $rootScope, $httpBackend) {
        // The header directive will call the statService and expect a response which will be used for tests
        $httpBackend.expectGET('/moduleapi/stat/').respond(200, testStatResponse);
        element = angular.element('<div id="wcHeader" wc-header></div>');
        $compile(element)($scope);
        $scope.$digest();
        $httpBackend.flush();
    }

    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('common.User', { userContext: testUserContext });

        var featureService = {
            testDjupintegration: false,
            features: {
                HANTERA_FRAGOR: 'hanteraFragor',
                HANTERA_INTYGSUTKAST: 'hanteraIntygsutkast',
                KOPIERA_INTYG: 'kopieraIntyg',
                MAKULERA_INTYG: 'makuleraIntyg',
                SKICKA_INTYG: 'skickaIntyg',
                FRAN_JOURNALSYSTEM: 'franJournalsystem'
            },
            isFeatureActive: function(feature) {
                if (this.testDjupintegration) {
                    return true;
                } else if (feature !== this.features.FRAN_JOURNALSYSTEM) {
                    return true;
                }

                return false;
            }
        };
        $provide.value('common.featureService', featureService); //jasmine.createSpyObj('common.featureService', ['isFeatureActive'])
    }));
    beforeEach(angular.mock.inject(['$compile', '$rootScope', '$httpBackend', 'common.User', 'common.statService', 'common.featureService',
        function(_$compile_, _$rootScope_, _$httpBackend_, _User_, _statService_, _featureService_) {
        $scope = _$rootScope_.$new();
        User = _User_;
        statService = _statService_;
        featureService = _featureService_;
        $compile = _$compile_;
        $rootScope = _$rootScope_;
        $httpBackend = _$httpBackend_;

        // Instruct jasmine to let the real broadcast be called so that scope.stat will be filled by the broadcast from statService
        spyOn($rootScope, '$broadcast').and.callThrough();
        generateDirective($compile, $rootScope, $httpBackend);
    }]));

    describe('header info and links', function() {

        it('should show the webcert logo with a link to the start page', function() {
            var logoLink = element.find('#webcertLogoLink');
            var href = logoLink.attr('href');
            expect(href).toBe('/web/start');
        });

        it('should show the current date, the name of the selected vardgivare and the selected vardenhet', function() {
            var locationText = element.find('#location');
            var today = moment().format('YYYY-MM-DD');
            expect(locationText.html()).toBe(today + ' - ' + User.userContext.valdVardgivare.namn + ' - ' + User.userContext.valdVardenhet.namn);
        });

        it('should show a byt vardenhet link if there are more than 1 vardenhet to choose from', function() {
            User.userContext.totaltAntalVardenheter = 6;
            $scope.$digest();
            var careUnitSelectionLink = element.find('#wc-care-unit-clinic-selector');
            expect(careUnitSelectionLink.hasClass('ng-hide')).toBe(false);
        });

        it('should show how many unhandled issues are present on other vardenheter', function() {
            var unhandledIssuesSpan = element.find('#otherLocations');
            expect(unhandledIssuesSpan.hasClass('ng-hide')).toBe(false);
            expect(unhandledIssuesSpan.html()).toContain('4');
        });

        it('should show name and role of the logged in user', function() {
            var role = element.find('#logged-in-role').html();
            var name = element.find('.logged-in').html();

            expect(role).toContain('Läkare');
            expect(name).toBe('Eva Holgersson');
        });
    });

    // Menu

    describe('info and links', function() {

        it('should show a logout button if not in djupintegration mode', function() {
            var link = element.find('#logoutLink');
            expect(link.length).toBe(1);
        });
/*
        xit('should generate a menu with choices fit for a doctor', function() {
        });

        xit('should generate a menu with choices fit for an administrator', function() {
        });
*/
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
            featureService.testDjupintegration = true;
            generateDirective($compile, $rootScope, $httpBackend);
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
