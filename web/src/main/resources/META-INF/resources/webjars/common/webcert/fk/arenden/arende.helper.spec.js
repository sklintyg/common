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

describe('arendeHelper', function() {
    'use strict';

    var ArendeListItemModel;
    var ArendeHelper;

    var arende1, arende2, arende3, arende4, arende5, arende6;
    var itemList, item1, item2, item3;

    beforeEach(angular.mock.inject(['common.ArendeListItemModel', 'common.ArendeHelper',
        function(_ArendeListItemModel_, _ArendeHelper_) {
            ArendeListItemModel = _ArendeListItemModel_;
            ArendeHelper = _ArendeHelper_;

            arende1 = {id:1, fraga:{ amne: 'KOMPLT' }, svar:{}, paminnelser:[]};
            arende2 = {id:2, fraga:{}, svar:{}, paminnelser:[]};
            arende3 = {id:3, fraga:{}, svar:{}, paminnelser:[]};
            arende4 = {id:4, fraga:{ amne: 'KOMPLT' }, svar:{}, paminnelser:[]};
            arende5 = {id:5, fraga:{ amne: 'KOMPLT' }, svar:{}, paminnelser:[]};
            arende6 = {id:6, fraga:{ amne: 'KOMPLT' }, svar:{}, paminnelser:[]};
            itemList = [
                item1 = ArendeListItemModel.build(arende1, [arende4, arende5, arende6]),
                item2 = ArendeListItemModel.build(arende2),
                item3 = ArendeListItemModel.build(arende3)
            ];
        }
    ]));

    it('Should split main item', function() {
        ArendeHelper.splitToSingleItem(item1, itemList);

        expect(itemList.length).toBe(4);
        expect(itemList[0].arende.id).toBe(4);
        expect(itemList[0].extraKompletteringarArenden.length).toBe(2);
        expect(itemList[0].extraKompletteringarArenden[0].arende.id).toBe(5);
        expect(itemList[0].extraKompletteringarArenden[1].arende.id).toBe(6);

        expect(itemList[1].arende.id).toBe(1);
        expect(itemList[1].extraKompletteringarArenden).toEqual([]);

        expect(itemList[2].arende.id).toBe(2);
        expect(itemList[2].extraKompletteringarArenden).toEqual([]);

        expect(itemList[3].arende.id).toBe(3);
        expect(itemList[3].extraKompletteringarArenden).toEqual([]);
    });

    it('Should split first subitem', function() {
        ArendeHelper.splitToSingleItem(item1.extraKompletteringarArenden[0], itemList);

        expect(itemList.length).toBe(4);
        expect(itemList[0].arende.id).toBe(1);
        expect(itemList[0].extraKompletteringarArenden.length).toBe(2);
        expect(itemList[0].extraKompletteringarArenden[0].arende.id).toBe(5);
        expect(itemList[0].extraKompletteringarArenden[1].arende.id).toBe(6);
        expect(itemList[1].arende.id).toBe(2);
        expect(itemList[1].extraKompletteringarArenden).toEqual([]);
        expect(itemList[2].arende.id).toBe(3);
        expect(itemList[2].extraKompletteringarArenden).toEqual([]);
        expect(itemList[3].arende.id).toBe(4);
        expect(itemList[3].extraKompletteringarArenden).toEqual([]);
    });


    it('Should split second subitem', function() {
        ArendeHelper.splitToSingleItem(item1.extraKompletteringarArenden[1], itemList);

        expect(itemList[0].arende.id).toBe(1);
        expect(itemList[0].extraKompletteringarArenden.length).toBe(2);
        expect(itemList[0].extraKompletteringarArenden[0].arende.id).toBe(4);
        expect(itemList[0].extraKompletteringarArenden[1].arende.id).toBe(6);
        expect(itemList[1].arende.id).toBe(2);
        expect(itemList[1].extraKompletteringarArenden).toEqual([]);
        expect(itemList[2].arende.id).toBe(3);
        expect(itemList[2].extraKompletteringarArenden).toEqual([]);
        expect(itemList[3].arende.id).toBe(5);
        expect(itemList[3].extraKompletteringarArenden).toEqual([]);
    });

    it('Should not merge if not komplettering', function() {
        ArendeHelper.checkMergeToKompletteringItem(item3, itemList);

        expect(itemList.length).toBe(3);
        expect(itemList[0].arende.id).toBe(1);
        expect(itemList[0].extraKompletteringarArenden.length).toBe(3);
        expect(itemList[0].extraKompletteringarArenden[0].arende.id).toBe(4);
        expect(itemList[0].extraKompletteringarArenden[1].arende.id).toBe(5);
        expect(itemList[0].extraKompletteringarArenden[2].arende.id).toBe(6);
        expect(itemList[1].arende.id).toBe(2);
        expect(itemList[1].extraKompletteringarArenden).toEqual([]);
        expect(itemList[2].arende.id).toBe(3);
        expect(itemList[2].extraKompletteringarArenden).toEqual([]);
    });

    it('Should merge if komplettering', function() {
        arende3.fraga.amne = 'KOMPLT';
        ArendeHelper.checkMergeToKompletteringItem(item3, itemList);

        expect(itemList.length).toBe(2);
        expect(itemList[0].arende.id).toBe(1);
        expect(itemList[0].extraKompletteringarArenden.length).toBe(4);
        expect(itemList[0].extraKompletteringarArenden[0].arende.id).toBe(4);
        expect(itemList[0].extraKompletteringarArenden[1].arende.id).toBe(5);
        expect(itemList[0].extraKompletteringarArenden[2].arende.id).toBe(6);
        expect(itemList[0].extraKompletteringarArenden[3].arende.id).toBe(3);
        expect(itemList[1].arende.id).toBe(2);
        expect(itemList[1].extraKompletteringarArenden).toEqual([]);
    });

    it('Should merge and reorder if komplettering has later timestamp', function() {
        arende1.senasteHandelse = '2016-08-21T11:02:41.800';
        arende3.fraga.amne = 'KOMPLT';
        arende3.senasteHandelse = '2016-09-21T11:02:41.800';
        ArendeHelper.checkMergeToKompletteringItem(item3, itemList);

        expect(itemList.length).toBe(2);
        expect(itemList[0].arende.id).toBe(2);
        expect(itemList[0].extraKompletteringarArenden).toEqual([]);
        expect(itemList[1].arende.id).toBe(3);
        expect(itemList[1].extraKompletteringarArenden.length).toBe(4);
        expect(itemList[1].extraKompletteringarArenden[0].arende.id).toBe(4);
        expect(itemList[1].extraKompletteringarArenden[1].arende.id).toBe(5);
        expect(itemList[1].extraKompletteringarArenden[2].arende.id).toBe(6);
        expect(itemList[1].extraKompletteringarArenden[3].arende.id).toBe(1);
    });

    it('should split all into single items', function() {
        ArendeHelper.splitAllToSingleItems(item1, itemList);

        expect(itemList.length).toBe(6);
        expect(itemList[0].arende.id).toBe(1);
        expect(itemList[1].arende.id).toBe(2);
        expect(itemList[2].arende.id).toBe(3);
        expect(itemList[3].arende.id).toBe(4);
        expect(itemList[4].arende.id).toBe(5);
        expect(itemList[5].arende.id).toBe(6);
    });
});