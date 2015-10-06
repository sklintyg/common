/**
 * Display FMB help texts
 */
angular.module('common').directive('wcFmbHelpDisplay',
        function() {
            'use strict';

            return {
                restrict: 'E',
                scope: {
                    helpTextContents: '=',
                    diagnosisDescription: '=',
                    diagnosisCode: '=',
                    relatedFormId: '@'
                },
                link: function(scope, element, attrs) {

                    //Some status we need to have on the accordion
                    scope.status = {
                        open: true
                    };
                },
                templateUrl: '/web/webjars/common/webcert/fmb/wcFmbHelpDisplay.directive.html'
            };
        });
