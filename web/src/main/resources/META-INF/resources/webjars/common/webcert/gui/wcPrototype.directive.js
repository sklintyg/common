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
 * wc-prototype directive makes it possible to include a dropdown switch and several tempaltes in a specified area
 *
 * usage:
 *  directive demands the following attributes on the element:
 *  dropdown-type : button or nothing, will render as a link if not button
 *  selected-value: property for holding the currently selected value
 *  preselected-value: the default template to load as default, and also triggers on the 'default' selected value
 *  data-dropdown-data: data array for populating the dropdown with template ids and names, used for the nested directive wc-prototype-template
 */
angular.module('common').directive('wcPrototype',
    function($compile) {
        'use strict';
        return {
            restrict: 'A',
            scope: {
                items: '=dropdownData',
                doSelect: '&selectVal',
                selectedItem: '=preselectedItem'
            },
            link: function(scope, element, attrs) {
                var html = '<div class="form-group proto-selector">';

                switch (attrs.dropdownType) {
                case 'button':
                    html +=
                        '<div class="btn-group"><button class="btn button-label btn-info">Action</button>' +
                        '<button class="btn btn-info dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">' +
                        '<span class="glyphicon glyphicon-random" aria-hidden="true"></span><span class="caret"></span><span class="sr-only">Byt gränssnitt</span>' +
                        '</button><span class="caret"></span></button>';
                    break;
                default:
                    html +=
                        '<div class="dropdown"><a class="dropdown-toggle" role="button" data-toggle="dropdown"  href="javascript:;">Dropdown<b class="caret"></b></a>';
                    break;
                }
                html +=
                    '<ul class="dropdown-menu"><li ng-repeat="item in items"><a tabindex="-1" data-ng-click="selectVal(item)">{{item.name}}</a></li></ul></div>';
                html += '</div>';
                element.append($compile(html)(scope));

                for (var i = 0; i < scope.items.length; i++) {
                    if (scope.items[i].id === scope.selectedItem) {
                        scope.bSelectedItem = scope.items[i];
                        break;
                    }
                }

                scope.selectVal = function (item) {
                    switch (attrs.menuType) {
                    case 'button':
                        $('button.button-label', element).html(item.name);
                        break;
                    default:
                        $('a.dropdown-toggle', element).html('<b class="caret"></b> ' + item.name);
                        break;
                    }
                    scope.doSelect({
                        selectedVal: item.id
                    });
                };

            }


        };

    });