// FNC for detecting for click outside of any elements ============== 
$.fn.clickOff = function (callback, selfDestroy) {
    var clicked = false;
    var parent = this;
    var destroy = selfDestroy || true;

    parent.click(function () {
        clicked = true;
    });

    $(document).click(function (event) {
        if (!clicked) {
            callback(parent, event);
        }
        if (destroy) {
        }
        ;
        clicked = false;
    });
};

/** 
 * PrimeFaces Atlas Layout
 */
var Atlas = {
    
    init: function () {
        this.menuWrapper = $('#layout-menu-cover');
        this.menu = this.menuWrapper.find('#layout-menu');
        this.menulinks = this.menu.find('a.menulink');
        this.menuButton = $('#menu-button');
        this.topmenuButton = $('#topmenu-button');
        this.topbarMenu = $('#topbar-menu');
        this.expandedMenuitems = this.expandedMenuitems || [];
        this.isPopupMenu = $('body').hasClass('PopupMenu');

        this.bindEvents();
        this.equalLinks();
    },
    
    bindEvents: function () {
        var $this = this;
        this.firstActiveMenu = null;

        this.menuButton.on('click', function () {
            $this.menuButtonClick = true;

            if ($this.menuWrapper.hasClass('active')) {
                $this.menuButton.removeClass('active');
                $this.menuWrapper.removeClass('active');
            }
            else {
                $this.menuButton.addClass('active');
                $this.menuWrapper.addClass('active');
                $this.topbarMenu.removeClass('active');
                $this.topmenuButton.removeClass('active');
            }
        });

        this.topmenuButton.on('click', function () {
            $this.topmenuButtonClick = true;

            if ($this.topbarMenu.hasClass('active')) {
                $this.topbarMenu.removeClass('active');
                $this.topmenuButton.removeClass('active');
            }
            else {
                $this.topbarMenu.addClass('active');
                $this.topmenuButton.addClass('active');
                $this.menuButton.removeClass('active');
                $this.menuWrapper.removeClass('active');
            }
        });

        this.menulinks.on('click', function (e) {
            var menuitemLink = $(this),
                    menuitem = menuitemLink.parent();

            if (menuitem.hasClass('active-menu-parent')) {
                menuitem.removeClass('active-menu-parent');
                menuitemLink.removeClass('active-menu').next('ul').removeClass('active-menu');
                $this.removeMenuitem(menuitem.attr('id'));
                $this.menubarActive = false;
            }
            else {
                var activeSiblings = $this.findActiveSiblings(menuitem);
                for(var i = 0; i< activeSiblings.length; i++) {
                    var activeSibling = activeSiblings[i];
                    activeSibling.removeClass('active-menu-parent');
                    $this.removeMenuitem(activeSibling.attr('id'));

                    activeSibling.find('ul.active-menu,a.active-menu').removeClass('active-menu');
                    activeSibling.find('li.active-menu-parent').each(function () {
                        var menuitem = $(this);
                        menuitem.removeClass('active-menu-parent');
                        $this.removeMenuitem(menuitem.attr('id'));
                    });
                }

                menuitem.addClass('active-menu-parent');
                menuitemLink.addClass('active-menu').next('ul').addClass('active-menu');
                $this.addMenuitem(menuitem.attr('id'));
                $this.menubarActive = true;
            }

            if (menuitemLink.next().is('ul')) {
                e.preventDefault();
            }
            else {
                $this.menuButton.removeClass('active');
                $this.menuWrapper.removeClass('active');
            }

            if (!$this.isPopupMenu) {
                $this.firstActiveMenu = $this.menu.children('li.active-menu-parent');
                $this.removeMenuitem($this.firstActiveMenu.attr('id'));
            }

            $this.saveMenuState();
        })
        .on('mouseenter', function() {
            if ($('body').hasClass('PopupMenu')) {
                return;
            }
            
            var menuitemLink = $(this),
                menuitem = menuitemLink.parent();
                
            if($this.menubarActive && document.documentElement.clientWidth > 960 && menuitem.closest('ul').attr('id') === "layout-menu" && !menuitem.hasClass('active-menu-parent')) {
                var prevMenuLink = menuitem.parent().find('a.active-menu');
                
                prevMenuLink.removeClass('active-menu').next('ul.active-menu').removeClass('active-menu');
                prevMenuLink.closest('li').removeClass('active-menu-parent');
                $this.removeMenuitem(prevMenuLink.closest('li').attr('id'));
                menuitem.addClass('active-menu-parent');
                menuitemLink.addClass('active-menu').next('ul[role="menu"]').addClass('active-menu');
            }
           
        });

        this.menuWrapper.clickOff(function (e) {
            if ($this.menuButtonClick) {
                $this.menuButtonClick = false;
            } else {
                $this.menuButton.removeClass('active');
                $this.menuWrapper.removeClass('active');
                $this.menu.children('li.active-menu-parent').removeClass('active-menu-parent').children('a,ul').removeClass('active-menu');
                $this.menubarActive = false;
            }
        });

        this.topbarMenu.clickOff(function (e) {
            if ($this.topmenuButtonClick) {
                $this.topmenuButtonClick = false;
            } else {
                $this.topbarMenu.removeClass('active');
                $this.topmenuButton.removeClass('active');
            }
        });

        $(window).resize(function () {
            $this.equalLinks();
        });

    },
    
    findActiveSiblings: function(menuitem) {
        var $this = this,
            siblings = menuitem.siblings('li'),
            activeSiblings = [];
            
        siblings.each(function () {
            if ($.inArray($(this).attr('id'), $this.expandedMenuitems) !== -1 || $(this).hasClass('active-menu-parent')) {
                activeSiblings.push($(this));
            }
        });

        return activeSiblings;
    },
    
    equalLinks: function () {
        var firstMenuitems = this.menu.children('li');
        /* Scrollbar width is 17px (960-17=943) */
        if ($(window).width() >= 943 && !$('body').hasClass('PopupMenu')) {
            var maxHeight = 0,
                    firstChildLinks = firstMenuitems.children('a');

            firstChildLinks.each(function () {
                $(this).height('auto');
                if ($(this).height() > maxHeight) {
                    maxHeight = $(this).height();
                }
            });
            firstMenuitems.height(maxHeight);
            if($.browser.msie && parseInt($.browser.version, 10) === 11) {
                firstChildLinks.css({
                    'height':'100%',
                    'box-sizing': 'border-box'
                });
            }
            else {
                firstChildLinks.height('100%');
            }
        }
        else {
            firstMenuitems.height('auto');
        }
    },
    
    removeMenuitem: function (id) {
        this.expandedMenuitems = $.grep(this.expandedMenuitems, function (value) {
            return value !== id;
        });
    },
    
    addMenuitem: function (id) {
        if ($.inArray(id, this.expandedMenuitems) === -1) {
            this.expandedMenuitems.push(id);
        }
    },
    
    saveMenuState: function () {
        $.cookie('atlas_expandeditems', this.expandedMenuitems.join(','), {path: '/'});
    },
    
    clearMenuState: function () {
        $.removeCookie('atlas_expandeditems', {path: '/'});
    },
    
    restoreMenuState: function () {
        var menucookie = $.cookie('atlas_expandeditems');
        if (menucookie) {
            this.expandedMenuitems = menucookie.split(',');
            for (var i = 0; i < this.expandedMenuitems.length; i++) {
                var id = this.expandedMenuitems[i];
                if (id) {
                    var menuitem = $("#" + this.expandedMenuitems[i].replace(/:/g, "\\:"));
                    menuitem.addClass('active-menu-parent');
                    menuitem.children('a,ul').addClass('active-menu');
                }
            }
        }
    },
    
    isMobile: function () {
        return (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(window.navigator.userAgent));
    },
    
    changeBodyClass: function (changedClass) {
        var $this = this,
        body = $('body'),
        bodyClasses = body.attr('class');

        if (!changedClass.length) {
            changedClass = bodyClasses.replace("PopupMenu", "");
        }
        else if (bodyClasses.indexOf('PopupMenu') >= 0) {
            if (changedClass === 'PopupMenu')
                changedClass = bodyClasses;
            else
                changedClass = changedClass + " PopupMenu";
        }
        else if (changedClass === 'PopupMenu') {
            changedClass = changedClass + " " + bodyClasses;
        }

        body.removeClass().addClass(changedClass);

        this.menuButton.on('click', function () {
            if ($('body').hasClass('PopupMenu')) {
                $this.equalLinks();
            }
        });

        return false;
    }
};

$(function () {
    Atlas.init();
});

/* Issue #924 is fixed for 5.3+ and 6.0. (compatibility with 5.3) */
PrimeFaces.widget.Dialog = PrimeFaces.widget.Dialog.extend({
    enableModality: function () {
        this._super();
        $(document.body).children(this.jqId + '_modal').addClass('ui-dialog-mask');
    },
    syncWindowResize: function () {

    }
});
