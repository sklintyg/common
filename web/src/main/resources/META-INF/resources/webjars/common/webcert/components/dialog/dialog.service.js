/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
    function($uibModal, $timeout, $window, $rootScope, $stateParams) {
        'use strict';

        function _showErrorMessageDialog(message, callback, customTitleId) {

            if (!angular.isDefined(customTitleId)) {
                customTitleId = 'common.modal.title.error';
            }
            var msgbox = $uibModal.open({
                templateUrl: '/web/webjars/common/webcert/components/dialog/dialogError.template.html',
                controller: function($scope, $uibModalInstance, bodyText, customTitleId, certificateId) {
                    $scope.bodyText = bodyText;
                    $scope.customTitleId = customTitleId;
                    $scope.certificateId = certificateId;
                },
                resolve: {
                    bodyText: function() {
                        return angular.copy(message);
                    },
                    customTitleId: function() {
                        return angular.copy(customTitleId);
                    },
                    certificateId: function() {
                        return $stateParams.certificateId;
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

        function _showMessageDialog(titleId, bodyText, callback) {
            var msgbox = $uibModal.open({
                templateUrl: '/web/webjars/common/webcert/components/dialog/dialogMessage.template.html',
                controller: function($scope, $uibModalInstance, bodyText, titleId) {
                    $scope.bodyText = bodyText;
                    $scope.titleId = titleId;
                },
                resolve: {
                    bodyText: function() {
                        return angular.copy(bodyText);
                    },
                    titleId: function() {
                        return angular.copy(titleId);
                    }
                }
            });

            msgbox.result['finally'](function(result) {
                if (callback) {
                    callback(result);
                }
            });
        }

        function _showMessageDialogText(dialogId, title, bodyText, callback) {
            var msgbox = $uibModal.open({
                templateUrl: '/web/webjars/common/webcert/components/dialog/dialogMessage.template.html',
                controller: function($scope, $uibModalInstance, dialogId, bodyText, title) {
                    $scope.dialogId = dialogId;
                    $scope.title = title;
                    $scope.bodyText = bodyText;
                },
                resolve: {
                    dialogId: function() {
                        return angular.copy(dialogId);
                    },
                    title: function() {
                        return angular.copy(title);
                    },
                    bodyText: function() {
                        return angular.copy(bodyText);
                    }
                }
            });

            msgbox.result['finally'](function(result) {
                if (callback) {
                    callback(result);
                }
            });
        }

        /*
         showDialog parameters:

         options =
         dialogId: html id attribute of dialog
         titleId: message id of title text
         titleText: title text
         bodyTextId: message id of body text
         bodyText: body text (can be used instead of or in addition to bodyTextId
         button1id: (optional) html id attribute of button 1
         button2id: (optional) html id attribute of button 2
         button3id: (optional) html id attribute of button 3
         button1click: (optional) function on button 1 click
         button2click: (optional) function on button 2 click
         button3click: (optional) function on button 3 click
         button1text: (optional) message id on button 1 text. default: OK
         button2text: (optional) message id on button 2 text. default: Cancel
         button3text: (optional) message id on button 3 text. default: No, don't ask
         button2visible: (optional) whether button 2 should be visible. default: true if button2text is specified, otherwise false
         button3visible: (optional) whether button 3 should be visible. default: true if button3text is specified, otherwise false
         autoClose: whether dialog should close on button click. If false, use .close() on return value from showDialog to close dialog later
         */
        function _showDialog( options ) {
            var dialogOptions, msgbox;

            if (options.dialogId === undefined) {
                throw 'dialogId must be specified';
            }

            setOptionDefaults(options);
            dialogOptions = createDialogOptions(options);

            // Open dialog box using specified options, template and controller
            msgbox = $uibModal.open( dialogOptions );

            msgbox.result.then(function(result) {
                if (options.callback) {
                    options.callback(result);
                } else if(result && result.resolve !== undefined){
                    result.resolve();
                } else if(result && result.direct) {
                    result.direct();
                } else if(result && typeof result === 'function'){
                    result();
                }
            }, function() {
            });

            msgbox.model = options.model;

            return msgbox;
        }

        var DialogInstanceCtrl = function($scope, $uibModalInstance, model, dialogId, titleId, titleText, bodyTextId,
                    bodyText,
                    button1id, button2id, button3id, button1click, button2click, button3click,
                    button2visible, button3visible,
                    button1text,
                    button2text, button3text, autoClose, title) {

                    $scope.model = model;
                    $scope.dialogId = dialogId;
                    $scope.title = title;
                    $scope.titleText = titleText;
                    $scope.titleId = titleId;
                    $scope.bodyTextId = bodyTextId;
                    $scope.bodyText = bodyText;
                    $scope.button1click = function(result) {
                        button1click($uibModalInstance);
                        if(autoClose) {
                            $uibModalInstance.close(result);
                        }
                    };
                    $scope.button2visible = button2visible;
                    if ($scope.button2visible !== undefined) {
                        $scope.button2click = function() {
                            if (button2click) {
                                button2click($uibModalInstance);
                            }
                            if(autoClose) {
                                $uibModalInstance.dismiss('button2 dismiss');
                            }
                        };
                    } else {
                        $scope.button2visible = false;
                    }
                    $scope.button3visible = button3visible;
                    if ($scope.button3visible !== undefined) {
                        $scope.button3click = function() {
                            if (button3click) {
                                button3click($uibModalInstance);
                            }
                            $uibModalInstance.dismiss('button3 dismiss');
                        };
                    } else {
                        $scope.button3visible = false;
                    }
                    $scope.button1text = button1text;
                    $scope.button2text = button2text;
                    $scope.button3text = button3text;
                    $scope.button1id = button1id;
                    $scope.button2id = button2id;
                    $scope.button3id = button3id;
                };

        function createDialogOptions(options) {

            var dialogOptions = {
                templateUrl: options.templateUrl,
                controller: options.controller ? options.controller : DialogInstanceCtrl,
                size : options.size,
                openedClass: options.openedClass,
                resolve: dialogCustomState( options )
            };

            if(options.windowClass !== undefined) {
                dialogOptions.windowClass = options.windowClass;
            }

            if(options.model !== undefined){
                var dscope = $rootScope.$new(true);
                dscope.model = options.model;
                dialogOptions.scope = dscope;
            }

            return dialogOptions;
        }

        function setOptionDefaults(options) {
            // Since we are dealing with ternary defaults checking here it is
            // really difficult to avoid cyclomatic complexity so lets set a high number for it
            /*jshint maxcomplexity:35*/

            // setup options defaults if parameters aren't included
            options.bodyText = options.bodyText === undefined ? '' : options.bodyText;
            options.bodyTextId = options.bodyTextId || undefined;
            options.button1text = options.button1text === undefined ? 'common.ok' : options.button1text;
            options.button2text = options.button2text === undefined ? 'common.cancel' : options.button2text;
            options.button3text = options.button3text === undefined ? undefined : options.button3text;
            options.button2visible = options.button2visible === undefined ? options.button2text !== undefined : options.button2visible;
            options.button3visible = options.button3visible === undefined ? options.button3text !== undefined : options.button3visible;
            options.button1id = options.button1id === undefined ? 'button1' + options.dialogId : options.button1id;
            options.button2id = options.button2id === undefined ? 'button2' + options.dialogId : options.button2id;
            options.button3id = options.button3id === undefined ? 'button3' + options.dialogId : options.button3id;
            options.autoClose = options.autoClose === undefined ? true : options.autoClose;
            options.templateUrl = options.templateUrl === undefined ? '/app/partials/common-dialog.html' : options.templateUrl;
            options.model = options.model === undefined ? {} : options.model;

            // setup model defaults if parameters aren't included
            options.model.errormessageid = options.model.errormessageid ? options.model.errormessageid : 'common.error.cantconnect';
            options.model.acceptprogressdone = options.model.acceptprogressdone ? options.model.acceptprogressdone : true;
            options.model.focus = options.model.focus ? options.model.focus : false;
            options.model.showerror = options.model.showerror ? options.model.showerror : false;
        }

        function dialogCustomState( options ) {
            return {
                model: function() {
                    return options.model;
                },
                dialogId: function() {
                    return angular.copy(options.dialogId);
                },
                title: function(){
                  return options.title;
                },
                titleId: function() {
                    return angular.copy(options.titleId);
                },
                titleText: function(){
                    return options.titleText;
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
                button2id: function() {
                    return angular.copy(options.button2id);
                },
                button3id: function() {
                    return angular.copy(options.button3id);
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
            };
        }

        // Return public API for the service
        return {
            showErrorMessageDialog: _showErrorMessageDialog,
            showMessageDialog: _showMessageDialog,
            showMessageDialogText: _showMessageDialogText,
            showDialog: _showDialog
        };
    });
