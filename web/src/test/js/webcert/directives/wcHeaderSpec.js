describe('wcHeader', function() {
    'use strict';

    var $scope;
    var element;
    var User;

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


    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('common.User', { userContext: testUserContext });

        var featureService = {
            features: {
                HANTERA_FRAGOR: 'hanteraFragor',
                HANTERA_INTYGSUTKAST: 'hanteraIntygsutkast',
                KOPIERA_INTYG: 'kopieraIntyg',
                MAKULERA_INTYG: 'makuleraIntyg',
                SKICKA_INTYG: 'skickaIntyg',
                FRAN_JOURNALSYSTEM: 'franJournalsystem'
            },
            isFeatureActive: function() {}
        };
        $provide.value('common.featureService', featureService); //jasmine.createSpyObj('common.featureService', ['isFeatureActive'])
    }));
    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.inject(['$compile', '$rootScope', '$httpBackend', 'common.User',
        function($compile, $rootScope, $httpBackend, _User_) {
        $scope = $rootScope.$new();
        User = _User_;

        $httpBackend.expectGET('/moduleapi/stat/').respond(200, {
            'fragaSvarValdEnhet':12,
            'fragaSvarAndraEnheter':0,
            'intygAndraEnheter':0,
            'intygValdEnhet':0,
            'vardgivare':[{'namn':'Landstinget Västmanland','id':'vastmanland','vardenheter':[
                {'namn':'Vårdcentrum i Väst','id':'centrum-vast','fragaSvar':11,'intyg':0},
                {'namn':'Vårdcentrum i Väst - Akuten','id':'akuten','fragaSvar':0,'intyg':0},
                {'namn':'Vårdcentrum i Väst - Dialys','id':'dialys','fragaSvar':1,'intyg':0}]},
                {'namn':'Landstinget Östergötland','id':'ostergotland','vardenheter':[
                    {'namn':'Linköpings Universitetssjukhus','id':'linkoping','fragaSvar':0,'intyg':0},
                    {'namn':'Linköpings Universitetssjukhus - Akuten','id':'lkpg-akuten','fragaSvar':0,'intyg':0},
                    {'namn':'Linköpings Universitetssjukhus - Ögonmottagningen','id':'lkpg-ogon','fragaSvar':0,'intyg':0}]}
            ]});
        element = angular.element('<div id="wcHeader" wc-header></div>');
        $compile(element)($scope);
        $scope.$digest();
        $httpBackend.flush();
    }]));

    // Info and links

    describe('info and links', function() {

        beforeEach(function() {
        });

        //1
        it('should show the webcert logo with a link to the start page', function() {
            var logoLink = element.find('#webcertLogoLink');
            var href = logoLink.attr('href');
            expect(href).toBe('/web/start');
        });

        //2
        it('should show the current date, the name of the selected vardgivare and the selected vardenhet', function() {
            var locationText = element.find('#location');
            var today = moment().format('YYYY-MM-DD');
            expect(locationText.html()).toBe(today + ' - ' + User.userContext.valdVardgivare.namn + ' - ' + User.userContext.valdVardenhet.namn);
        });

        //3
        xit('should show a byt vardenhet link if there are more than 1 vardenhet to choose from', function() {
        });

        //4
        xit('should show how many unhandled issues are present on other vardenheter', function() {
        });

        //2
        xit('should show name and role of the logged in user', function() {

        });
    });

    // Menu

    describe('info and links', function() {
        //2
        xit('should generate a menu with choices fit for a doctor', function() {
        });

        //2
        xit('should generate a menu with choices fit for an administrator', function() {
        });

        //2
        xit('should bubbles showing number of unhandled questions/answers and utkast on vardenhet', function() {
        });
    });

    // Djupintegration

    describe('info and links', function() {
        //3
        xit('should hide elements of the header if coming from a djupintegrerat journalsystem', function() {
        });

        //3
        xit('should show a logout button if not in djupintegration mode', function() {
        });

        //3
        xit('should show a link to Om Webcert if in djupintegration mode', function() {
        });
    });

    // Negatives

});
