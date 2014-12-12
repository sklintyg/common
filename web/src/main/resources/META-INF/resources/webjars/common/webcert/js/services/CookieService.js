/**
 * CookieService
 * The basic idea here is to have a single point of contact (attic) for all cookie related properties.
 * In general it should be easier trace what is being saved and fetched from the cookie store throughout the code.
 * If cookies are sprinkled throughout the code after a time it will get harder to trace cookie usage.
 *
 * Created by stephenwhite on 12/12/14.
 */
angular.module('common').factory('common.CookieService',['$cookieStore', function($cookieStore) {
    'use strict';

    var skipShowUnhandledDialog = 'skipShowUnhandledDialog';

    function _setSkipShowUnhandledDialog(value){
        $cookieStore.set(skipShowUnhandledDialog, value);
    }

    function _isSkipShowUnhandledDialogSet(){
        return $cookieStore.get(skipShowUnhandledDialog);
    }

    return {
        setSkipShowUnhandledDialog: _setSkipShowUnhandledDialog,
        isSkipShowUnhandledDialogSet: _isSkipShowUnhandledDialogSet
    };

}]);
