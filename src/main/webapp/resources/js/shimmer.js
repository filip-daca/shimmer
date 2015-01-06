// Ustawienia symulacji
var Shimmer = {
	width: 800,
	height: 650,
	nodesCount: 0,
	enableHeatmap: true,
	heatmapUpdateInterval: 100,
	heatmapTimeout: 0,
	
	networkContainer: undefined,
	data: undefined,
	network: undefined,
	
	heatmapContainer: undefined,
	heatmap: undefined,
	
	/**
	 * Creates new network.
	 */
	createNetwork: function() {
		Shimmer.network = new vis.Network(
			Shimmer.networkContainer, 
			Shimmer.data, 
			{
				nodes: {
					shape: 'dot',
					fontColor: 'white',
				},
				edges: {
					width: 1,
				},
				smoothCurves: true,
				configurePhysics: true,
			}
			);
	},
	
	/**
	 * Creates new heatmap.
	 */
	createHeatmap: function() {
		Shimmer.heatmap = h337.create({
			container: Shimmer.heatmapContainer,
			gradient: { ".1": 'rgba(0,0,0,0)', "0.25": "rgba(0,0,90, .6)", ".6": "blue", ".9": "cyan", ".95": 'rgba(255,255,255,.4)'},
			maxOpacity: .6,
			radius: 10,
			blur: .90
		});
	},
	
	/**
	 * Reloads network data.
	 */
	reloadData: function() {
		Shimmer.network.setData(Shimmer.data);
	},
	
	/**
	 * Finds node X param.
	 * @param i - node index
	 * @returns node X param
	 */
	getNodeX: function(i) {
		var canvasScaledXCenter = Shimmer.network.getScale() * Shimmer.network.getCenterCoordinates().x;
		var nodeScaledXDelta = Shimmer.width / 2 + Shimmer.network.getScale() * Shimmer.network.nodes[i].x;
		
		return Math.floor(nodeScaledXDelta - canvasScaledXCenter);
	},
	
	/**
	 * Finds node Y param.
	 * @param i - node index
	 * @returns node Y param
	 */
	getNodeY: function(i) {
		var canvasScaledYCenter = Shimmer.network.getScale() * Shimmer.network.getCenterCoordinates().y;
		var nodeScaledYDelta = Shimmer.height / 2 + Shimmer.network.getScale() * Shimmer.network.nodes[i].y;
		
		return Math.floor(nodeScaledYDelta - canvasScaledYCenter);
	},
	
	/**
	 * Updates heatmap image.
	 */
	updateHeatmap: function () {
		var i;
		var nodesCount = Shimmer.nodesCount;
		var nodeX, nodeY;

		if (!Shimmer.enableHeatmap) {
			// Not updating heatmap when it's disabled
			return;
		}
		
		Shimmer.heatmap.setData({min: 0, max:30, data: []});
		for (i = 0; i < nodesCount; i++) {
			nodeX = Shimmer.getNodeX(i);
			nodeY = Shimmer.getNodeY(i);
			Shimmer.heatmap.addData({
				x: nodeX, 
				y: nodeY, 
				value: Shimmer.data.nodes[i].heatValue, 
				radius: Shimmer.network.getScale() * 100
			});
		}
	},
	
	/**
	 * Starts updating heatmap.
	 */
	startUpdatingHeatmap: function() {
		ShimmerWeb.show("calculatingStatus");
		Shimmer.heatmapTimeout = setInterval(function(){Shimmer.updateHeatmap();}, 
			Shimmer.heatmapUpdateInterval);
	},
	
	/**
	 * Stops updating heatmap.
	 */
	stopUpdatingHeatmap: function() {
		clearInterval(Shimmer.heatmapTimeout);
		ShimmerWeb.hide("calculatingStatus");
	},
	
	/**
	 * Toggles heatmap functionality.
	 */
	toggleHeatmap: function() {
		Shimmer.enableHeatmap = !Shimmer.enableHeatmap;
		if (Shimmer.enableHeatmap) {
			$("#visualisationMode").text("Constallation");
			$("#visualisationMode").removeClass("label-warning");
			$("#visualisationMode").addClass("label-primary");
			Shimmer.changeEdgesColor('transparent');
			ShimmerWeb.show("shimmerHeatmap");
		} else {
			$("#visualisationMode").text("Graph");
			$("#visualisationMode").removeClass("label-primary");
			$("#visualisationMode").addClass("label-warning");
			Shimmer.changeEdgesColor('black');
			ShimmerWeb.hide("shimmerHeatmap");
		}
	},
	
	/**
	 * Changes color of edges.
	 * @param color - color to change
	 */
	changeEdgesColor: function(color) {
		var i;
		var nodesCount = Shimmer.nodesCount;
		for (i = 0; i < nodesCount; i++) {
			var oldBackgroundColor = Shimmer.data.nodes[i].color.background;
			Shimmer.data.nodes[i].color = { background : oldBackgroundColor, border : color };
			Shimmer.network.nodes[i].color = { background : oldBackgroundColor, border : color };
		}
		Shimmer.reloadData();
		Shimmer.updateHeatmap();
	},
	
	nodeSelectCallback: function(event) {
		var node = nodes[event.nodes[0]];
		var metaData = node.shimmerProperties;
		$("#shimmerInfo .packageName").text(node.label);
		$("#shimmerInfo .classCount").text(metaData.classCount);
		$("#shimmerInfo .concreteClassesCount").text(metaData.concreteClassesCount);
		$("#shimmerInfo .abstractClassesCount").text(metaData.abstractClassesCount);
		$("#shimmerInfo .abstractness").text(metaData.abstractness);
		$("#shimmerInfo .efferentsCount").text(metaData.efferentsCount);
		$("#shimmerInfo .afferentsCount").text(metaData.afferentsCount);
		$("#shimmerInfo .instability").text(metaData.instability);
		$("#shimmerInfo .distanceFromMainSequence").text(metaData.distanceFromMainSequence);
	},
	
	getRandom: function(from, to) {
		return Math.floor((Math.random() * to) + from);
	},
	
	getRandomColor: function() {
		if (Shimer.getRandom(1, 100) < 15) {
			return 'red';
		} else if (Shimmer.getRandom(1, 100) > 85) {
			return 'green';
		} else {
			return 'lightgray';
		}
	}
	
};

/**
 * Funkcje główne portalu
 */
var ShimmerWeb = {
		
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