angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'sjukskrivningar',
        templateUrl: '/web/webjars/common/webcert/gui/formly/sjukskrivningar.formly.html',
        controller: ['$scope', 'common.ArendeListViewStateService', 'common.SjukskrivningarViewStateService',
            'common.UtkastValidationService', 'common.messageService',
            function($scope, ArendeListViewState, viewstate, UtkastValidationService, messageService) {

                $scope.$watch('formState.viewState.common.validation.messagesByField', function() {
                    $scope.validationsForPeriod = {};
                    $scope.overlapValidations = [];

                    if (!$scope.formState.viewState.common.validation.messagesByField) {
                        return;
                    }

                    angular.forEach($scope.to.fields, function(field) {

                        var key = $scope.options.key + '.period.' + field.toLowerCase();

                        var fromValidations = $scope.formState.viewState.common.validation.messagesByField[key + '.from'];
                        var tomValidations = $scope.formState.viewState.common.validation.messagesByField[key + '.tom'];
                        var periodValidations = $scope.formState.viewState.common.validation.messagesByField[key];

                        $scope.validationsForPeriod[field] = [];
                        if (fromValidations) {
                            $scope.validationsForPeriod[field] = $scope.validationsForPeriod[field].concat(fromValidations);
                        }
                        if (tomValidations) {
                            $scope.validationsForPeriod[field] = $scope.validationsForPeriod[field].concat(tomValidations);
                        }
                        if (periodValidations) {
                            $scope.validationsForPeriod[field] = $scope.validationsForPeriod[field].concat(periodValidations);
                        }

                        // The validation message for PERIOD_OVERLAP should only be displayed once even if several periods overlaps
                        angular.forEach($scope.validationsForPeriod[field], function(validation) {
                            if (validation.type === 'PERIOD_OVERLAP' && $scope.overlapValidations.length === 0) {
                                $scope.overlapValidations.push(validation);
                            }
                        });

                        // The validation message for PERIOD_OVERLAP should not be displayed at each period
                        function noPeriodOverlaps(validation) {
                            return validation.type !== 'PERIOD_OVERLAP';
                        }

                        $scope.validationsForPeriod[field] = $scope.validationsForPeriod[field].filter(noPeriodOverlaps);
                    });
                });

                $scope.hasValidationError = function(field, type) {
                    var fieldKey = $scope.options.key + '.period.' + field;
                    var typeKey = fieldKey + '.' + type;
                    return $scope.formState.viewState.common.validation.messagesByField &&
                        (!!$scope.formState.viewState.common.validation.messagesByField[fieldKey.toLowerCase()] ||
                            !!$scope.formState.viewState.common.validation.messagesByField[typeKey.toLowerCase()]);
                };

                $scope.hasKompletteringar = function() {
                    return ArendeListViewState.hasKompletteringar($scope.options.key);
                };
                $scope.validate = function() {
                    $scope.form.$commitViewValue();
                    UtkastValidationService.validate($scope.model);
                };
                $scope.viewstate = viewstate.reset();

                function setup() {
                    viewstate.setModel($scope.model[$scope.options.key]);
                    viewstate.updatePeriods();

                    if ($scope.model.grundData.relation.sistaGiltighetsDatum) {
                        $scope.lastEffectiveDateNoticeText = messageService
                            .getProperty('lisjp.help.sjukskrivningar.sista-giltighets-datum')
                            .replace('{{lastEffectiveDate}}', $scope.model.grundData.relation.sistaGiltighetsDatum)
                            .replace('{{sjukskrivningsgrad}}', $scope.model.grundData.relation.sistaSjukskrivningsgrad);
                    }
                }

                setup();
                $scope.$on('intyg.loaded', function() {
                    setup();
                });

                $scope.$watch('model.' + $scope.options.key, function(newValue, oldValue) {
                    viewstate.updatePeriods();
                }, true);
            }
        ]
    });

});
