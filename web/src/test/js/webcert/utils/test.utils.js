/**
 * Created by stephenwhite on 21/09/15.
 */
function getTestUser (role){
    return {
        'hsaId': 'eva',
        'namn': 'Eva Holgersson',
        'lakare': true,
        'forskrivarkod': '2481632',
        'authenticationScheme': 'urn:inera:webcert:fake',
        'vardgivare': [
            {
                'id': 'vastmanland', 'namn': 'Landstinget Västmanland', 'vardenheter': [
                {
                    'id': 'centrum-vast', 'namn': 'Vårdcentrum i Väst', 'arbetsplatskod': '0000000', 'mottagningar': [
                    {'id': 'akuten', 'namn': 'Akuten', 'arbetsplatskod': '0000000'},
                    {'id': 'dialys', 'namn': 'Dialys', 'arbetsplatskod': '0000000'}
                ]
                }
            ]
            },
            {
                'id': 'ostergotland', 'namn': 'Landstinget Östergötland', 'vardenheter': [
                {
                    'id': 'linkoping',
                    'namn': 'Linköpings Universitetssjukhus',
                    'arbetsplatskod': '0000000',
                    'mottagningar': [
                        {'id': 'lkpg-akuten', 'namn': 'Akuten', 'arbetsplatskod': '0000000'},
                        {'id': 'lkpg-ogon', 'namn': 'Ögonmottagningen', 'arbetsplatskod': '0000000'}
                    ]
                }
            ]
            }
        ],
        'specialiseringar': ['Kirurgi', 'Oftalmologi'],
        'titel': 'Leg. Ögonläkare',
        'legitimeradeYrkesgrupper': ['Läkare'],
        'valdVardenhet': {
            'id': 'centrum-vast', 'namn': 'Vårdcentrum i Väst', 'arbetsplatskod': '0000000', 'mottagningar': [
                {'id': 'akuten', 'namn': 'Akuten', 'arbetsplatskod': '0000000'},
                {'id': 'dialys', 'namn': 'Dialys', 'arbetsplatskod': '0000000'}
            ]
        },
        'valdVardgivare': {
            'id': 'vastmanland', 'namn': 'Landstinget Västmanland', 'vardenheter': [
                {
                    'id': 'centrum-vast', 'namn': 'Vårdcentrum i Väst', 'arbetsplatskod': '0000000', 'mottagningar': [
                    {'id': 'akuten', 'namn': 'Akuten', 'arbetsplatskod': '0000000'},
                    {'id': 'dialys', 'namn': 'Dialys', 'arbetsplatskod': '0000000'}
                ]
                }
            ]
        },
        'roles': role,
        'aktivaFunktioner': ['hanteraFragor', 'hanteraFragor.fk7263'],
        'totaltAntalVardenheter': 6
    };

}