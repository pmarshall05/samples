Drupal.behaviors.relatedLinks = {
  attach(context) {
    const relatedLinks = context.getElementsByClassName('related-links');

    if (relatedLinks != null && relatedLinks.length > 0) {
      for (var x = 0; x < relatedLinks.length; x++) {
        var relatedLinksButton = relatedLinks[x].getElementsByClassName('related-links__button')[0].getElementsByClassName('button')[0];
        var relatedLinksMoreItems = relatedLinks[x].getElementsByClassName('related-links__item-more');

      	relatedLinksButton.addEventListener('click', (e) => {
      		var style = null;
      		var label = null;

      		if (relatedLinksMoreItems != null && relatedLinksMoreItems.length > 0) {
            for (var i = 0; i < relatedLinksMoreItems.length; i++) {
        			if (relatedLinksMoreItems[i].style.display == 'none') {
        				style = 'flex';
        				label = 'Show Less';
        			} else {
        				style = 'none';
        				label = 'Show More';
        			}
        				
              relatedLinksMoreItems[i].style.display = style;
      			}

            e.target.innerHTML = label;
      	  }
        });
      }
    }
  }
};