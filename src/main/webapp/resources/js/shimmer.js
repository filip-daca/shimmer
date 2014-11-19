/**
 * Funkcje główne portalu
 */
var Shimmer = {
		
	/**
	 * Ustawia aktywną stronę w nawigacji.
	 * @param id - id strony
	 */
    setActivePage: function (id) {
        $(".navbar ul.nav li").each(function () {
            var link = $(this).children("a")[0];
            if (link && link.id.endsWith('navigation-' + id)) {
                $(this).addClass('active');
            } else {
                $(this).removeClass('active');
            }
        });
    },
    
    /**
     * Podświetla aktywną zakładkę w głównej nawigacji
     * @param id - id elementu nawigacji
     */
    setActiveTab: function (id) {
        $(".nav-tabs > li").each(function () {
            var node = $(this);
            if (node.length > 0 && node.attr("id") == 'tab-' + id) {
                $(this).addClass('active');
            } else {
                $(this).removeClass('active');
            }
        });
    },

    /**
     * Podświetla pola z błędami
     */
    highlightFieldsWithErrors: function () {
        $(".ui-message-error").parents("div.control-group").addClass("error");
    },
    
    /**
     * Przewija stronę na górę
     */
    scrollToTop: function () {
    	window.scrollTo(0, 0);
    },
    
    /**
     * Pokazuje element
     * @param id - id elementu
     */
    show: function (id) {
    	$('#' + id).show(500);
    },

    /**
     * Pokazuje element jsf
     * @param id - id elementu
     */
    componentShow: function (id) {
    	$('[id$=' + id + ']').show(500);
    },
    
    /**
     * Chowa element
     * @param id - id elementu
     */
    hide: function (id) {
    	$('#' + id).hide(500);
    },
    
    /**
     * Chowa element jsf
     * @param id - id elementu
     */
    componentHide: function (id) {
    	$('[id$=' + id + ']').hide(500);
    },
    
    /**
     * Pokazuje element
     * @param id - id elementu
     */
    showAndHide: function (id) {
    	$('#' + id).show(500);
    },
    
    /**
     * Chowa element
     * @param id - id elementu
     */
    hideTableRow: function (id, rowNumber) {
    	$("[id$=" + id + "] [data-ri=" + rowNumber + "]").hide(500);
    },
    
    /**
     * Pokazuje lub chowa element
     * @param id - id elementu
     * @param show - czy pokazać?
     */
    toggle: function (id, show) {
    	if (show) {
    		this.show(id);
    	} else {
    		this.hide(id);
    	}
    }

};

/**
 * Czy napis kończy się danym sufiksem?
 * @param suffix - sufiks do weryfikacji
 * @returns {Boolean} tak / nie
 */
String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};