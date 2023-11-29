Drupal.behaviors.relatedMedia = {
	attach(context) {
		const relatedMedia = context.getElementsByClassName('related-media');
		const viewport = window.innerWidth;


    	if (relatedMedia != null && relatedMedia.length > 0) {
      		for (var x = 0; x < relatedMedia.length; x++) {
      			var relatedMediaCards = relatedMedia[x].getElementsByClassName('related-media__card');
        		var relatedMediaLeft = relatedMedia[x].getElementsByClassName('related-media__pager-left')[0];
        		var relatedMediaRight = relatedMedia[x].getElementsByClassName('related-media__pager-right')[0];
        		var relatedMediaCard = relatedMediaCards[0];
        		var totalPages = getTotalPages(relatedMediaCards);

        		if (totalPages == 1) {
        			relatedMediaLeft.style.display ='none';
        			relatedMediaRight.style.display ='none';
        		} else {

	        		relatedMediaLeft.addEventListener('click', (e) => {
	        			var newIndex = 0;
	        			var currentIndex = getCurrentIndex(relatedMediaCards);
	        			var page = getCurrentPage(currentIndex);
	        			var pageWidth = getPageWidth(relatedMediaCard);
	        			var pages = getTotalPages(relatedMediaCards);
	        			var scrollLeft = relatedMediaCard.parentNode.scrollLeft
	        			var offset = 0;

	        			if (page > 1) {
	        				offset = pageWidth + 32;
	        				relatedMediaCard.parentNode.scrollLeft = scrollLeft - offset;
	        			}

	        			newIndex = setNewIndex('left', currentIndex,relatedMediaCards);
	        			page = getCurrentPage(newIndex);

	        			if (page == 1) {
	        				e.target.style.background = "#f5f0f0";
	        				e.target.children[0].style.color = "#969691";
	        				relatedMediaRight.style.background = "#72bf44";
	        				relatedMediaRight.children[0].style.color = "#ffffff";
	        			}

	        			if (page < pages) {
	        				relatedMediaRight.style.background = "#72bf44";
	        				relatedMediaRight.children[0].style.color = "#ffffff";
	        			}
	        		});

	        		relatedMediaRight.addEventListener('click', (e) => {
	        			var newIndex = 0;
	        			var currentIndex = getCurrentIndex(relatedMediaCards);
	        			var page = getCurrentPage(currentIndex);
	        			var pageWidth = getPageWidth(relatedMediaCard);
	        			var pages = getTotalPages(relatedMediaCards);
	        			var scrollLeft = relatedMediaCard.parentNode.scrollLeft
	        			var offset = 0;
	        			
	        			if (page < pages) {
	        				offset = pageWidth + 32;
	        				relatedMediaCard.parentNode.scrollLeft = scrollLeft + offset;
	        			}

	        			newIndex = setNewIndex('right', currentIndex,relatedMediaCards);
	        			page = getCurrentPage(newIndex);

	        			if (page == pages) {
	        				e.target.style.background = "#f5f0f0";
	        				e.target.children[0].style.color = "#969691";
	        				relatedMediaLeft.style.background = "#72bf44";
	        				relatedMediaLeft.children[0].style.color = "#ffffff";
	        			}

	        			if (page > 1) {
	        				relatedMediaLeft.style.background = "#72bf44";
	        				relatedMediaLeft.children[0].style.color = "#ffffff";
	        			}
	        		});
	        	}

      		}
		}

		function getPageWidth(card) {
			var pageWidth = 0;

			if (viewport >= 992) { /* medium to XXL */
				pageWidth = (card.offsetWidth * 2) + 32;
			} else {
				pageWidth = card.offsetWidth;
			}

			return pageWidth;
		}

		function getCurrentPage(current) {
			var page = 0;

			if (viewport >= 992) { /* medium to XXL */
				page = Math.ceil((current/2).toFixed(2));
			} else {
				page = current;
			}

			return page;
		}

		function getTotalPages(cards) {
			var pages = 0;

			if (viewport >= 992) { /* medium to XXL */
				pages = Math.ceil((cards.length/2).toFixed(2));
			} else {
				pages = cards.length;
			}

			return pages;
		}

		function getCurrentIndex(cards) {
			var classList = null;
        			
			var current = 0;

			for (var i = 0; i < cards.length; i++) {
    			if (cards[i].classList.contains('current')) {
    				current = i;
    				break;
    			}
    		}

    		current = current + 1;

			return current;
		}

		function setNewIndex(direction, current, cards) {
			var newCurrent = 0;
			var classList = null;
			var index = 0;
			current = current - 1;
			
			if (viewport >= 992 && direction == 'left') { /* medium to XXL */
				newCurrent = current - 2;
			} else if (viewport < 992 && direction == 'left') { /* xs to sm */
				newCurrent = current - 1;
			} else if (viewport >= 992 && direction == 'right') { /* medium to XXL */
				newCurrent = current + 2;
			} else {
				newCurrent = current + 1;
			}

            cards[current].classList.remove('current');
            cards[newCurrent].classList.add('current');

            newCurrent = newCurrent + 1;

			return newCurrent ;
		}
	}
}