QB = {};

(function() {

	//try { console.log('init console...asd.'); } catch(e) { console = { log: function() {} } }
	
	var buildPropertyCondition = function(panel, propertyUri){
		var propertyCondition = {
				propertyUri : propertyUri
			};

		var propertyConditionElem = panel
				.children('.property-condition');

		var operatorSelectElem = propertyConditionElem
				.children('.operator-select');
		if (operatorSelectElem.length > 0) {
			propertyCondition.operator = operatorSelectElem.val();
		}

		var valueConditionElem = propertyConditionElem
				.children('.value-condition');
		if (valueConditionElem.length > 0) {
			var dataTypeValueElem = $('>.property-value',valueConditionElem);
			if (dataTypeValueElem.length > 0) {
				propertyCondition.datatypeValue = dataTypeValueElem.val();
			}
			
			var individualSelectElem = $('>.individual-panel>.individual-select',valueConditionElem);
			if (individualSelectElem.length > 0) {
				propertyCondition.individualValue = individualSelectElem.val();
			}

			var constrainedValueElem = valueConditionElem
					.children('.constrained-value');
			console.log('CONSTRAINED VALUE');
			console.log(constrainedValueElem);
			if (constrainedValueElem.length > 0) {
				var classUri = $('>.class-panel>.class-select',valueConditionElem).val();
				propertyCondition.classConstraintValue = QB.buildCondition(classUri, constrainedValueElem);
			}
		}
		return propertyCondition;
	};	
	
	QB.buildCondition = function(classUri, classConditionPanel) {
		//console.log("********************");
		//console.log(classURi);
		//console.log("********************");
		
	
		//console.log('CLASS CONDITION PANEL:');
		//console.log(classConditionPanel);
		//console.log('CLASS:');
		//console.log(classUri);
		var classCondition = {
			classUri : classUri,
			propertyConditions : []
		};

		//console.log(classConditionPanel.children('.condition-panel'));
		classConditionPanel
				.find('.condition-panel')
				.each(
						function(index, panel) {
							panel = $(panel);
							//console.log("asd "+index);
							//console.log('PANEL:')
							//console.log(panel);
							var propertyUri = $('.property-select', panel)
									.val();
							if (propertyUri === 'off')
								return;

							var propertyCondition = buildPropertyCondition(panel, propertyUri);

							classCondition.propertyConditions
									.push(propertyCondition);
						});
		return classCondition;
	}
	
	
})();

(function( $ ) {
	  $.fn.startMonitoringForReplies = function(getRepliesAction, actionParams, interval) {
		  var context = this;
		  var intervalId = setInterval(function(){
			  context.load(
					   getRepliesAction(actionParams)
					   );
		  }, interval);		
		  //clear previous interval
		  var oldIntervalId = this.data('monitoringId');
		  if(oldIntervalId){
			  clearInterval(oldIntervalId);
		  }
		  this.data('monitoringId', intervalId);
	  };
	  
	  $.fn.stopMonitoringForReplies = function() {
		  //clear previous interval
		  var oldIntervalId = this.data('monitoringId');
		  if(oldIntervalId){
			  clearInterval(oldIntervalId);
		  }
	  };
	})( jQuery );	