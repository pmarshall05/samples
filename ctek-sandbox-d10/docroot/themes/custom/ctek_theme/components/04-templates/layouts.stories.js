import fullWidthTwig from './full-width.twig';
import withSidebarTwig from './with-sidebar.twig';

import footerSocial from '../02-molecules/menus/social-menu/social-menu.yml';
import footerMenu from '../02-molecules/menus/footer/footer.yml';
import breadcrumbData from '../02-molecules/menus/breadcrumbs/breadcrumbs.yml';
import mainMenuData from '../02-molecules/menus/main-menu/main-menu.yml';
import utilityMenuData from '../02-molecules/menus/utility-menu/utility-menu.yml';
import usertypeData from '../02-molecules/selector/user-type/user-type.yml';
import languageData from '../02-molecules/selector/language/language.yml';

import '../02-molecules/menus/main-menu/main-menu';
import '../02-molecules/menus/utility-menu/utility-menu';
import '../02-molecules/selector/user-type/user-type';
import '../02-molecules/selector/language/language';

/**
 * Storybook Definition.
 */
export default {
  title: 'Templates/Layouts',
  parameters: {
    layout: 'fullscreen',
  },
};

export const fullWidth = () =>
  fullWidthTwig({
    ...breadcrumbData,
    ...mainMenuData,
    ...utilityMenuData,
    ...usertypeData,
    ...languageData,
    ...footerSocial,
    ...footerMenu,
  });

export const withSidebar = () =>
  withSidebarTwig({
    ...breadcrumbData,
    ...mainMenuData,
    ...utilityMenuData,
    ...usertypeData,
    ...languageData,
    ...footerSocial,
    ...footerMenu,
  });
