/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

describe('recipientsFactory', function() {
  'use strict';

  var recipientsFactory;

  beforeEach(angular.mock.module('common', function($provide) {
  }));

  beforeEach(angular.mock.inject(['common.recipientsFactory', function(_recipientsFactory_) {
    var knownRecipients = [
      {id: 'FK', name: 'Kassan'},
      {id: 'FBA', name: 'Företaget AB'},
      {id: 'TRANSP', name: 'Transportstyrelsen'}
    ];

    recipientsFactory = _recipientsFactory_;
    recipientsFactory.setRecipients(knownRecipients);
  }]));

  it('should return correct name when recipient was found', function() {
    var result = recipientsFactory.getNameForId('TRANSP');
    expect(result).toEqual('Transportstyrelsen');
  });

  it('should return undefined  when recipient is not found', function() {
    var result = recipientsFactory.getNameForId('dummy');
    expect(result).toBeUndefined();
  });

});