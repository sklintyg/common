$filter = function(type) {
    if (type === 'number') {
        return function(number, fractionSize) {

            // if null or undefined pass it through
            return (number == null)
                ? number
                : number;
        };
    }

    if (type === 'uvBoolFilter') {
        return function(value) {
            return value == null || typeof value === 'undefined' ||value === '' || value === 'false' || value === false
                ? 'Nej' : 'Ja'
        };
    }

    // default.
    return function(value) {
        return value;
    }
}
