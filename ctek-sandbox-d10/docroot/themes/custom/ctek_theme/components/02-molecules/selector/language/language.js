Drupal.behaviors.languageSelector = {
	attach(context) {
		const langDropdown = context.getElementsByClassName('language-selector__dropdown')[0];
		const langModal = context.getElementsByClassName('language-selector__modal')[0];
		const langClose = context.getElementsByClassName('language-selector__modal-close')[0];
		const langRadios = context.getElementsByClassName('language-selector__modal-radios')[0];
		const langRadioItems = langRadios.getElementsByTagName('li');
		const langHeader = context.getElementsByClassName('language-selector__modal-header')[0];
		const langSubhead = langHeader.getElementsByClassName('subhead')[0];

		// Select default
		function selectDefault() {
			var code = null;
			var selectedCode = window.localStorage.getItem('language');

			selectedCode == null ? 'en' : selectedCode;

			for (var i = 0; i < langRadioItems.length; i++) {
				code = langRadioItems[i].getElementsByTagName('input')[0].getAttribute('data-code');

				if (code == selectedCode) {
					langRadioItems[i].getElementsByTagName('input')[0].setAttribute('checked','checked');
				}
			}
		}

		// Page Load: Select Default
		selectDefault();

		langDropdown.addEventListener('click', (e) => {
			var toggleExpand = langDropdown.getElementsByTagName("i")[0];
			
    		// Modal Show/Hide.
			if (langModal.style.display != 'none'){
				langModal.style.display = 'none';
				toggleExpand.remove();
				langDropdown.innerHTML += '<i class="fa-solid fa-angle-down"></i>';
			} else {
				langModal.style.display = 'flex';
				toggleExpand.remove();
				langDropdown.innerHTML += '<i class="fa-solid fa-angle-up"></i>';
			}
			e.preventDefault();
		});

		langClose.addEventListener('click', (e) => {
			var toggleExpand = langDropdown.getElementsByTagName("i")[0];

    		// Close Modal
			langModal.style.display = 'none';
			toggleExpand.remove();
			langDropdown.innerHTML += '<i class="fa-solid fa-angle-down"></i>';
			
			e.preventDefault();
		});

		
		for (var i = 0; i < langRadioItems.length; i++) {
			langRadioItems[i].getElementsByTagName('input')[0].addEventListener('click', (e) => {
				var label = null;
				var code = null;
				var url = null;
				var toggleExpand = langDropdown.getElementsByTagName("i")[0];
				
				if (e.target.checked) {
					code = e.target.getAttribute('data-code');
					label = e.target.parentNode.getElementsByTagName('label')[0].innerHTML.trim();

					// Save in Local Storage
					window.localStorage.setItem('language', code);

					// Update Dropdown
					langDropdown.getElementsByTagName('div')[0].innerHTML = label;

					// Update Subhead
					langSubhead.innerHTML = label;

					// Close Modal
					langModal.style.display = 'none';
					toggleExpand.remove();
					langDropdown.innerHTML += '<i class="fa-solid fa-angle-down"></i>';

					// Reload Page
					url = window.location.href;    
					if (url.indexOf('?') > -1){
					   url += '&language=' + code;
					} else {
					   url += '?language=' + code;
					}
					window.location.href = url;
					

					e.preventDefault();
				}
			});
		}
	}
};
