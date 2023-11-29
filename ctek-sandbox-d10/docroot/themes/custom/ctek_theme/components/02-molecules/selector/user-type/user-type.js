Drupal.behaviors.userSelector = {
	attach(context) {
		const dropdown = context.getElementsByClassName('user-type-selector__dropdown')[0];
		const modal = context.getElementsByClassName('user-type-selector__modal')[0];
		const hrList = modal.getElementsByTagName('hr');
		const close = context.getElementsByClassName('user-type-selector__modal-close')[0];
		const content = context.getElementsByClassName('user-type-selector__modal-content')[0];
		const radios = context.getElementsByClassName('user-type-selector__modal-radios')[0];
		const radioItems = radios.getElementsByTagName('li');
		const linkGroups = context.getElementsByClassName('user-type-selector__modal-link-group');
		const header = context.getElementsByClassName('user-type-selector__modal-header')[0];
		const subhead = header.getElementsByClassName('subhead')[0];
		const paragraph = content.getElementsByTagName('p')[0];
		const paragraphContent = paragraph.innerHTML;
		const footer = context.getElementsByClassName('user-type-selector__modal-footer')[0];
		const footerSelect = context.getElementsByClassName('user-type-selector__modal-footer-select')[0];

		function updateUserType () {
			var matches = null;
			var label = null;
			var vowels = ['a','e','i','o','u'];

			// Check current user type
			for (var i = 0; i < radioItems.length; i++) {
				label = radioItems[i].getElementsByTagName('label')[0].innerHTML.trim();
                
				if (window.localStorage.getItem('userType') == label) {
					radioItems[i].getElementsByTagName('input')[0].checked = true;
					
					// Hide all Link Groups
					for (var x = 0; x < linkGroups.length; x++) {
						linkGroups[x].style.display = 'none';

						if (linkGroups[x].dataset.type == label) {
							linkGroups[x].style.display = 'flex';
						}
					}

    				// Update Subhead
					if (vowels.includes(label.charAt(0).toLowerCase())) {
						subhead.innerHTML = 'I am an ' + label;
					} else {
						subhead.innerHTML = 'I am a ' + label;
					}

					// Update Paragraph
					paragraph.innerHTML = 'Quick Links:';

					// Hide radios
					radios.style.display = 'none';

					// Show Footer
					hrList[1].style.display = 'block';
					footer.style.display = 'flex';
				}
			}
		}	

		hrList[1].style.display = 'none';

		updateUserType();
    		
		dropdown.addEventListener('click', (e) => {
			var toggleExpand = dropdown.getElementsByTagName("i")[0];
		
    		// Modal Show/Hide.
			if (modal.style.display != 'none'){
				modal.style.display = 'none';
				toggleExpand.remove();
				dropdown.innerHTML += '<i class="fa-solid fa-angle-down"></i>';
			}
			else {
				modal.style.display = 'flex';
				toggleExpand.remove();
				dropdown.innerHTML += '<i class="fa-solid fa-angle-up"></i>';
			}
			e.preventDefault();
		});

		close.addEventListener('click', (e) => {
			var toggleExpand = dropdown.getElementsByTagName("i")[0];

    		// Close Modal
			modal.style.display = 'none';
			toggleExpand.remove();
			dropdown.innerHTML += '<i class="fa-solid fa-angle-down"></i>';
			
			e.preventDefault();
		});

		footerSelect.addEventListener('click', (e) => {
			// Hide Footer
    		hrList[1].style.display = 'none';
    		footer.style.display = 'none';

    		// Uncheck all radio items
    		for (var i = 0; i < radioItems.length; i++) {
    			radioItems[i].getElementsByTagName('input')[0].checked = false;
    		}

    		// Hide Link Groups
    		for (var x = 0; x < linkGroups.length; x++) {
				linkGroups[x].style.display = 'none';
			}

			// Show Radios
			radios.style.display = 'flex';

			// Update Subhead
			subhead.innerHTML = 'I am a . . .';

			// Update Paragraph
			paragraph.innerHTML = paragraphContent;
			
			e.preventDefault();
		});

		
		for (var i = 0; i < radioItems.length; i++) {
			radioItems[i].getElementsByTagName('input')[0].addEventListener('click', (e) => {
				var label = '';
				var vowels = ['a','e','i','o','u'];


				if (e.target.checked) {
					//Get Label
					label = e.target.parentNode.getElementsByTagName('label')[0].innerHTML.trim();

					// Save in Local Storage
					window.localStorage.setItem('userType', label);

					// Update User Type
					updateUserType();

				}
			});
		}
	}
};
