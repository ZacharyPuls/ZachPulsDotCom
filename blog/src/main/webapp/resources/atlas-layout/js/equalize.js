equalheight = function(container) {

    var currentTallest = 0,
            currentRowStart = 0,
            rowDivs = new Array(),
            $el,
            topPosition = 0,
            menuCoverScroll;

    $(container).each(function() {

        $el = $(this);
        $($el).height('auto');
        menuCoverScroll = $('#layout-menu-cover').scrollTop();

        topPostion = $el.offset().top + menuCoverScroll;

        if(currentRowStart != topPostion) {
            for (currentDiv = 0; currentDiv < rowDivs.length; currentDiv++) {
                rowDivs[currentDiv].height(currentTallest);
            }
            rowDivs.length = 0; // empty the array
            currentRowStart = topPostion;
            currentTallest = $el.height();
            rowDivs.push($el);
        } else {
            rowDivs.push($el);
            currentTallest = (currentTallest < $el.height()) ? ($el.height()) : (currentTallest);
        }
        for(currentDiv = 0; currentDiv < rowDivs.length; currentDiv++) {
            rowDivs[currentDiv].height(currentTallest);
        }
    });
};

// Equalizing of height of first level nodes of main menu
$(window).resize(function () {
    equalheight('.WideMenu .layout-menu > li');
});