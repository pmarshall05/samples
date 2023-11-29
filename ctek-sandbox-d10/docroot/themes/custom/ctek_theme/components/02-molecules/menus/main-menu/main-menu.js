Drupal.behaviors.mainMenu = {
  attach(context) {
    const map = new Map();
    const menuItems = context.getElementsByClassName('main-menu__item');

    // Main Menu Div Show/Hide.
    for (var i = 0; i < menuItems.length; i++) {
      menuItems[i].addEventListener('click', (e) => {
        var subMenu = null;
        var link = null;
        var toggleExpand = null;

        if (e.target.tagName.toLowerCase() == 'a') {
          subMenu = e.target.parentElement.getElementsByClassName('main-menu__item-submenu')[0];
          link = e.target;
        } else {
          subMenu = e.target.parentNode.parentNode.getElementsByClassName('main-menu__item-submenu')[0];
          link = e.target.parentNode;
        }

        toggleExpand = link.getElementsByTagName("i")[0];
        
        if (subMenu.style.display == 'flex') {
          subMenu.style.display = 'none';
          toggleExpand.remove();
          link.innerHTML += '<i class="fa-solid fa-angle-down"></i>';
        } else {
          //close all other open menus first
          closeSubmenus();
          subMenu.style.display = 'flex';
          toggleExpand.remove();
          link.innerHTML += '<i class="fa-solid fa-angle-up"></i>';
        }
        e.preventDefault();
      });
    }

    function closeSubmenus() {
      for (var i = 0; i < menuItems.length; i++) {
        var subMenu = menuItems[i].getElementsByClassName('main-menu__item-submenu')[0];
        var link = menuItems[i].getElementsByClassName('link')[0];
        var toggleExpand = link.getElementsByTagName("i")[0];

        
        if (subMenu.style.display == 'flex') {
          subMenu.style.display = 'none';
          toggleExpand.remove();
          link.innerHTML += '<i class="fa-solid fa-angle-down"></i>';
        }
      }
    }

    //console.log(map);
  }
};
