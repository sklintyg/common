angular.module('common').service('common.ArendeHelper',
    ['$log', '$timeout', '$window', 'common.statService', 'common.ArendeListItemModel',
        function ($log, $timeout, $window, statService, ArendeListItemModel) {
            'use strict';

            this.createListItemsFromArenden = function(arendeModelList) {
                var arendeListItemList = [];
                var kompletteringar = [];

                angular.forEach(arendeModelList, function (arendeModel) {
                    if (arendeModel.fraga.amne === 'KOMPLT' && arendeModel.fraga.status !== 'CLOSED') {
                        kompletteringar.push(arendeModel);
                    }
                    else {
                        arendeListItemList.push(this.createArendeListItem(arendeModel));
                    }
                }, this);

                if (kompletteringar.length > 0) {
                    // När det kommer två kompletteringsbegäran på samma intyg, ska den senaste inkomna hamna överst
                    // i fråga/svar vyn.
                    kompletteringar.sort(_arendeSortFunction);
                    // Om det finns två kompletteringsfrågor så placeras båda frågorna i samma grå bakgrundsplatta, så
                    // att det visuellt indikerar att det hänger ihop.
                    arendeListItemList.push(this.createArendeListItem(kompletteringar[0], kompletteringar.splice(1)));
                }

                return arendeListItemList;
            };

            this.createArendeListItem = function(arendeModel, kompletteringarArendeModel) {
                var arendeListItem = ArendeListItemModel.build(arendeModel, kompletteringarArendeModel);
                return arendeListItem;
            };

            function _arendeSortFunction(a,b) {
                if (a.paminnelser.length > 0 && b.paminnelser.length === 0) {
                    return -1;
                }
                if (b.paminnelser.length > 0 && a.paminnelser.length === 0) {
                    return 1;
                }
                if (a.senasteHandelse > b.senasteHandelse) {
                    return -1;
                }
                if (a.senasteHandelse < b.senasteHandelse) {
                    return 1;
                }
                return 0;
            }

            function _arendeListItemSortFunction(a,b) {
                return _arendeSortFunction(a.arende, b.arende);
            }

            function _mergeToKompletteringItem(insertArendeListItem, mainArendeListItem, arendeList) {

                // Check if insertArendeListItem should become the new main listitem
                if (_arendeListItemSortFunction(insertArendeListItem, mainArendeListItem) < 0) {
                    var swap = insertArendeListItem;
                    insertArendeListItem = mainArendeListItem;
                    mainArendeListItem = swap;

                    mainArendeListItem.parentListItem = null;
                    // Move all items from insertArendeListItem.extraKompletteringarArenden
                    // to mainArendeListItem.extraKompletteringarArenden
                    Array.prototype.push.apply(mainArendeListItem.extraKompletteringarArenden,
                        insertArendeListItem.extraKompletteringarArenden.splice(
                            0, insertArendeListItem.extraKompletteringarArenden.length));
                }

                // Add into mainArendeListItem extraKompletteringarArenden
                insertArendeListItem.parentListItem = mainArendeListItem;
                mainArendeListItem.extraKompletteringarArenden.push(insertArendeListItem);

                mainArendeListItem.extraKompletteringarArenden.sort(_arendeListItemSortFunction);


                // and remove this listitem from root
                var index = arendeList.indexOf(insertArendeListItem);
                arendeList.splice(index, 1);
            }

            this.checkMergeToKompletteringItem = function(arendeListItem, arendeList) {
                if (arendeListItem.arende.fraga.amne === 'KOMPLT' && arendeListItem.arende.fraga.status !== 'CLOSED') {
                    // Search for existing open komplettering
                    for (var i = 0; i < arendeList.length; i++) {
                        var arendeListItem2 = arendeList[i];

                        if (arendeListItem !== arendeListItem2 &&
                            arendeListItem2.arende.fraga.amne === 'KOMPLT' &&
                            arendeListItem2.arende.fraga.status !== 'CLOSED') {

                            _mergeToKompletteringItem(arendeListItem, arendeListItem2, arendeList);

                            break;
                        }
                    }
                }
            };

            this.splitToSingleItem = function(arendeListItem, arendeList) {
                if (arendeListItem.extraKompletteringarArenden.length === 1) {
                    // Add all items from extraKompletteringarArenden into arendeList
                    arendeList.push(arendeListItem.extraKompletteringarArenden[0]);
                    arendeListItem.extraKompletteringarArenden[0].parentListItem = null;
                    // Clear the array
                    arendeListItem.extraKompletteringarArenden.pop();
                }
                else if (arendeListItem.extraKompletteringarArenden.length > 1) {
                    // Promote first item from extraKompletteringarArenden to main item
                    var newMainItem = arendeListItem.extraKompletteringarArenden.splice(0, 1)[0];
                    arendeList.unshift(newMainItem);
                    newMainItem.parentListItem = null;
                    // Move all remaining items under this item
                    Array.prototype.push.apply(newMainItem.extraKompletteringarArenden,
                        arendeListItem.extraKompletteringarArenden.splice(0, arendeListItem.extraKompletteringarArenden.length));
                    angular.forEach(newMainItem.extraKompletteringarArenden, function(subItem) {
                        subItem.parentListItem = newMainItem;
                    });
                }
                if (arendeListItem.parentListItem) {
                    // remove this listitem from parent
                    var index = arendeListItem.parentListItem.extraKompletteringarArenden.indexOf(arendeListItem);
                    arendeListItem.parentListItem.extraKompletteringarArenden.splice(index, 1);
                    arendeListItem.parentListItem = null;
                    // and add it to root list
                    arendeList.push(arendeListItem);
                }
            };

        }]);
