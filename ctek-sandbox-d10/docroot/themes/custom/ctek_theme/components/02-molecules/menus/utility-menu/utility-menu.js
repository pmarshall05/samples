Drupal.behaviors.utilityMenu = {
  attach(context) {
    const toggleExpand = context.getElementById('toggle-location');
    const currentLocationForm = context.getElementById('current-location-form');

    // Location Show/Hide.
      toggleExpand.addEventListener('click', (e) => {
        if (currentLocationForm.style.display != 'none'){
        	currentLocationForm.style.display = 'none';
        	toggleExpand.innerHTML = '<i class="fa-solid fa-angle-down"></i>';
        }
        else {
        	currentLocationForm.style.display = 'flex';
        	toggleExpand.innerHTML = '<i class="fa-solid fa-angle-up"></i>';
        }
        e.preventDefault();
      });
    }
};
