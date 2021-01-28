/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcVardPersonSekretess',
        [ '$uibModal', 'common.UserModel', function($uibModal, UserModel) {
            'use strict';

            return {
                restrict: 'E',
                scope: {},
                template: '',

                link: function() {

                    var confirmDialogInstance, _SEKRETESSWARNING_APPROVED = 'wc.vardperson.sekretess.approved';

                    //The confirmation dialog is only displayed if sekretess and not already given consent.
                    var _showSekretessConfirmationDialog = function() {
                        confirmDialogInstance = $uibModal.open({
                            templateUrl: '/web/webjars/common/webcert/components/headers/wcAppHeader/wcHeaderUser/wcVardPersonSekretess/wcVardPersonSekretessDialog.html',
                            controller: 'wcVardPersonSekretessDialogCtrl',
                            size: 'md',
                            id: 'SekretessConsentDialog',
                            backdrop: 'static',
                            keyboard: false,
                            resolve: {
                                preferenceKey: function() {
                                    return _SEKRETESSWARNING_APPROVED;
                                }
                            }
                        });
                    };



                    //Should we launch the approval dialog?
                    if (UserModel.user.sekretessMarkerad && UserModel.getAnvandarPreference(_SEKRETESSWARNING_APPROVED) === undefined) {
                        _showSekretessConfirmationDialog();

                    }

                }

            };
        } ]);
