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

/**
 * Created by marced on 2016-08-31.
 */

/**
 * arendePanelSvarKompletteringsatgard directive. Handles kompletteringsatgard choice in a modal dialog.
 * If user selects to reply with a new Intyg, this component handles that, if user choose to reply with a message, the calling component
 * get's a callback through 'onAnswerWithMessage' and must handle it there
 */
angular.module('common').directive('arendePanelSvarKompletteringsatgard',
    [ '$window', '$log', '$state', '$stateParams', '$q',
        'common.ArendeProxy', 'common.statService', 'common.ObjectHelper',
        'common.IntygCopyRequestModel', 'common.ArendeSvarModel', 'common.dialogService', 'common.ArendenViewStateService',
        function($window, $log, $state, $stateParams, $q, ArendeProxy, statService, ObjectHelper,
            IntygCopyRequestModel, ArendeSvarModel, dialogService, arendenViewStateService) {
            'use strict';

            function _hasKompletteringUtkastRelation(relations) {
                for (var a = 0; a < relations.length; a++) {
                    var relation = relations[a];
                    if (relation.kod === 'KOMPLT') {
                        return true;
                    }
                }
                return false;
            }

            return {
                restrict: 'A',
                replace: true,
                templateUrl: '/web/webjars/common/webcert/fk/arenden/komplettera/arendePanelSvarKompletteringsatgard.directive.html',
                scope: {
                    arendeListItem: '=',
                    parentViewState: '=',
                    onAnswerWithMessage: '&'
                },
                controller: function($scope, $element, $attrs) {

                    var kompletteringDialog;

                    // For readability, keep a local struct with the values used from parent scope
                    var ArendeSvar = ArendeSvarModel.build($scope.parentViewState, $scope.arendeListItem);
                    $scope.relations = arendenViewStateService.relations;

                    $scope.arendeSvar = ArendeSvar;

                    $scope.showGoToUtkastButton = _hasKompletteringUtkastRelation($scope.relations);

                    $scope.openKompletteringsUtkast = function() {

                        // Highly unlikely that there could be a komplettering without at least one relation.
                        if (typeof $scope.relations === 'undefined' || $scope.relations.length === 0) {
                            ArendeSvar.activeKompletteringErrorMessageKey = 'komplettera-no-utkast';
                            return;
                        }

                        // Iterate over relations, find the newest 'KOMPLT' one. Could be made prettier with _.js or similar.
                        var latestKomplRelation;
                        for (var a = 0; a < $scope.relations.length; a++) {
                            var relation =  $scope.relations[a];
                            if (relation.kod === 'KOMPLT') {
                                if (typeof latestKomplRelation === 'undefined') {
                                    latestKomplRelation = relation;
                                } else if (relation.date > latestKomplRelation.date) {
                                    latestKomplRelation = relation;
                                }
                            }
                        }
                        if (typeof latestKomplRelation !== 'undefined') {
                            $state.go(ArendeSvar.intygProperties.type + '-edit', {certificateId: latestKomplRelation.intygsId});
                        } else {
                            ArendeSvar.activeKompletteringErrorMessageKey = 'komplettera-no-utkast';
                        }
                    };

                   $scope.openKompletteringDialog = function() {

                       var dialogModel = {
                           arendeSvar: ArendeSvar,
                           komplUtkastFinns : _hasKompletteringUtkastRelation($scope.relations)
                       };

                        kompletteringDialog = dialogService.showDialog({
                            dialogId: 'komplettering-modal-dialog',
                            titleId: 'common.arende.komplettering.kompletteringsatgard.dialogtitle',
                            templateUrl: '/web/webjars/common/webcert/fk/arenden/komplettera/komplettering-modal-dialog.html',
                            model: dialogModel,
                            button1click: function(modalInstance) {
                                if (!ObjectHelper.isDefined(ArendeSvar.intygProperties)) {
                                    ArendeSvar.activeKompletteringErrorMessageKey = 'komplettera-no-intyg';
                                    return;
                                }

                                // The actual process of answering with a new intyg is rather complex, so defer that
                                // to calling code, and act on outcome of it here (keep dialog or close it)
                                $scope.onAnswerWithIntyg().then(function (result){
                                    //If successful, wer'e done here
                                    modalInstance.close();

                                    statService.refreshStat();

                                    function goToDraft(type, intygId) {
                                        $state.go(type + '-edit', {
                                            certificateId: intygId
                                        });
                                    }

                                    goToDraft(ArendeSvar.intygProperties.type, result.intygsUtkastId);

                                }, function (errorResult) {
                                    //Keep dialog open so that activeKompletteringErrorMessageKey is displayed to user.
                                });
                            },
                            button1id: 'button1answerintyg-dialog',
                            button2click: function(modalInstance) {
                                // Answer with text message is not handled here, so just close the
                                // dialog and notify calling code that this is the choice of the user.
                                modalInstance.close();
                                $scope.onAnswerWithMessage();
                            },
                            button2id: 'button2answermessage-dialog',
                            button3click: function(modalInstance) {
                                modalInstance.close();
                                $scope.openKompletteringsUtkast();
                            },
                            button3id:  'button3gotoutkast-dialog',
                            autoClose: false,
                            size: 'lg'
                        }).result.then(function() {
                            kompletteringDialog = null; // Dialog closed
                        }, function() {
                            kompletteringDialog = null; // Dialog dismissed
                        });

                    };


                    $scope.onAnswerWithIntyg = function() {

                        var deferred = $q.defer();

                        ArendeSvar.updateInProgress = true; // trigger local spinner
                        ArendeSvar.activeKompletteringErrorMessageKey = null;

                        ArendeProxy.answerWithIntyg($scope.arendeListItem.arende, ArendeSvar.intygProperties.type,
                            IntygCopyRequestModel.build({
                                intygId: $scope.parentViewState.intyg.id,
                                intygType: ArendeSvar.intygProperties.type,
                                patientPersonnummer: $scope.parentViewState.intyg.grundData.patient.personId,
                                nyttPatientPersonnummer: $stateParams.patientId,
                                fornamn: $stateParams.fornamn,
                                efternamn: $stateParams.efternamn,
                                mellannamn: $stateParams.mellannamn,
                                postadress: $stateParams.postadress,
                                postnummer: $stateParams.postnummer,
                                postort: $stateParams.postort
                            }), function(result) {

                                ArendeSvar.updateInProgress = false;
                                ArendeSvar.activeKompletteringErrorMessageKey = null;
                                deferred.resolve(result);


                            }, function(errorData) {
                                // show error view
                                ArendeSvar.updateInProgress = false;
                                ArendeSvar.activeKompletteringErrorMessageKey = errorData.errorCode;
                                deferred.reject(errorData);
                            });
                        return deferred.promise;
                    };




                }
            };
        }]);
