$.fn.selectRegion = function(options) {
    var defaults = {
            'color': 'red',
            'fontSize': '12px'
        };
        var settings = $.extend(defaults, options);
        return this.css({
            'color': settings.color,
            'fontSize': settings.fontSize
        });
    }