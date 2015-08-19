angular.module('common').controller('common.wcHeaderController',
    ['$anchorScroll', '$cookieStore', '$location', '$log', '$modal', '$scope', '$state', '$window', 'common.dialogService',
        'common.featureService', 'common.messageService', 'common.statService', 'common.User', 'common.UserModel',
        function($anchorScroll, $cookieStore, $location, $log, $modal, $scope, $state, $window, dialogService,
            featureService, messageService, statService, User, UserModel) {
            'use strict';

            //Expose 'now' as a model property for the template to render as todays date
            $scope.today = new Date();
            $scope.user = UserModel.userContext;
            $scope.statService = statService;
            $scope.statService.startPolling();
            $scope.menuDefs = [];
            $scope.stat = {
                fragaSvarValdEnhet: 0,
                fragaSvarAndraEnheter: 0,
                intygValdEnhet: 0,
                intygAndraEnheter: 0,
                vardgivare: []
            };

            /**
             * Event listeners
             */
            $scope.$on('wc-stat-update', function(event, message) {
                $scope.stat = message;
            });

            /**
             * Private functions
             */

            /**
             * Finds the stat of the enhet or mottagning in the flat
             * structure returned from server
             */
            function findStats(vg, id) {
                for (var i = 0; i < $scope.stat.vardgivare.length; i++) {
                    if (vg === $scope.stat.vardgivare[i].id) {
                        var vardenheter = $scope.stat.vardgivare[i].vardenheter;
                        for (var j = 0; j < vardenheter.length; j++) {
                            var vardenhet = vardenheter[j];
                            if (vardenhet.id === id) {
                                return {
                                    intyg: vardenhet.intyg,
                                    fragaSvar: vardenhet.fragaSvar
                                };
                            }
                        }
                    }
                }
                return {
                    intyg: 0,
                    fragaSvar: 0
                };
            }

            /**
             * Finds the mottagningar at the vardgivare with id `vg` and
             * enhet with id `id`
             */
            function findMottagningar(vg, id) {
                var vgs = $scope.user.vardgivare;
                for (var i = 0; i < vgs.length; i++) {
                    if (vgs[i].id === vg) {
                        var enheter = vgs[i].vardenheter;
                        for (var j = 0; j < enheter.length; j++) {
                            if (enheter[j].id === id) {
                                return enheter[j].mottagningar;
                            }
                        }
                    }
                }
                return undefined;
            }

            /**
             * Finds the total returned from `func` on all mottagningar
             * at the enhet specified by vg and id
             */
            function findAll(vg, id, func) {
                var total = 0;
                var mottagningar = findMottagningar(vg, id) || [];

                for (var i = 0; i < mottagningar.length; i++) {
                    total = total + func(vg, mottagningar[i].id);
                }

                return total;
            }


            function directiveLoad() {
                $scope.menuDefs = buildMenu();
            }

            function buildMenu() {

                var menu = [];
                if (featureService.isFeatureActive(featureService.features.HANTERA_FRAGOR)) {
                    menu.push({
                        link: '/web/dashboard#/unhandled-qa',
                        label: 'Frågor och svar',
                        requiresDoctor: false,
                        statNumberId: 'stat-unitstat-unhandled-question-count',
                        statTooltip: 'not set',
                        id: 'menu-unhandled-qa',
                        getStat: function() {
                            this.statTooltip = 'Vårdenheten har ' + $scope.stat.fragaSvarValdEnhet +
                                ' ej hanterade frågor och svar.';
                            return $scope.stat.fragaSvarValdEnhet || '';
                        }
                    });
                }

                if (featureService.isFeatureActive(featureService.features.HANTERA_INTYGSUTKAST)) {
                    menu.push({
                        link: '/web/dashboard#/unsigned',
                        label: messageService.getProperty('dashboard.unsigned.title'),
                        requiresDoctor: false,
                        statNumberId: 'stat-unitstat-unsigned-certs-count',
                        statTooltip: 'not set',
                        id: 'menu-unsigned',
                        getStat: function() {
                            this.statTooltip =
                                'Vårdenheten har ' + $scope.stat.intygValdEnhet + ' ej signerade utkast.';
                            return $scope.stat.intygValdEnhet || '';
                        }
                    });
                }

                if (featureService.isFeatureActive(featureService.features.HANTERA_INTYGSUTKAST)) {
                    var writeCertMenuDef = {
                        link: '/web/dashboard#/create/index',
                        label: 'Sök/skriv intyg',
                        requiresDoctor: false,
                        id: 'menu-skrivintyg',
                        getStat: function() {
                            return '';
                        }
                    };

                    if (UserModel.userContext.lakare) {
                        menu.splice(0, 0, writeCertMenuDef);
                    } else {
                        menu.push(writeCertMenuDef);
                    }
                }

                menu.push({
                    link: '/web/dashboard#/webcert/about',
                    label: 'Om Webcert',
                    requiresDoctor: false,
                    id: 'menu-about',
                    getStat: function() {
                        return '';
                    }
                });

                return menu;
            }

            /**
             * Exposed scope interaction functions
             */

            $scope.isActive = function(page) {
                if (!page) {
                    return false;
                }

                page = page.substr(page.lastIndexOf('/') + 1);
                if ($state.current.data && angular.isString($state.current.data.defaultActive)) {
                    if (page === $state.current.data.defaultActive) {
                        return true;
                    }
                }

                var currentRoute = $location.path().substr($location.path().lastIndexOf('/') + 1);
                return page === currentRoute;
            };

            function endsWith(str, suffix) {
                return str.indexOf(suffix, str.length - suffix.length) !== -1;
            }

            $scope.logout = function() {
                if (endsWith(UserModel.userContext.authenticationScheme, ':fake')) {
                    $window.location = '/logout';
                } else {
                    iid_Invoke('Logout');
                    $window.location = '/saml/logout/';
                }
            };

            $scope.goToAbout = function() {
                $window.dialogDoneLoading = false;

                var msgbox = $modal.open({
                    templateUrl: '/web/webjars/common/webcert/gui/headers/wcHeaderAboutDialog.template.html',
                    controller: function($scope, $modalInstance) {

                        $scope.close = function() {
                            $modalInstance.close();
                        };

                    },
                    resolve: {}
                });

                dialogService.runOnDialogDoneLoading(msgbox, function() {
                    $window.dialogDoneLoading = true;
                });
            };

            $scope.openChangeCareUnitDialog = function() {
                $window.dialogDoneLoading = false;

                var msgbox = $modal.open({
                    templateUrl: '/web/webjars/common/webcert/gui/headers/wcHeaderCareUnitDialog.template.html',
                    controller: function($scope, $modalInstance, vardgivare) {
                        $scope.vardgivare = vardgivare;
                        $scope.error = false;

                        $scope.close = function() {
                            $modalInstance.close();
                        };

                        /******************
                         * Functions used by wcHeaderCareUnitDialog to
                         * present the data in a structured way
                         ******************/

                        /**
                         * Toggles the value to show or hide the
                         * mottagningar connected to the vardenhet
                         */
                        $scope.toggle = function(enhet) {
                            enhet.showMottagning = !enhet.showMottagning;
                        };

                        $scope.findIntyg = function(vg, id) {
                            return findStats(vg, id).intyg;
                        };

                        $scope.findFragaSvar = function(vg, id) {
                            return findStats(vg, id).fragaSvar;
                        };

                        $scope.findAllFragaSvar = function(vg, id) {
                            return findAll(vg, id, $scope.findFragaSvar);
                        }

                        $scope.findAllIntyg = function(vg, id) {
                            return findAll(vg, id, $scope.findIntyg);
                        }

                        /******************
                         * End of presentation functions
                         ******************/

                        $scope.selectVardenhet = function(enhet) {
                            $scope.error = false;
                            User.setValdVardenhet(enhet, function() {
                                // Remove stored cookie for selected filter. We want to choose a new
                                // filter after choosing another unit to work on
                                $cookieStore.remove('enhetsId');

                                // We updated the user context. Reroute to start page so as not to end
                                // up on a page we aren't welcome anymore. Maybe we should make these
                                // routes some kind of global configuration? No other choices are
                                // relevant today though.
                                if (UserModel.userContext.lakare === true) {
                                    $location.path('/');
                                } else {
                                    $location.path('/unhandled-qa');
                                }
                                $state.reload();

                                $modalInstance.close();
                            }, function() {
                                $scope.error = true;
                            });

                        };
                    },
                    resolve: {
                        vardgivare: function() {
                            return angular.copy($scope.user.vardgivare);
                        }
                    }
                });

                dialogService.runOnDialogDoneLoading(msgbox, function() {
                    $window.dialogDoneLoading = true;
                });
            };

            directiveLoad();
        }
    ]);
