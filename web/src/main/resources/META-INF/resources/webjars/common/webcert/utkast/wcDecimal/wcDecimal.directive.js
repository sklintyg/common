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
 * Directive for 'Decimal numbers' with two way binding /
 */
angular.module('common').directive('wcDecimalNumber',
    function() {
        'use strict';

        return {
            restrict: 'A',
            require: 'ngModel',
            scope: {
                wcDecimalMaxNumbers: '@'
            },

            link: function(scope, elem, attrs, ngModelCtrl) {
                var decimalPoint = /[\,,\.]/;
                var number = /[0-9]/;

                function filter(s) {
                    var filtered = '', dec = false;
                    angular.forEach(s, function(c) {
                        if (number.test(c)) {
                            filtered += c;
                        }
                        if (!dec && decimalPoint.test(c)) {
                            dec = true;
                            filtered += ',';
                        }
                    });
                    if (scope.wcDecimalMaxNumbers > 0) {
                        // Limits length to maxNumbers with optional decimal separator
                        var maxLength = scope.wcDecimalMaxNumbers;
                        if (dec) {
                            maxLength++;
                        }
                        return filtered.length <= maxLength ? filtered : filtered.substring(0, maxLength);
                    }
                    return filtered;
                }

                function format(value) {
                    var length = value.length, valForView = '', valForModel = null;

                    if (length > 0) {
                        if (_isMaxTwoDecimals( scope )) {
                            valForView = _getValueForView( value );
                            valForModel = Number(valForView[0] + '.' + valForView[2]);
                        }
                        else {
                            valForView = value.replace('.', ',');
                            valForModel = valForView.replace(',', '.');
                        }
                    }

                    return {
                        valForView: valForView,
                        valForModel: valForModel
                    };
                }

                function _isMaxTwoDecimals(value) {
                    if(value === 'undefined') {
                        return false;
                    }
                    return value.wcDecimalMaxNumbers === 2 || value.wcDecimalMaxNumbers === '2';
                }

                function _getValueForView( value ) {
                    var valForView = '',
                        length = value.length;

                    if (length === 1) {
                        if (value[0] === ',') {
                            valForView = '0,0';
                        }
                        else {
                            valForView = value[0] + ',0';
                        }
                    } else if (length === 2) {
                        if (value[0] === ',') {
                            valForView = '0,' + value[1];
                        } else if (value[1] === ',') {
                            valForView = value[0] + ',0';
                        } else {
                            valForView = value[0] + ',' + value[1];
                        }
                    } else if (length === 3) {
                        if (value[0] === ',') {
                            valForView = '0,' + value[1];
                        } else if (value[1] === ',') {
                            valForView = value;
                        } else {
                            valForView = value[0] + ',' + value[1];
                        }
                    }
                    return valForView;
                }

                var blurFormat = function blurFormat() {
                    var filtered = filter(this.value);
                    var val = format(filtered);
                    if (this.value !== val.valForView) {
                        this.value = val.valForView;
                    }
                };

                function decimalParse(valFromView) {
                    var filtered = filter(valFromView);
                    var val = format(filtered);

                    if (filtered !== valFromView) {
                        ngModelCtrl.$setViewValue(filtered);
                        ngModelCtrl.$render();
                    }
                    return val.valForModel;
                }

                elem.bind('blur', blurFormat);
                ngModelCtrl.$parsers.push(decimalParse);
                ngModelCtrl.$formatters.push(function(valFromModel) {
                    if (typeof valFromModel === 'undefined' || valFromModel === null) {
                        return undefined;
                    }
                    var val = format(filter(valFromModel.toString()));
                    return val.valForView;
                });
            }
        };
    });
