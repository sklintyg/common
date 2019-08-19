describe('Filter: uvDomIdFilter', function() {
  'use strict';

  var _filter;

  beforeEach(angular.mock.module('common'));

  beforeEach(inject(function(_uvDomIdFilterFilter_) {
    _filter = _uvDomIdFilterFilter_;
  }));

  it('should replace non-valid characters', function() {
    expect(_filter('ABC-D:E-[0].svar')).toEqual('ABC-D-E--0--svar');
    expect(_filter('my.prop')).toEqual('my-prop');
  });

  it('should not replace valid charachers', function() {
    expect(_filter('synVinkel1')).toEqual('synVinkel1');
  });

});
