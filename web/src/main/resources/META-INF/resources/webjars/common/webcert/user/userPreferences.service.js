/**
 * UserPreferencesService
 * The basic idea here is to have a single point of contact (attic) for all cookie related properties that are used as UserPreferences.
 * In general it should be easier trace what is being saved and fetched from the cookie store throughout the code.
 * If cookies are sprinkled throughout the code after a time it will get harder to trace cookie usage.
 *
 * Created by stephenwhite on 12/12/14.
 */
angular.module('common').factory('common.UserPreferencesService',['$cookies', function($cookies) {
    'use strict';

    var skipShowUnhandledDialog = 'skipShowUnhandledDialog';
    _setSkipShowUnhandledDialog(false);

    function _setSkipShowUnhandledDialog(value){
        $cookies.putObject(skipShowUnhandledDialog, value);
    }

    function _isSkipShowUnhandledDialogSet(){
        return $cookies.getObject(skipShowUnhandledDialog);
    }

    return {
        setSkipShowUnhandledDialog: _setSkipShowUnhandledDialog,
        isSkipShowUnhandledDialogSet: _isSkipShowUnhandledDialogSet
    };

}]);
