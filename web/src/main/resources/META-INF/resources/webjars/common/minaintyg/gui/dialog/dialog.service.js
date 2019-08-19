/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
 * wcDialogService - Generic dialog service
 */
angular.module('common').factory('common.dialogService',
    function($uibModal) {
        'use strict';

        function _showErrorMessageDialog(message, callback) {
            var msgbox = $uibModal.open({
                templateUrl: '/web/webjars/common/minaintyg/gui/dialog/dialogError.template.html',
                controller: function($scope, $uibModalInstance, bodyText) {
                    $scope.bodyText = bodyText;
                },
                resolve: {
                    bodyText: function() {
                        return angular.copy(message);
                    }
                }
            });

            msgbox.result.then(function(result) {
                if (callback) {
                    callback(result);
                }
            }, function() {
            });
        }

        /*
         showDialog parameters:

         scope = parent scope

         options =
         dialogId: html id attribute of dialog
         titleId: message id of title text
         bodyTextId: message id of body text
         bodyText: body text (can be used instead of or in addition to bodyTextId
         button1id: (optional) html id attribute of button 1
         button1icon: (optional) icon class for button 1
         button2id: (optional) html id attribute of button 2
         button2icon: (optional) icon class for button 2
         button3id: (optional) html id attribute of button 3
         button3icon: (optional) icon class for button 3
         button1click: (optional) function on button 1 click
         button2click: (optional) function on button 2 click
         button3click: (optional) function on button 3 click
         button1text: (optional) message id on button 1 text. default: OK
         button2text: (optional) message id on button 2 text. default: Cancel
         button3text: (optional) message id on button 3 text. default: No, don't ask
         button3visible: (optional) whether button 3 should be visible. default: true if button3text is specified, otherwise false
         autoClose: whether dialog should close on button click. If false, use .close() on return value from showDialog to close dialog later

         // jshint ignored until a larger refactoring is performed
         */
        function _showDialog(scope, options) { // jshint ignore:line

            // Apply default dialog behaviour values
            scope.dialog = {
                acceptprogressdone: true,
                focus: false,
                errormessageid: '',
                showerror: false
            };

            scope.dialog.errormessageid =
                (scope.dialog.errormessageid ? scope.dialog.errormessageid : 'common.error.unknown');

            if (options.dialogId === undefined) {
                throw 'dialogId must be specified';
            }

            // setup options defaults if parameters aren't included
            options.bodyText = (options.bodyText === undefined) ? '' : options.bodyText;
            options.button1text = (options.button1text === undefined) ? 'common.ok' : options.button1text;
            //If button1 is a default OK button, set the default ok icon unless other is specified
            options.button1icon = (options.button1icon === undefined && options.button1text === 'common.ok') ? 'icon-ok' : options.button1icon;
            options.button1tooltip = (options.button1tooltip === undefined) ? '' : options.button1tooltip;

            options.button2text = (options.button2text === undefined) ? 'common.cancel' : options.button2text;
            options.button2visible = options.button2visible === undefined ? options.button2text !== undefined :
                options.button2visible;
            //If button2 is a default cancel button, set the default x icon unless other is specified
            options.button2icon = (options.button2icon === undefined && options.button2text === 'common.cancel') ? 'icon-cancel' : options.button2icon;
            options.button2class = (options.button2class === undefined) ? 'btn-third' :  options.button2class;
            options.button3text = (options.button3text === undefined) ? undefined : options.button3text;
            options.button3visible = options.button3visible === undefined ? options.button3text !== undefined :
                options.button3visible;
            options.button1id =
                (options.button1id === undefined) ? 'button1' + options.dialogId : options.button1id;
            options.button2id =
                (options.button2id === undefined) ? 'button2' + options.dialogId : options.button2id;
            options.button3id =
                (options.button3id === undefined) ? 'button3' + options.dialogId : options.button3id;
            options.autoClose = (options.autoClose === undefined) ? true : options.autoClose;
            options.templateUrl =
                (options.templateUrl === undefined) ? '/app/partials/common-dialog.html' : options.templateUrl;
            options.model = (options.model === undefined) ? undefined : options.model;

            // Create controller to setup dialog
            var DialogInstanceCtrl = function($scope, $uibModalInstance, model, dialogId, titleId, bodyTextId, bodyText,
                button1id, button1icon, button1tooltip, button2id, button2icon, button2class, button3id, button3icon,
                button1click, button2click, button3click, button2visible, button3visible,
                button1text, button2text, button3text, autoClose) {

                $scope.model = model;
                $scope.dialogId = dialogId;
                $scope.titleId = titleId;
                $scope.bodyTextId = bodyTextId;
                $scope.bodyText = bodyText;
                $scope.button1click = function(result) {
                    button1click();
                    if (autoClose) {
                        $uibModalInstance.close(result);
                    }
                };
                $scope.button2visible = button2visible;
                if ($scope.button2visible !== undefined) {
                    $scope.button2click = function() {
                        if (button2click) {
                            button2click();
                        }
                        $uibModalInstance.dismiss('button2 dismiss');
                    };
                }
                $scope.button3visible = button3visible;
                if ($scope.button3visible !== undefined) {
                    $scope.button3click = function() {
                        if (button3click) {
                            button3click();
                        }
                        $uibModalInstance.dismiss('button3 dismiss');
                    };
                } else {
                    $scope.button3visible = false;
                }
                $scope.button1id = button1id;
                $scope.button1text = button1text;
                $scope.button1icon = button1icon;
                $scope.button1tooltip = button1tooltip;
                $scope.button2id = button2id;
                $scope.button2text = button2text;
                $scope.button2icon = button2icon;
                $scope.button2class = button2class;
                $scope.button3id = button3id;
                $scope.button3text = button3text;
                $scope.button3icon = button3icon;

            };

            // Open dialog box using specified options, template and controller
            var msgbox = $uibModal.open({
                scope: scope,
                templateUrl: options.templateUrl,
                keyboard: false,
                controller: DialogInstanceCtrl,
                resolve: {
                    model: function() {
                        return options.model;
                    },
                    dialogId: function() {
                        return angular.copy(options.dialogId);
                    },
                    titleId: function() {
                        return angular.copy(options.titleId);
                    },
                    bodyTextId: function() {
                        return angular.copy(options.bodyTextId);
                    },
                    bodyText: function() {
                        return angular.copy(options.bodyText);
                    },
                    button1id: function() {
                        return angular.copy(options.button1id);
                    },
                    button1icon: function() {
                        return angular.copy(options.button1icon);
                    },
                    button1tooltip: function() {
                        return angular.copy(options.button1tooltip);
                    },
                    button2id: function() {
                        return angular.copy(options.button2id);
                    },
                    button2icon: function() {
                        return angular.copy(options.button2icon);
                    },
                    button2class: function() {
                        return angular.copy(options.button2class);
                    },
                    button3id: function() {
                        return angular.copy(options.button3id);
                    },
                    button3icon: function() {
                        return angular.copy(options.button3icon);
                    },
                    button1click: function() {
                        return options.button1click;
                    },
                    button2click: function() {
                        return options.button2click;
                    },
                    button3click: function() {
                        return options.button3click;
                    },
                    button1text: function() {
                        return angular.copy(options.button1text);
                    },
                    button2text: function() {
                        return angular.copy(options.button2text);
                    },
                    button3text: function() {
                        return angular.copy(options.button3text);
                    },
                    button2visible: function() {
                        return angular.copy(options.button2visible);
                    },
                    button3visible: function() {
                        return angular.copy(options.button3visible);
                    },
                    autoClose: function() {
                        return angular.copy(options.autoClose);
                    }
                }
            });

            msgbox.result.then(function(result) {
                if (options.callback) {
                    options.callback(result);
                }
            }, function() {
            });

            return msgbox;
        }

        // Return public API for the service
        return {
            showErrorMessageDialog: _showErrorMessageDialog,
            showDialog: _showDialog
        };
    });
